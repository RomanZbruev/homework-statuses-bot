package app.filatov.homeworkstatusesbot.bot.handle.texthandler;

import app.filatov.homeworkstatusesbot.bot.handle.texthandler.state.StateRouter;
import app.filatov.homeworkstatusesbot.bot.handle.texthandler.state.UserState;
import app.filatov.homeworkstatusesbot.model.Setting;
import app.filatov.homeworkstatusesbot.model.SettingCompositeKey;
import app.filatov.homeworkstatusesbot.model.User;
import app.filatov.homeworkstatusesbot.model.repository.SettingRepository;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Log4j2
@Service
public class TextMessageHandler {
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;
    private final StateRouter stateRouter;

    public TextMessageHandler(UserRepository userRepository,
                              SettingRepository settingRepository,
                              StateRouter stateRouter) {
        this.userRepository = userRepository;
        this.settingRepository = settingRepository;
        this.stateRouter = stateRouter;
    }

    public BotApiMethod<?> textMessageProvide(Message message) {
        String text = message.getText();
        Long userId = message.getFrom().getId();
        log.info("Получено сообщение от пользователя с id={}: {}", userId, text);

        User user = userRepository.findById(userId).orElseGet(() -> {
                    User newUser = userRepository.save(User.builder()
                            .id(userId)
                            .firstName(message.getFrom().getFirstName())
                            .lastName(message.getFrom().getLastName())
                            .userName(message.getFrom().getUserName())
                            .state(UserState.REGISTRATION)
                            .build());
                    // Сохраняем по умолчанию настройки для пользователя
                    settingRepository.save(Setting.builder().settingCompositeKey(SettingCompositeKey.builder()
                                    .user(newUser)
                                    .key("UPDATED")
                                    .build())
                            .value("true").build());

                    settingRepository.save(Setting.builder().settingCompositeKey(SettingCompositeKey.builder()
                                    .user(newUser)
                                    .key("LANGUAGE_CODE")
                                    .build())
                            .value(message.getFrom().getLanguageCode()).build());

                    return newUser;
                }
        );

        UserState state = switch (text) {
            case "/start" -> UserState.REGISTRATION;
            case "/settings" -> UserState.SETTINGS;
            case "/help" -> UserState.HELP;
            default -> user.getState();
        };

        user.setState(state);
        userRepository.save(user);

        return stateRouter.process(state, message);
    }
}
