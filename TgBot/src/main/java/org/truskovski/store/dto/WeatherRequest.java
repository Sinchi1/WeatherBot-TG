package org.truskovski.store.dto;

import java.io.Serializable;

public record WeatherRequest(
        Long userId,
        Long chatId,
        double latitude,
        double longitude
) implements Serializable {}