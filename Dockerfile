### Builder stage
##FROM gradle:8.4-jdk17-alpine AS builder
##
##WORKDIR /app
##COPY . .
##
### Install required dependencies and build the project
##RUN gradle build --no-daemon -x test
##
### Install Sonar Scanner CLI
##ENV SONAR_SCANNER_VERSION=5.0.1.3006
##RUN apk add --no-cache curl zip unzip \
##    && curl -LO https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
##    && unzip sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
##    && mv sonar-scanner-${SONAR_SCANNER_VERSION}-linux /opt/sonar-scanner \
##    && rm sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
##    && ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner
##
### Final stage
##FROM openjdk:17-alpine3.18
##
##WORKDIR /app
##
### Copy the JAR file built in the builder stage
##COPY --from=builder /app/build/libs/*.jar app.jar
##
### Copy Gradle and Sonar Scanner from builder stage
##COPY --from=builder /opt/gradle /opt/gradle
##COPY --from=builder /opt/sonar-scanner /opt/sonar-scanner
##
### Make Gradle and Sonar Scanner available in the final image
##RUN ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle
##RUN ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner
##
### Expose port and mount volumes
##EXPOSE 8080
##VOLUME /home/istad/media
##VOLUME /keys
##
### Run the application
##ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
#
## Builder stage
#FROM gradle:8.4-jdk17-alpine AS builder
#
#WORKDIR /app
#COPY . .
#
## Install required dependencies and build the project
#RUN gradle build --no-daemon -x test
#
## Install Sonar Scanner CLI
#ENV SONAR_SCANNER_VERSION=5.0.1.3006
#RUN apk add --no-cache curl zip unzip \
#    && curl -LO https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
#    && unzip sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
#    && mv sonar-scanner-${SONAR_SCANNER_VERSION}-linux /opt/sonar-scanner \
#    && rm sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
#    && ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner
#
## Final stage
#FROM openjdk:17-alpine3.18
#
#WORKDIR /app
#
## Copy the JAR file built in the builder stage
#COPY --from=builder /app/build/libs/*.jar app.jar
#
## Copy Gradle and Sonar Scanner from builder stage
#COPY --from=builder /opt/gradle /opt/gradle
#COPY --from=builder /opt/sonar-scanner /opt/sonar-scanner
#
## Make Gradle and Sonar Scanner available in the final image
#RUN ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle
#RUN ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner
#
## Configure Sonar Scanner to use the system JRE
#ENV SONAR_SCANNER_OPTS="-Djava.home=/usr/lib/jvm/java-17-openjdk"
#
## Expose port and mount volumes
#EXPOSE 8080
#VOLUME /home/istad/media
#VOLUME /keys
#
## Run the application
#ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]




FROM openjdk:21-jdk-slim  AS builder
COPY . .
RUN gradle build --no-daemon -x test




# Use a base image with JDK 21
FROM openjdk:21-jdk-slim

# Install dependencies and tools for version checking
RUN apt-get update && apt-get install -y \
    curl zip unzip jq \
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

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the local machine to the container
COPY --from=builder /app/build/libs/*.jar app.jar

# Create a directory for configuration files
RUN mkdir -p /app/config

# Copy the application.yml file
COPY src/main/resources/application-prod.yml /app/config/

# Expose the port your application will run on
EXPOSE 8081

# Define the command to run your application
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=prod", "app.jar", "--spring.config.location=file:/app/config/application-prod.yml"]