# Build stage
FROM gradle:8.5-jdk21-alpine AS build
WORKDIR /app

# Copy gradle files
COPY settings.gradle.kts .
COPY home-inventory-management/build.gradle.kts ./home-inventory-management/

# Copy source code
COPY home-inventory-management/src ./home-inventory-management/src

# Build the application
WORKDIR /app/home-inventory-management
RUN gradle clean build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar from build stage
COPY --from=build /app/home-inventory-management/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
