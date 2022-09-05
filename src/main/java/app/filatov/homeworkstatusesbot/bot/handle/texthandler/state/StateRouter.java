package app.filatov.homeworkstatusesbot.bot.handle.texthandler.state;

import app.filatov.homeworkstatusesbot.bot.handle.texthandler.message.MessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StateRouter {

    private final Map<UserState, MessageHandler> handlers = new HashMap<>();

    public StateRouter(List<MessageHandler> handlers) {
        handlers.forEach(handler -> this.handlers.put(handler.getHandlerType(), handler));
    }

    public BotApiMethod<?> process(UserState state, Message message) {
        MessageHandler messageHandler = handlers.get(getHandlerFamily(state));
        return messageHandler.handle(message);
    }

    private UserState getHandlerFamily(UserState state) {
        return switch (state) {
            case REGISTRATION, ASK_API_KEY, CHECK_API_KEY -> UserState.REGISTRATION;
            case READY -> UserState.READY;
            case SETTINGS -> UserState.SETTINGS;
            case HELP -> UserState.HELP;
            case ERROR -> UserState.ERROR;
        };
    }
}
