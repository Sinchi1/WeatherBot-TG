package org.truskovski.client;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CbrClient {

    private final OkHttpClient okHttpClient;

    @Value("weather.api")
    private String url;

    public String getWeatherForecastByCoordinates(float x, float y) throws IOException {
        var request = new Request.Builder()
                .url(url)
                .build();

        try (var response = okHttpClient.newCall(request).execute()) {
            var responseBody = response.body();
            if (responseBody == null){
                // Здесь ещё кастом эксепшен (наверное)
                return null;
            }
            return responseBody.string();
        }
        // Добавить Chechked exception с api error
         catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
