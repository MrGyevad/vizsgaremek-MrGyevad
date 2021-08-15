FROM adoptopenjdk:16-jre-hotspot
RUN mkdir /opt/app
COPY target/animal-shelter-1.0-SNAPSHOT.jar /opt/app/animal-shelter.jar
CMD ["java", "-jar", "/opt/app/animal-shelter.jar"]