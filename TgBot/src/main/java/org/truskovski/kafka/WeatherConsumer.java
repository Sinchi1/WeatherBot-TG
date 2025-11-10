package org.truskovski.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.truskovski.bot.WeatherBotGate;
import org.truskovski.store.dto.WeatherResponse;

@Component
@RequiredArgsConstructor
public class WeatherConsumer {

    private final WeatherBotGate  weatherBotGate;

    @KafkaListener(topics = "weather-response")
    public void consume(WeatherResponse weatherResponse) {
        if (weatherResponse.errorMessage() == null){
            try {
                weatherBotGate.execute(new SendMessage(weatherResponse.chatId().toString(), weatherResponse.description()));
            }
            catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                weatherBotGate.execute(new SendMessage(weatherResponse.chatId().toString(),
                        "Error occurred" + weatherResponse.errorMessage()));
            }
            catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
