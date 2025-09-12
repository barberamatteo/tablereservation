FROM --platform=$BUILDPLATFORM maven:3.8.3-amazoncorretto-17 AS builder
WORKDIR /build/server
COPY pom.xml /build/server/pom.xml
RUN mvn dependency:go-offline

COPY src /build/server/src
RUN mvn package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /appruntime
COPY --from=builder /build/server/target/*.jar tablereservation.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "tablereservation.jar"]
