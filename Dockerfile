# Builder stage
FROM gradle:8.4-jdk17-alpine AS builder

WORKDIR /app
COPY . .

# Install required dependencies and build the project
RUN gradle build --no-daemon -x test

# Install Sonar Scanner CLI
ENV SONAR_SCANNER_VERSION=5.0.1.3006
RUN apk add --no-cache curl zip unzip \
    && curl -LO https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && unzip sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && mv sonar-scanner-${SONAR_SCANNER_VERSION}-linux /opt/sonar-scanner \
    && rm sonar-scanner-cli-${SONAR_SCANNER_VERSION}-linux.zip \
    && ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner

# Final stage
FROM openjdk:17-alpine3.18

WORKDIR /app

# Copy the JAR file built in the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy Gradle and Sonar Scanner from builder stage
COPY --from=builder /opt/gradle /opt/gradle
COPY --from=builder /opt/sonar-scanner /opt/sonar-scanner

# Make Gradle and Sonar Scanner available in the final image
RUN ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle
RUN ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner

# Expose port and mount volumes
EXPOSE 8080
VOLUME /home/istad/media
VOLUME /keys

# Run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
