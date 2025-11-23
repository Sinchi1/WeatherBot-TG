package org.truskovski.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherJsonCollectorService {

    public  String getDescriptionOfWeather(String weatherCode) {
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
