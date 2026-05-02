# Java 21 runtime (образ openjdk:* на Docker Hub больше не публикуется)
FROM eclipse-temurin:21-jre-jammy

# Указываем рабочую директорию в контейнере
WORKDIR /app

# Копируем JAR файл в контейнер
COPY build/libs/suitcase-gateway-0.0.1-SNAPSHOT.jar /app/suitcase-gateway.jar

# Копируем keystore.p12 в контейнер
COPY keystore.p12 /app/keystore.p12

# Открываем порты
EXPOSE 443

USER root

# Запускаем JAR файл
CMD ["java", "-jar", "suitcase-gateway.jar"]
