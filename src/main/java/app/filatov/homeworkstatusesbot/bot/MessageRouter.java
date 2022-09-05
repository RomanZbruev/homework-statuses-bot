package app.filatov.homeworkstatusesbot.bot;

import app.filatov.homeworkstatusesbot.bot.handle.callbackhandler.CallBackMessageHandler;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.TextMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageRouter {
    private final TextMessageHandler textMessageHandler;
    private final CallBackMessageHandler callBackMessageHandler;

    public MessageRouter(TextMessageHandler textMessageHandler,
                         CallBackMessageHandler callBackMessageHandler) {
        this.textMessageHandler = textMessageHandler;
        this.callBackMessageHandler = callBackMessageHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        // Обработка сообщений с кнопками
        if (update.hasCallbackQuery()) {
            return callBackMessageHandler.callBackMessageProvide(update.getCallbackQuery());
        }

        Message message = update.getMessage();
        // Обработка текстовых сообщений
        if (message != null && message.hasText()) {
            return textMessageHandler.textMessageProvide(message);
        }

        return null;
    }
}
