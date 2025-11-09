package org.truskovski.store.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record WeatherResponse(
        Long userId,
        Long chatId,
        String locationName,
        double temperature,
        String description,
        LocalDateTime forecastTime,
        String errorMessage
) implements Serializable {}
