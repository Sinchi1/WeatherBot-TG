package org.truskovski.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.truskovski.client.CbrClient;
import org.truskovski.service.WeatherJsonCollectorService;
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
    private final WeatherJsonCollectorService weatherJsonCollectorService;

    @KafkaListener(topics = "weather-requests")
    public void consume(WeatherRequest weatherRequest) throws IOException {
        String apiResponse = cbrClient.getWeatherForecastByRequest(weatherRequest);
        log.info("Info from request{}", cbrClient.getWeatherForecastByRequest(weatherRequest));
        JsonNode jsonNode = new ObjectMapper().readTree(apiResponse);
        double temp = jsonNode.path("hourly").path("temperature_2m").get(0).asDouble();
        String tempUnit =  jsonNode.path("hourly_units").path("temperature_2m").asText();
        String weatherCode = weatherJsonCollectorService.getDescriptionOfWeather(jsonNode.path("daily").path("weather_code").get(0).asText());
        log.info("Weather Code: {}", jsonNode.path("daily").path("weather_code").get(0).asText());

        WeatherResponse response = new WeatherResponse(
                weatherRequest.userId(),
                weatherRequest.chatId(),
                "По вашему запросу найдено",
                temp,
                String.format(
                        "Погода на данный момент: \n %s \n" +
                        "\n Температура на данный момент: %.2f %s", weatherCode, temp, tempUnit
                ),
                LocalDateTime.now(),
                null
        );
        kafkaTemplate.send("weather-response", response);
    }
}
