package app.filatov.homeworkstatusesbot.bot.handle.texthandler.message;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;

@Service
public class MessageService {
    private final MessageSource messageSource;
    private final Locale locale;

    public MessageService(MessageSource messageSource, LocaleProperties localeProperties) {
        this.messageSource = messageSource;
        this.locale = Locale.forLanguageTag(localeProperties.getTag());
    }

    public String getMessage(String message, String languageCode) {
        if (Arrays.stream(Locale.getAvailableLocales())
                .map(Locale::toLanguageTag)
                .toList().contains(languageCode)) {
            String sourceMessage = messageSource.getMessage(message, null, new Locale.Builder()
                    .setLanguageTag(languageCode)
                    .build());
            return sourceMessage;
        } else return messageSource.getMessage(message, null, locale);
    }
}
