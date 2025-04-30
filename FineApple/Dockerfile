FROM eclipse-temurin:21-jdk-alpine


WORKDIR /app


COPY mvnw pom.xml /app/
COPY .mvn /app/.mvn

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]