package org.truskovski.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
public class WeatherConsumer {

    private final CbrClient cbrClient;
    private final KafkaTemplate<String, WeatherResponse> kafkaTemplate;
    private final WeatherRepository weatherRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "weather-request")
    public void consume(WeatherRequest weatherRequest) throws IOException {
        String apiResponse = cbrClient.getWeatherForecastByRequest(weatherRequest);
        JsonNode jsonNode = new ObjectMapper().readTree(apiResponse);
        double temp = jsonNode.path("hourly").path("temperature_2m").get(0).asDouble();
        String description = getWeatherDescription(jsonNode.path("hourly").path("weather_code").get(0).asInt());

        WeatherResponse response = new WeatherResponse(
                weatherRequest.userId(),
                weatherRequest.chatId(),
                null,
                temp,
                description,
                LocalDateTime.now(),
                null
        );
        kafkaTemplate.send("weather-responses", response);
    }

    private String getWeatherDescription(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1, 2, 3 -> "Mainly clear";
            default -> "Unknown";
        };
    }
}
