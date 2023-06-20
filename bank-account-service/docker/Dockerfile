FROM eclipse-temurin:17-jdk-jammy

RUN apt-get update && \
    apt-get install -y mysql-client

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn ./.mvn

RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:resolve

COPY src ./src

COPY ./docker/entrypoint.sh /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

EXPOSE 8080

CMD ["./entrypoint.sh"]
