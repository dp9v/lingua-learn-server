package com.dp9v.lingua_learn_server.bot.handler;

import com.dp9v.libretranslate.LibreTranslateClient;
import com.dp9v.libretranslate.dto.DetectResponse;
import com.dp9v.libretranslate.dto.LanguageCode;
import com.dp9v.libretranslate.dto.TranslateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranslateHandler implements UpdateHandler {

    private static final Set<LanguageCode> SUPPORTED_SOURCES = Set.of(LanguageCode.ENGLISH, LanguageCode.CZECH);

    private final LibreTranslateClient libreTranslateClient;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && !update.getMessage().getText().startsWith("/");
    }

    @Override
    public void handle(AbsSender sender, Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var text = update.getMessage().getText().trim();

        try {
            List<DetectResponse> detections = libreTranslateClient.detect(text);
            if (detections == null || detections.isEmpty()) {
                sendReply(sender, chatId, "❌ Не удалось определить язык.");
                return;
            }

            LanguageCode detected = detections.getFirst().language();
            if (!SUPPORTED_SOURCES.contains(detected)) {
                sendReply(sender, chatId, "⚠️ Поддерживается перевод только с английского и чешского.");
                return;
            }

            TranslateResponse response = libreTranslateClient.translate(text, detected, LanguageCode.RUSSIAN);
            String reply = "\uD83D\uDD24 *%s* → *%s*\n\n%s".formatted(
                    detected.getCode().toUpperCase(),
                    LanguageCode.RUSSIAN.getCode().toUpperCase(),
                    response.translatedText()
            );

            sendReply(sender, chatId, reply);
        } catch (Exception e) {
            log.error("Translation failed for chatId={}", chatId, e);
            sendReply(sender, chatId, "❌ Ошибка при переводе: " + e.getMessage());
        }
    }

    private void sendReply(AbsSender sender, String chatId, String text) {
        try {
            sender.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode("Markdown")
                    .build());
        } catch (TelegramApiException e) {
            log.error("Failed to send reply to chatId={}", chatId, e);
        }
    }
}

