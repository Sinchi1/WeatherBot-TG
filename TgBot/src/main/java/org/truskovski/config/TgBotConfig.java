package org.truskovski.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.truskovski.bot.WeatherBotGate;

@Configuration
public class TgBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(WeatherBotGate weatherBotGate) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(weatherBotGate);
        return api;
    }
}
