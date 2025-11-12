package org.truskovski.store.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record WeatherRequest(
        Long userId,
        Long chatId,
        double latitude,
        double longitude
) implements Serializable {}