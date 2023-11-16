FROM maven:3.9.4-eclipse-temurin-21@sha256:a9b1581fc335b6eb0f56e2c884e870dbfb6519571475a06a941143d407c51bf7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:17@sha256:ad6772e9844819032ddf606c1eb0d62df32c16dd14fb809c1543f0529cfff580
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
