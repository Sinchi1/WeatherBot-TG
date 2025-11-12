package org.truskovski.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.truskovski.kafka.WeatherProducer;
import org.truskovski.store.dto.WeatherRequest;

@Component
@Slf4j
public class WeatherBotGate extends TelegramLongPollingBot {

    @Autowired
    private WeatherProducer weatherProducer;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.name:WeatherBot}")
    private String botName;

    public WeatherBotGate() {
        super();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        Message message = update.getMessage();
        Long chatId = message.getChatId();

        if (message.hasText()) {
            String text = message.getText().trim();
            if ("/start".equalsIgnoreCase(text) || text.startsWith("/start ")) {
                sendMessage(chatId, "Привет! Отправь свою геолокацию (📎 -> Location), и я пришлю прогноз.");
                return;
            }
            sendMessage(chatId, "Если хотите прогноз погоды — пришлите геолокацию (Location).");
            return;
        }

        if (message.hasLocation()) {
            Location location = message.getLocation();
            Long userId = message.getFrom() != null ? message.getFrom().getId().longValue() : null;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            WeatherRequest request = new WeatherRequest(userId, chatId, latitude, longitude);
            weatherProducer.sendWeatherRequest(request);

            sendMessage(chatId, "Запрос отправлен — получу прогноз и перешлю вам ответ.");
            return;
        }

        sendMessage(chatId, "Я понимаю текст и геолокацию. Отправьте геолокацию для прогноза.");
    }

    public void sendMessage(Long chatId, String message) {
        try {
            execute(new SendMessage(chatId.toString(), message));
        } catch (TelegramApiException e) {
            log.error("Failed to send message to {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
