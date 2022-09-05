package app.filatov.homeworkstatusesbot.bot.handle.texthandler.message;

import app.filatov.homeworkstatusesbot.bot.handle.texthandler.state.UserState;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.util.HandlerUtil;
import app.filatov.homeworkstatusesbot.exception.UserNotFoundException;
import app.filatov.homeworkstatusesbot.model.User;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import app.filatov.homeworkstatusesbot.service.loader.LoaderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Component
public class RegistrationHandler implements MessageHandler {
    private final HandlerUtil util;

    private final UserRepository userRepository;

    private final LoaderService loaderService;

    public RegistrationHandler(HandlerUtil util,
                               UserRepository userRepository,
                               LoaderService loaderService) {
        this.util = util;
        this.userRepository = userRepository;
        this.loaderService = loaderService;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        Optional<User> optional = userRepository.findById(message.getFrom().getId());
        if (optional.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        User user = optional.get();

        if (util.hasApiKey(user)) {
            util.setCorrectStateForUser(user);
            return new SendMessage(String.valueOf(chatId), "Токен уже был зарегистрирован");
        }

        if (user.getState() == UserState.REGISTRATION) {
            user.setState(UserState.ASK_API_KEY);
            userRepository.save(user);
        }

        if (user.getState() == UserState.ASK_API_KEY) {
            user.setState(UserState.CHECK_API_KEY);
            userRepository.save(user);
            return new SendMessage(String.valueOf(chatId), "Введите токен");
        }

        if (user.getState() == UserState.CHECK_API_KEY) {
            user.setApiKey(message.getText());
            try {
                loaderService.getHomeworks(user);
            } catch (Exception e) {
                user.setState(UserState.ASK_API_KEY);
                return new SendMessage(String.valueOf(chatId), "Некорректный токен, введите токен повторно");
            }
            user.setState(UserState.READY);
            userRepository.save(user);
        }

        return new SendMessage(String.valueOf(chatId), "Токен зарегистрирован");
    }

    @Override
    public UserState getHandlerType() {
        return UserState.REGISTRATION;
    }
}
