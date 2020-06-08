FROM openjdk:11.0

# Add Maintainer Info
LABEL maintainer="arthur.diego7@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8085 available to the world outside this container
EXPOSE 9898

# The application's jar file
ARG JAR_FILE=./target/poc-venda-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} poc-venda.jar

ENTRYPOINT ["java", "-jar", "/poc-venda.jar"]