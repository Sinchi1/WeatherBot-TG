package org.truskovski.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.truskovski.client.CbrClient;
import org.truskovski.store.dto.WeatherRequest;
import org.truskovski.store.dto.WeatherResponse;
import org.truskovski.store.repository.WeatherRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherConsumer {

    private final CbrClient cbrClient;
    private final KafkaTemplate<String, WeatherResponse> kafkaTemplate;
    private final WeatherRepository weatherRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "weather-requests")
    public void consume(WeatherRequest weatherRequest) throws IOException {
        String apiResponse = cbrClient.getWeatherForecastByRequest(weatherRequest);
        log.info("Info from request{}", cbrClient.getWeatherForecastByRequest(weatherRequest));
        JsonNode jsonNode = new ObjectMapper().readTree(apiResponse);
        double temp = jsonNode.path("hourly").path("temperature_2m").get(0).asDouble();
        String weatherCode = getDescriptionOfWeather(jsonNode.path("daily").path("weather_code").get(0).asText());
        log.info("Weather Code: {}", jsonNode.path("daily").path("weather_code").get(0).asText());

        WeatherResponse response = new WeatherResponse(
                weatherRequest.userId(),
                weatherRequest.chatId(),
                "По вашему запросу найдено",
                temp,
                String.format(
                        "Погода на данный момент: \n %s \n" +
                        "\n Температура на данный момент: %.2f", weatherCode, temp
                ),
                LocalDateTime.now(),
                null
        );
        kafkaTemplate.send("weather-response", response);
    }

    private  String getDescriptionOfWeather(String weatherCode) {
        if (weatherCode == null || weatherCode.isEmpty()) {
            return "❓ Данные о погоде отсутствуют";
        }

        return switch (weatherCode) {
            case "0" -> "☀️ Ясное небо";
            case "1" -> "🌤️ В основном ясно";
            case "2" -> "⛅ Частично облачно";
            case "3" -> "☁️ Пасмурно";
            case "45" -> "🌫️ Туман";
            case "48" -> "🌫️ Инейный туман";
            case "51" -> "🌦️ Легкая морось";
            case "53" -> "🌦️ Умеренная морось";
            case "55" -> "🌦️ Сильная морось";
            case "56" -> "🌧️❄️ Легкая ледяная морось";
            case "57" -> "🌧️❄️ Сильная ледяная морось";
            case "61" -> "🌧️ Небольшой дождь";
            case "63" -> "🌧️ Умеренный дождь";
            case "65" -> "🌧️💦 Сильный дождь";
            case "66" -> "🌧️❄️ Легкий ледяной дождь";
            case "67" -> "🌧️❄️ Сильный ледяной дождь";
            case "71" -> "❄️ Небольшой снег";
            case "73" -> "❄️ Умеренный снег";
            case "75" -> "❄️💨 Сильный снегопад";
            case "77" -> "🌨️ Снежная крупа";
            case "80" -> "🌦️ Небольшие ливни";
            case "81" -> "🌦️ Умеренные ливни";
            case "82" -> "🌦️💦 Сильные ливни";
            case "85" -> "🌨️ Небольшие снежные ливни";
            case "86" -> "🌨️💨 Сильные снежные ливни";
            case "95" -> "⛈️ Гроза";
            case "96" -> "⛈️🌨️ Гроза с небольшим градом";
            case "99" -> "⛈️🌨️💥 Гроза с сильным градом";
            default -> "Неизвестный код погоды: " + weatherCode;
        };
    }
}
