package org.truskovski.controller;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.truskovski.client.CbrClient;
import org.truskovski.store.dto.ApiStatus;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather/")
public class WeatherController {

    private final CbrClient cbrClient;

    @GetMapping
    public ResponseEntity<ApiStatus> getWeather(@RequestParam Double latitude, @RequestParam Double longitude) throws IOException {
        String apiAnswer = "";
        try {
            apiAnswer =  cbrClient.getWeatherForecastByCoordinates(latitude, longitude);
        } catch (IOException e) {
            throw new HttpResponseException(HttpStatus.I_AM_A_TEAPOT.value(), "Error occurred while getting the results of operation!");
        }
        return ResponseEntity.ok().body(new ApiStatus(apiAnswer));
    }
}
