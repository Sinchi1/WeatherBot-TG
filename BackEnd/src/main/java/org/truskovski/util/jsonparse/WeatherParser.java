package org.truskovski.util.jsonparse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class WeatherParser {

    private final ObjectMapper objectMapper;

    public WeatherInfo parseWeather(String json) throws IOException {
        WeatherResponse response = objectMapper.readValue(json, WeatherResponse.class);

        WeatherInfo info = new WeatherInfo();
        info.setCurrentWeather(response.current_weather);
        info.setHourlyTimes(response.hourly.time);
        info.setHourlyTemperatures(response.hourly.temperature_2m);

        return info;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WeatherResponse {
        public CurrentWeather current_weather;
        public Hourly hourly;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CurrentWeather {
        public String time;
        public double temperature;
        public double windspeed;
        public int winddirection;
        public int weathercode;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Hourly {
        public List<String> time;
        public List<Double> temperature_2m;
    }

    @Setter
    @Getter
    public static class WeatherInfo {
        private CurrentWeather currentWeather;
        private List<String> hourlyTimes;
        private List<Double> hourlyTemperatures;
    }
}
