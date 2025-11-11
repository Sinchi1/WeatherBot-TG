package org.truskovski.client;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.truskovski.store.dto.WeatherRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CbrClient {

    private final OkHttpClient okHttpClient;

    @Value("${weather.url}")
    private String baseUrl;

    public String getWeatherForecastByRequest(WeatherRequest userRequest) throws IOException {
        var url = baseUrl + "?latitude=" + userRequest.latitude() + "&longitude=" + userRequest.longitude();
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

    public String getWeatherForecastByCoordinates(Double latitude, Double longitude) throws IOException {
        var url = baseUrl + "?latitude=" + latitude + "&longitude=" + longitude;
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


}
