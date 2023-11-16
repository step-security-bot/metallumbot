FROM maven:3.9.1-eclipse-temurin-17@sha256:471d9f0b11063569cc12ca07d6b44fa9a0bc17cde17535614275f82d4b0d92b6 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21@sha256:6054ac3d599388ceca4916f3d0e1864e24cdd14bb760e662f740dfb9b9e3bee8
COPY --from=build /home/app/target/metallumbot-0.0.1.jar /usr/local/lib/metallumbot.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/metallumbot.jar"]
