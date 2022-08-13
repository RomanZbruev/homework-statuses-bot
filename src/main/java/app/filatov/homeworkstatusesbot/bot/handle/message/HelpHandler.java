package app.filatov.homeworkstatusesbot.bot.handle.message;

import app.filatov.homeworkstatusesbot.bot.handle.state.BotState;
import app.filatov.homeworkstatusesbot.bot.handle.util.HandlerUtil;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpHandler implements MessageHandler {

    private final HandlerUtil util;
    private final UserRepository userRepository;


    public HelpHandler(HandlerUtil util, UserRepository userRepository) {
        this.util = util;
        this.userRepository = userRepository;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        userRepository.findById(message.getFrom().getId()).ifPresent(util::setCorrectStateForUser);

        return new SendMessage(String.valueOf(chatId), "Помощь");
    }

    @Override
    public BotState getHandlerType() {
        return BotState.HELP;
    }
}
