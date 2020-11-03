FROM openjdk
COPY ./target/api-0.0.1-SNAPSHOT.jar /apps/
CMD [ "java", "-jar", "/apps/api-0.0.1-SNAPSHOT.jar" ]