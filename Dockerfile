FROM eclipse-temurin:17-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 6974
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
