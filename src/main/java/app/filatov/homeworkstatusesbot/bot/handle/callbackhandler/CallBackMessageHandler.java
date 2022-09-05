package app.filatov.homeworkstatusesbot.bot.handle.callbackhandler;

import app.filatov.homeworkstatusesbot.exception.UserNotFoundException;
import app.filatov.homeworkstatusesbot.model.Setting;
import app.filatov.homeworkstatusesbot.model.SettingCompositeKey;
import app.filatov.homeworkstatusesbot.model.User;
import app.filatov.homeworkstatusesbot.model.repository.SettingRepository;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Optional;

@Log4j2
@Service
public class CallBackMessageHandler {
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;

    public CallBackMessageHandler(UserRepository userRepository,
                                  SettingRepository settingRepository) {
        this.userRepository = userRepository;
        this.settingRepository = settingRepository;
    }

    public BotApiMethod<?> callBackMessageProvide(CallbackQuery callbackQuery) {
        final Long userId = callbackQuery.getFrom().getId();

        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        User user = optional.get();

        return switch (callbackQuery.getData()) {
            case "SET_RUSSIAN_LANGUAGE" -> {
                settingRepository.save(Setting.builder().settingCompositeKey(SettingCompositeKey.builder()
                                .user(user)
                                .key("LANGUAGE_CODE")
                                .build())
                        .value("ru").build());
                yield sendAnswerCallbackQuery("Установлен русский язык", callbackQuery);
            }
            case "SET_ENGLISH_LANGUAGE" -> {
                settingRepository.save(Setting.builder().settingCompositeKey(SettingCompositeKey.builder()
                                .user(user)
                                .key("LANGUAGE_CODE")
                                .build())
                        .value("en").build());
                yield sendAnswerCallbackQuery("English language installed", callbackQuery);
            }
            case "ENABLED_UPDATE" -> {
                settingRepository.save(Setting.builder().settingCompositeKey(SettingCompositeKey.builder()
                                .user(user)
                                .key("UPDATED")
                                .build())
                        .value("true").build());
                yield sendAnswerCallbackQuery("Вы подписались на обновления статуса проверки ДЗ", callbackQuery);
            }
            case "DISABLE_UPDATE" -> {
                settingRepository.save(Setting.builder().settingCompositeKey(SettingCompositeKey.builder()
                                .user(user)
                                .key("UPDATED")
                                .build())
                        .value("false").build());
                yield sendAnswerCallbackQuery("Вы отписались от обновления статуса проверки ДЗ", callbackQuery);
            }
            default -> null;
        };
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(false);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}
