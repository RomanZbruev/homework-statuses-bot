package app.filatov.homeworkstatusesbot.bot.handle.texthandler.message;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "locale")
public class LocaleProperties {
    private String tag;
}
