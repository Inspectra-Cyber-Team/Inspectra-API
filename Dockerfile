#build file jar file here
FROM openjdk:21-jdk-slim  AS builder
COPY . .
# Ensure Gradle Wrapper is executable
RUN chmod +x ./gradlew

# Build the project and ignore tests
RUN ./gradlew build --no-daemon -x test




# Use a base image with JDK 21
FROM openjdk:21-jdk-slim

# Install dependencies and tools for version checking
RUN apt-get update && apt-get install -y \
    php-cli \
    unzip \
    curl \
    jq \
    python3 \
    python3-pip \
    apt-transport-https \
    ca-certificates \
    gnupg \
    lsb-release \
    && rm -rf /var/lib/apt/lists/*


# Install Gradle
ENV GRADLE_VERSION=8.5
RUN curl -L https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip
RUN unzip gradle.zip && rm gradle.zip
RUN mv gradle-${GRADLE_VERSION} /opt/gradle
ENV PATH=$PATH:/opt/gradle/bin

# Install Maven
ENV MAVEN_VERSION=3.9.5
RUN curl -L https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz \
    && tar xzf maven.tar.gz \
    && rm maven.tar.gz \
    && mv apache-maven-${MAVEN_VERSION} /opt/maven \
    && ln -s /opt/maven/bin/mvn /usr/local/bin/mvn

# Install SonarScanner CLI
ENV SONAR_SCANNER_VERSION=5.0.1.3006
RUN curl -LO https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && unzip sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && mv sonar-scanner-${SONAR_SCANNER_VERSION}-linux /opt/sonar-scanner \
    && rm sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner

# Install Node.js
ENV NODE_VERSION=18.x
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && npm install -g npm

# Set PATH for Node.js
ENV PATH=$PATH:/usr/bin/node:/usr/bin/npm

# Install Composer
ENV COMPOSER_VERSION=2.6.2
RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer \
    && composer --version


# Update and install required packages
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    python3-venv \
    && rm -rf /var/lib/apt/lists/*

# Create and activate a virtual environment, then install Python libraries
RUN python3 -m venv /opt/venv \
    && /opt/venv/bin/pip install --no-cache-dir django fastapi uvicorn sqlalchemy pydantic

# Add the virtual environment's binaries to PATH
ENV PATH="/opt/venv/bin:$PATH"

# Install Go CLI
ENV GO_VERSION=1.21.3
RUN curl -LO https://golang.org/dl/go${GO_VERSION}.linux-amd64.tar.gz \
    && tar -C /usr/local -xzf go${GO_VERSION}.linux-amd64.tar.gz \
    && rm go${GO_VERSION}.linux-amd64.tar.gz
ENV PATH=$PATH:/usr/local/go/bin





# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the local machine to the container
COPY --from=builder build/libs/*.jar  app.jar


# Create a directory for configuration files
RUN mkdir -p /app/config

# Copy the application.yml file
COPY src/main/resources/application-prod.yml /app/config/

# Expose the port your application will run on
EXPOSE 8080

# Define the command to run your application
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=prod", "app.jar", "--spring.config.location=file:/app/config/application-prod.yml"]