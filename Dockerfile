FROM --platform=linux/amd64 maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml ./
COPY src ./src

RUN mvn -B clean package -DskipTests -Djavafx.platform=linux

FROM --platform=linux/amd64 eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends xvfb libgtk-3-0 libgl1 libxi6 libxrender1 libxtst6 libxxf86vm1 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /build/target/fuelcons.jar /app/fuelcons.jar

# Keep current behavior by default; set APP_DISPLAY_MODE=x11 to use a real X display.
ENV APP_DISPLAY_MODE=xvfb

CMD ["sh", "-c", "if [ \"$APP_DISPLAY_MODE\" = \"x11\" ] || [ \"$APP_DISPLAY_MODE\" = \"visible\" ]; then exec java -jar /app/fuelcons.jar; elif [ \"$APP_DISPLAY_MODE\" = \"xvfb\" ] || [ \"$APP_DISPLAY_MODE\" = \"headless\" ]; then exec xvfb-run -a java -jar /app/fuelcons.jar; else echo \"Unsupported APP_DISPLAY_MODE: $APP_DISPLAY_MODE\" >&2; exit 1; fi"]
