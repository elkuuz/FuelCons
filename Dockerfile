FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml ./
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends xvfb libgtk-3-0 libgl1 libxi6 libxrender1 libxtst6 libxxf86vm1 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /build/target/fuelcons.jar /app/fuelcons.jar

# Run JavaFX in a virtual framebuffer so the app can start in containerized environments.
CMD ["xvfb-run", "-a", "java", "-jar", "/app/fuelcons.jar"]
