# Use an official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

# Install Gradle
RUN apt-get update && \
    apt-get install -y wget unzip git && \
    wget https://services.gradle.org/distributions/gradle-7.6-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-7.6-bin.zip && \
    rm /tmp/gradle-7.6-bin.zip && \
    ln -s /opt/gradle/gradle-7.6/bin/gradle /usr/bin/gradle

# Set environment variables for Gradle
ENV GRADLE_HOME /opt/gradle/gradle-7.6
ENV PATH ${GRADLE_HOME}/bin:$PATH

RUN git clone https://github.com/ruben1191018/cogsi-1190914-1191018-1191042.git /app

WORKDIR /app/ca2/part2

# Run the Gradle build to generate the application JAR
RUN gradle build --no-daemon

# Expose the port the Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app/build/libs/app.jar"]
