package org.truskovski.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.truskovski.store.dto.WeatherRequest;
import org.truskovski.util.jsonparse.WeatherParser;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CbrClient {

    private final OkHttpClient okHttpClient;
    private final WeatherParser weatherParser;

    @Value("${weather.url}")
    private String baseUrl;

    public String getWeatherForecastByRequest(WeatherRequest userRequest) throws IOException {
        var url = baseUrl + "?latitude=" + userRequest.latitude()
                + "&longitude=" + userRequest.longitude()
                + "&hourly=temperature_2m"
                + "&daily=weather_code"
                + "&current_weather=true"
                + "&timezone=Europe/Moscow";

        var request = new Request.Builder()
                .url(url)
                .build();

        try (var response = okHttpClient.newCall(request).execute()) {
            var responseBody = response.body();
            if (responseBody == null || !response.isSuccessful()){
                throw new  IOException("Api Error " + response.code());
            }
            return responseBody.string();
        }
    }

    public WeatherParser.WeatherInfo getWeatherForecastByCoordinates(Double latitude, Double longitude) throws IOException {
        var url = baseUrl + "?latitude=" + latitude
                + "&longitude=" + longitude
                + "&hourly=temperature_2m"
                + "&current_weather=true"
                + "&timezone=Europe/Moscow";

        var request = new Request.Builder()
                .url(url)
                .build();

        log.info(request.toString());
        try (var response = okHttpClient.newCall(request).execute()) {
            var responseBody = response.body();
            if (responseBody == null || !response.isSuccessful()){
                throw new  IOException("Api Error " + response.code());
            }
            return weatherParser.parseWeather(responseBody.string());
        }
    }


}
