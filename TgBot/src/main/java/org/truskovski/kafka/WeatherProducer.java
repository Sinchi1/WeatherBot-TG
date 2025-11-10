package org.truskovski.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.truskovski.store.dto.WeatherRequest;

@Component
@RequiredArgsConstructor
public class WeatherProducer {

    private final KafkaTemplate<String, WeatherRequest> kafkaTemplate;

    public void sendWeatherRequest(WeatherRequest weatherRequest) {
        kafkaTemplate.send("weather-requests", weatherRequest);
    }
}
