package app.filatov.homeworkstatusesbot.bot.handle.texthandler.message;

import app.filatov.homeworkstatusesbot.bot.handle.language.LanguageSupplier;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.state.UserState;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.util.HandlerUtil;
import app.filatov.homeworkstatusesbot.exception.UserNotFoundException;
import app.filatov.homeworkstatusesbot.model.User;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Component
public class ErrorHandler implements MessageHandler {

    private final HandlerUtil util;

    private final UserRepository userRepository;

    private final MessageService messageService;

    private final LanguageSupplier languageSupplier;

    public ErrorHandler(HandlerUtil util, UserRepository userRepository,
                        MessageService messageService, LanguageSupplier languageSupplier) {
        this.util = util;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.languageSupplier = languageSupplier;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        Optional<User> optional = userRepository.findById(message.getFrom().getId());
        if (optional.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        User user = optional.get();
        user.setApiKey(null);
        util.setCorrectStateForUser(user);
        userRepository.save(user);
        return new SendMessage(String.valueOf(chatId),
                messageService.getMessage("message.error", languageSupplier.getLanguage(message)));
    }

    @Override
    public UserState getHandlerType() {
        return UserState.ERROR;
    }
}
