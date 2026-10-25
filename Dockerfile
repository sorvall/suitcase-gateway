# Java 21 runtime (образ openjdk:* на Docker Hub больше не публикуется)
FROM eclipse-temurin:21-jre-jammy

# Указываем рабочую директорию в контейнере
WORKDIR /app

# Копируем JAR файл в контейнер
COPY build/libs/suitcase-gateway-0.0.1-SNAPSHOT.jar /app/suitcase-gateway.jar

# HTTPS обслуживает nginx перед gateway, сам gateway слушает HTTP внутри docker-сети
EXPOSE 8080

USER root

# Запускаем JAR файл
CMD ["java", "-jar", "suitcase-gateway.jar"]
