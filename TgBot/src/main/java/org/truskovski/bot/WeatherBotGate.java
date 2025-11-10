package org.truskovski.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class WeatherBotGate extends TelegramLongPollingBot {

    private final static String history = "/history";
    private final static String getByPosition = "getByPosition";
    private final static String getByLastPosition = "getByLast";
    private final static String help = "help";

    public WeatherBotGate(@Value("${bot.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var chatId = update.getMessage().getChatId();
            switch (message.getText()) {
                case (history) -> //
                case (getByPosition) -> //
                case (getByLastPosition) -> //
                case (help) -> //
            }
        }
        else{
            return;
        }
    }

    private void SendMessage(String message, Long chatId) {
        try {
            execute(new SendMessage(chatId.toString(), message));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "";
    }
}
