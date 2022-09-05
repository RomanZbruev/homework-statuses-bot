package app.filatov.homeworkstatusesbot.bot.handle.language;

import app.filatov.homeworkstatusesbot.exception.UserNotFoundException;
import app.filatov.homeworkstatusesbot.model.Setting;
import app.filatov.homeworkstatusesbot.model.SettingCompositeKey;
import app.filatov.homeworkstatusesbot.model.User;
import app.filatov.homeworkstatusesbot.model.repository.SettingRepository;
import app.filatov.homeworkstatusesbot.model.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Component
@Primary
public class DBLanguageSupplier implements LanguageSupplier {
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;
    private final LanguageConverter languageConverter;

    public DBLanguageSupplier(UserRepository userRepository,
                              SettingRepository settingRepository,
                              LanguageConverter languageConverter) {
        this.userRepository = userRepository;
        this.settingRepository = settingRepository;
        this.languageConverter = languageConverter;
    }

    @Override
    public String getLanguage(Message message) {
        Long id = message.getChatId();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Optional<Setting> optional = settingRepository.findById(SettingCompositeKey.builder()
                    .user(user.get()).key("LANGUAGE_CODE").build());

            return optional.isPresent() ?
                    languageConverter.getLanguageCode(optional.get().getValue()) :
                    languageConverter.getDefaultLanguageCode();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
