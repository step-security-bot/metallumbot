FROM maven:3.9.1-eclipse-temurin-17@sha256:471d9f0b11063569cc12ca07d6b44fa9a0bc17cde17535614275f82d4b0d92b6 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:17@sha256:ad6772e9844819032ddf606c1eb0d62df32c16dd14fb809c1543f0529cfff580
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
