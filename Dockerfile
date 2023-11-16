FROM maven:3.9.4-eclipse-temurin-21@sha256:a9b1581fc335b6eb0f56e2c884e870dbfb6519571475a06a941143d407c51bf7 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:6054ac3d599388ceca4916f3d0e1864e24cdd14bb760e662f740dfb9b9e3bee8
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
