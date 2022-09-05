package app.filatov.homeworkstatusesbot.bot.handle.texthandler.message;

import app.filatov.homeworkstatusesbot.bot.handle.callbackhandler.KeyboardSupplier;
import app.filatov.homeworkstatusesbot.bot.handle.language.LanguageSupplier;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.state.UserState;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.util.HandlerUtil;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpHandler implements MessageHandler {

    private final HandlerUtil util;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final LanguageSupplier languageSupplier;
    private final KeyboardSupplier keyboardSupplier;

    public HelpHandler(HandlerUtil util,
                       UserRepository userRepository,
                       MessageService messageService,
                       LanguageSupplier languageSupplier,
                       KeyboardSupplier keyboardSupplier) {
        this.util = util;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.languageSupplier = languageSupplier;
        this.keyboardSupplier = keyboardSupplier;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        userRepository.findById(message.getFrom().getId()).ifPresent(util::setCorrectStateForUser);

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),
                messageService.getMessage("message.help", languageSupplier.getLanguage(message)));
        sendMessage.setReplyMarkup(keyboardSupplier.getApiKeyLinkButtonMarkup());

        return sendMessage;
    }

    @Override
    public UserState getHandlerType() {
        return UserState.HELP;
    }
}
