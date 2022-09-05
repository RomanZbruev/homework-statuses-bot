package app.filatov.homeworkstatusesbot.bot.handle.callbackhandler;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
@Configuration
@ConfigurationProperties(prefix = "api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyboardSupplier {
    String url;

    public InlineKeyboardMarkup getApiKeyLinkButtonMarkup() {
        // Клавиатура с кнопкой-ссылкой на получение API-KEY
        InlineKeyboardMarkup settingsKeyboardMarkup = new InlineKeyboardMarkup();
        // Первый ряд кнопок
        List<InlineKeyboardButton> keyboard = new ArrayList<>();

        // Кнопка-ссылка
        InlineKeyboardButton apiKeyLinkButton = new InlineKeyboardButton();
        apiKeyLinkButton.setText("Получить API-KEY");
        apiKeyLinkButton.setCallbackData("GET_API_KEY");
        apiKeyLinkButton.setUrl(url);

        // Добавляем кнопку на клавиатуру
        keyboard.add(apiKeyLinkButton);
        settingsKeyboardMarkup.setKeyboard(List.of(keyboard));

        return settingsKeyboardMarkup;
    }

    public InlineKeyboardMarkup getSettingsButtonsMarkup() {
        // Клавиатура настройки языка и подписки на обновления статуса проверки ДЗ
        InlineKeyboardMarkup settingsKeyboardMarkup = new InlineKeyboardMarkup();

        // Первый ряд кнопок
        List<InlineKeyboardButton> keyboardButtonsFirstRow = new ArrayList<>();
        // Второй ряд кнопок
        List<InlineKeyboardButton> keyboardButtonsSecondRow = new ArrayList<>();

        // Установка английского языка для бота
        InlineKeyboardButton englishLanguageButton = new InlineKeyboardButton();
        englishLanguageButton.setText("Русский");
        englishLanguageButton.setCallbackData("SET_RUSSIAN_LANGUAGE");

        // Установка русского языка для бота
        InlineKeyboardButton russianLanguageButton = new InlineKeyboardButton();
        russianLanguageButton.setText("English");
        russianLanguageButton.setCallbackData("SET_ENGLISH_LANGUAGE");

        // Добавляем кнопки настройки на первый ряд клавиатуры
        keyboardButtonsFirstRow.add(englishLanguageButton);
        keyboardButtonsFirstRow.add(russianLanguageButton);

        // Подписка на обновления
        InlineKeyboardButton enabledUpdateButton = new InlineKeyboardButton();
        enabledUpdateButton.setText("Подписаться");
        enabledUpdateButton.setCallbackData("ENABLED_UPDATE");

        // Отписка от обновлений
        InlineKeyboardButton disableUpdateButton = new InlineKeyboardButton();
        disableUpdateButton.setText("Отписаться");
        disableUpdateButton.setCallbackData("DISABLE_UPDATE");

        // Добавляем кнопки настройки на второй ряд клавиатуры
        keyboardButtonsSecondRow.add(enabledUpdateButton);
        keyboardButtonsSecondRow.add(disableUpdateButton);

        // Добавляем ряды кнопок на клавиатуру
        settingsKeyboardMarkup.setKeyboard(List.of(keyboardButtonsFirstRow, keyboardButtonsSecondRow));

        return settingsKeyboardMarkup;
    }
}
