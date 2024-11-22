# Builder stage
FROM gradle:8.4-jdk17-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# Install required packages
RUN apk update && apk add --no-cache \
    curl zip unzip jq \
    ca-certificates \
    gnupg

# Install Gradle
ENV GRADLE_VERSION=8.5
RUN curl -L https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip \
    && unzip gradle.zip && rm gradle.zip \
    && mv gradle-${GRADLE_VERSION} /opt/gradle \
    && ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle

# Install Maven
ENV MAVEN_VERSION=3.9.5
RUN curl -L https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -o maven.tar.gz \
    && tar xzf maven.tar.gz && rm maven.tar.gz \
    && mv apache-maven-${MAVEN_VERSION} /opt/maven \
    && ln -s /opt/maven/bin/mvn /usr/local/bin/mvn

# Install SonarScanner CLI
ENV SONAR_SCANNER_VERSION=5.0.1.3006
RUN curl -LO https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && unzip sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && mv sonar-scanner-${SONAR_SCANNER_VERSION}-linux /opt/sonar-scanner \
    && rm sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner

# Final stage
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
VOLUME /home/istad/media
VOLUME /keys
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]