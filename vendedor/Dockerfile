FROM openjdk:11.0

# Add Maintainer Info
LABEL maintainer="arthur.diego7@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

EXPOSE 9899

# The application's jar file
ARG JAR_FILE=./target/vendedor-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} vendedor.jar

ENTRYPOINT ["java", "-jar", "/vendedor.jar"]