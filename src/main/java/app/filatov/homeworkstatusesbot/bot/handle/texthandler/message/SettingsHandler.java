package app.filatov.homeworkstatusesbot.bot.handle.texthandler.message;

import app.filatov.homeworkstatusesbot.bot.handle.callbackhandler.KeyboardSupplier;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.state.UserState;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.util.HandlerUtil;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SettingsHandler implements MessageHandler {
    private final HandlerUtil util;
    private final UserRepository userRepository;
    private final KeyboardSupplier keyboardSupplier;

    public SettingsHandler(HandlerUtil util,
                           UserRepository userRepository,
                           KeyboardSupplier keyboardSupplier) {
        this.util = util;
        this.userRepository = userRepository;
        this.keyboardSupplier = keyboardSupplier;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        userRepository.findById(message.getFrom().getId()).ifPresent(util::setCorrectStateForUser);

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Настройки");
        sendMessage.setReplyMarkup(keyboardSupplier.getSettingsButtonsMarkup());
        return sendMessage;
    }

    @Override
    public UserState getHandlerType() {
        return UserState.SETTINGS;
    }
}
