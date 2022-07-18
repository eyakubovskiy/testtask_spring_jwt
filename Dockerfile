FROM openjdk:17-alpine
WORKDIR /app
COPY target/testtask_spring_jwt-0.0.1-SNAPSHOT.jar /app/testtask_spring_jwt.jar
EXPOSE 9876
ENTRYPOINT java -jar testtask_spring_jwt.jar
