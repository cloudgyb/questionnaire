FROM openjdk:11

ARG JAR_FILE=./target/*.jar

COPY ${JAR_FILE} /app.jar

ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB=questionnaire
ENV DB_USER=questionnaire
ENV DB_PASSWORD=123456
ENV ES_ADDRESS=localhost:9200

EXPOSE 8080

CMD ["java","-jar","-Dspring.profiles.active=docker","/app.jar"]


