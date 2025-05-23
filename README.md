# Suitcase Gateway

[![Java CI with Gradle](https://github.com/your-username/suitcase-gateway/actions/workflows/deploy.yml/badge.svg)](https://github.com/your-username/suitcase-gateway/actions/workflows/deploy.yml)

API Gateway для экосистемы приложений Suitcase, построенный на Spring Cloud Gateway.

## 🚀 Возможности

- **Маршрутизация API**: Интеллектуальная маршрутизация запросов к микросервисам
- **Балансировка нагрузки**: Встроенная клиентская балансировка нагрузки
- **Безопасность**: Защита API с помощью Spring Security (в настоящее время отключена)
- **Мониторинг**: Интеграция с Spring Boot Admin и Prometheus
- **Контейнеризация**: Готово к развертыванию в Docker

## 🛠️ Технологический стек

- Java 21
- Spring Boot 3.4.4
- Spring Cloud Gateway
- Spring Boot Actuator
- Thymeleaf (для административного интерфейса)
- Micrometer + Prometheus (метрики)
- Gradle (сборка)
- Docker

## 📦 Необходимые компоненты

- JDK 21 или новее
- Gradle 8.0 или новее
- Docker (для контейнеризованного развертывания)
- Доступ к реестру контейнеров (для продакшн-развертывания)

## 🚀 Начало работы

### Локальная разработка

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/your-username/suitcase-gateway.git
   cd suitcase-gateway
   ```

2. Соберите приложение:
   ```bash
   ./gradlew build
   ```

3. Запустите приложение:
   ```bash
   ./gradlew bootRun
   ```

Приложение будет доступно по адресу `http://localhost:8080`

### Развертывание в Docker

1. Соберите Docker-образ:
   ```bash
   docker build -t suitcase-gateway .
   ```

2. Запустите контейнер:
   ```bash
   docker run -p 443:443 suitcase-gateway
   ```

## 🔧 Конфигурация

### Переменные окружения

| Переменная | Описание | Значение по умолчанию |
|------------|----------|----------------------|
| `DASHBOARD_USER` | Имя пользователя для доступа к панели управления | your_user |
| `DASHBOARD_PASS` | Пароль для доступа к панели управления | your_pass |

### Безопасность

⚠️ **Важно**: Безопасность в приложении в настоящее время отключена. Перед развертыванием в продакшн необходимо:
1. Включить Spring Security

## 📊 Мониторинг

Приложение предоставляет следующие эндпоинты для мониторинга:

- `/actuator/health`: Состояние работоспособности приложения
- `/actuator/prometheus`: Метрики для Prometheus
- `/actuator/info`: Информация о приложении

## 🤝 Вклад в проект

1. Форкните репозиторий
2. Создайте ветку для новой функциональности (`git checkout -b feature/НоваяФункция`)
3. Зафиксируйте изменения (`git commit -m 'Добавлена новая функциональность'`)
4. Отправьте изменения в удаленный репозиторий (`git push origin feature/НоваяФункция`)
5. Откройте Pull Request

## 📄 Лицензия

Этот проект распространяется под лицензией MIT. Подробнее см. в файле [LICENSE](LICENSE).

## 🙏 Благодарности

- Создано с ❤️ с использованием Spring Boot и Spring Cloud Gateway
- Спасибо всем, кто помог в разработке проекта

---

Создано 💻 [Sorokin Valery] | [Профиль на GitHub](https://github.com/sorvall)