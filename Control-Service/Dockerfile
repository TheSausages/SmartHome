# Build the service
FROM gradle:7.1.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# without tests
RUN gradle :Control-Service:build --no-daemon --x test

# Create a runnable image
FROM openjdk:11

COPY --from=build /home/gradle/src/Control-Service/build/libs/*.jar control.jar

# Set entrypoint to
ENTRYPOINT ["java", "-jar", "control.jar"]