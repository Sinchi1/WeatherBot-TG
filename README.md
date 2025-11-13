# 🌦 WeatherBot

WeatherBot — это Telegram-бот, который получает от пользователя геолокацию и возвращает прогноз погоды, используя API [Open-Meteo](https://open-meteo.com).
Архитектура разделена на два микросервиса: **TgBot** (интерфейс с Telegram) и **BackEnd** (обработка данных и взаимодействие с внешним API).
Обмен сообщениями между сервисами осуществляется через **Apache Kafka**.

---

## 🧩 Архитектура

```
[Telegram User] ⇄ [TgBot Service] ⇄ Kafka ⇄ [BackEnd Service] ⇄ Open-Meteo API
```

---

## ⚙️ Компоненты проекта

### 1. TgBot (Telegram Bot Service)

* Получает сообщения и геолокацию от пользователей.
* Отправляет запросы в Kafka-топик `weather-requests`.
* Принимает ответы из Kafka-топика `weather-response`.
* Отправляет прогноз пользователю в Telegram.

**Основные классы:**

* `WeatherBotGate` — реализация TelegramLongPollingBot.
* `WeatherProducer` — отправка сообщений в Kafka.
* `WeatherConsumer` — получение прогнозов из Kafka.

### 2. BackEnd (Weather Processing Service)

* Получает запросы из Kafka (`weather-requests`).
* Делает запрос к внешнему API Open-Meteo.
* Формирует ответ `WeatherResponse` и отправляет в Kafka (`weather-responses`).
* Сохраняет историю запросов в PostgreSQL.

**Основные классы:**

* `WeatherConsumer` — слушает Kafka и вызывает `CbrClient`.
* `CbrClient` — выполняет HTTP-запрос к Open-Meteo API.
* `WeatherParser` — парсит JSON-ответ.
* `WeatherRepository` — JPA-репозиторий для хранения данных.

---

## 🗄 Docker Compose

В проекте используется `docker-compose.yaml`, который поднимает:

* Два контейнера PostgreSQL (для TgBot и BackEnd);
* Kafka и Zookeeper.

Запуск:

```bash
docker-compose up -d
```

Порты по умолчанию:

* **PostgreSQL TgBot** — `5434`
* **PostgreSQL BackEnd** — `5433`
* **Kafka** — `9092`

---

## 🚀 Запуск проекта

1. Убедитесь, что установлен Docker и Java 17+.

2. Запустите инфраструктуру:

   ```bash
   docker-compose up -d
   ```

3. Запустите оба микросервиса:

   ```bash
   # TgBot
   cd TgBot
   ./mvnw spring-boot:run

   # BackEnd
   cd BackEnd
   ./mvnw spring-boot:run
   ```

4. В Telegram найдите бота и отправьте команду:

   ```
   /start
   ```

5. Отправьте свою геолокацию и получите прогноз погоды.

---

## ⚡ Переменные окружения

В файлах `application.yaml` можно задать настройки:

**TgBot:**

```yaml
bot:
  name: "WeatherBot"
  token: "<ВАШ_TELEGRAM_BOT_TOKEN>"
```

**BackEnd:**

```yaml
weather:
  url: "https://api.open-meteo.com/v1/forecast"
```

---

## 🧠 Технологии

* **Java 17**
* **Spring Boot 3**
* **Kafka** (Confluent)
* **PostgreSQL**
* **OkHttp** — HTTP-клиент для API-запросов
* **Jackson** — парсинг JSON
* **Lombok** — генерация boilerplate-кода

---

## 📚 Пример взаимодействия

1. Пользователь отправляет боту свою геолокацию.
2. TgBot отправляет объект `WeatherRequest` в Kafka.
3. BackEnd получает запрос, обращается к API, формирует `WeatherResponse`.
4. TgBot получает ответ из Kafka и отправляет сообщение пользователю:

```
☀️ Sunny weather
Temperature: +18.4°C
```

