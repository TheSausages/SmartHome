# Build the service
FROM gradle:7.1.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# without tests
RUN gradle :Air-Humidifier:build --no-daemon --x test

# Create a runnable image
FROM openjdk:11

COPY --from=build /home/gradle/src/Air-Humidifier/build/libs/*.jar humidifier.jar

# Set entrypoint to
ENTRYPOINT ["java", "-jar", "humidifier.jar"]