package ua.javarush;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.javarush.TelegramBotContent.*;
import static ua.javarush.TelegramBotUtils.*;

public class MyFirstTelegramBot extends TelegramLongPollingBot {
    private Map<Long, Data> data = new HashMap<>();

    private final String BUTTON1 = "step1Button";
    private final String BUTTON2 = "tep2Button";
    private final String BUTTON3 = "step3Button";
    private final String BUTTON4 = "step4Button";
    private final String BUTTON5 = "step5Button";
    private final String BUTTON6 = "step6Button";
    private final String BUTTON7 = "step7Button";
    private final String BUTTON8 = "step8Button";

    @Override
    public String getBotUsername() {
        return "hacker_cat_038_bot";
    }

    @Override
    public String getBotToken() {
        return "6367098711:AAFKYTesgCmQjNRDWLLZ55LoQ6voluLLS1c";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);

        if (update.hasMessage()) {
            if (isTextEqual(update, "/start")) {
                sendMessage(chatId, STEP_1_TEXT, Map.of("Злам холодильника", BUTTON1));
            }
        }

        if (update.hasCallbackQuery()) {
            if (isButtonPressed(update, BUTTON1) && getGlories(chatId) == 0) {
                sendMessageUpdateGlories(chatId, STEP_2_TEXT, Map.of("Взяти сосиску! + 20 слави", BUTTON2,
                        "Взяти рибку! + 20 слави", BUTTON2,
                        "Скинути банку з огірками! + 20 слави", BUTTON2), 20);
            }

            if (isButtonPressed(update, BUTTON2) && getGlories(chatId) == 20) {
                sendMessageUpdateGlories(chatId, STEP_3_TEXT, Map.of("Злам робота-пилососа", BUTTON3), 20);
            }

            if (isButtonPressed(update, BUTTON3) && getGlories(chatId) == 40) {
                sendMessageUpdateGlories(chatId, STEP_4_TEXT, Map.of("Відправити робопилосос за їжею! +30 слави", BUTTON4,
                        "Проїхатися на робопилососі! +30 слави", BUTTON4,
                        "Тікати від робопилососа! +30 слави", BUTTON4), 30);
            }

            if (isButtonPressed(update, BUTTON4) && getGlories(chatId) == 70) {
                sendMessageUpdateGlories(chatId, STEP_5_TEXT, Map.of("Одягнути та ввімкнути GoPro!", BUTTON5), 30);
            }

            if (isButtonPressed(update, BUTTON5) && getGlories(chatId) == 100) {
                sendMessageUpdateGlories(chatId, STEP_6_TEXT, Map.of("Бігати дахами, знімати на GoPro! +40 слави", BUTTON6,
                        "З GoPro нападати на інших котів із засідки! +40 слави", BUTTON6,
                        "З GoPro нападати на собак із засідки! +40 слави", BUTTON6), 40);
            }

            if (isButtonPressed(update, BUTTON6) && getGlories(chatId) == 140) {
                sendMessageUpdateGlories(chatId, STEP_7_TEXT, Map.of("Злам пароля", BUTTON7), 40);
            }

            if (isButtonPressed(update, BUTTON7) && getGlories(chatId) == 180) {
                sendMessageUpdateGlories(chatId, STEP_8_TEXT, Map.of("Вийти на подвір'я", BUTTON8), 50);
            }

            if (isButtonPressed(update, BUTTON8) && getGlories(chatId) == 230) {
                sendMessage(chatId, FINAL_TEXT, null);
            }
        }

        /*
        if (chatId == null) return;

        Data userData = data.get(chatId);
        if (userData == null) {
            userData = new Data(chatId);
            data.put(chatId, userData);
        }*/

    }

    private boolean isTextEqual(Update update, String text) {
        return update.getMessage().getText().equals(text);
    }

    private boolean isButtonPressed(Update update, String buttonId) {
        return update.getCallbackQuery().getData().equals(buttonId);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new MyFirstTelegramBot());
    }

    private void sendMessageUpdateGlories(Long chatId, String text, Map<String, String> buttons, int glories) {
        addGlories(chatId, glories);
        sendMessage(chatId, text, buttons);
    }

    private void sendMessage(Long chatId, String text, Map<String, String> buttons) {
        SendMessage message = buttons == null ? createMessage(chatId, text) : createMessage(chatId, text, buttons);
        sendApiMethodAsync(message);
    }

    private String getFormattedName(String name) {
        char c = name.charAt(0);
        c = Character.toUpperCase(c);
        name = name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(c));
        return name;
    }

    private final class Data {
        @NonNull
        private Long chatId;
        private boolean question = false;
        private String name = null;

        public Data(Long chatId) {
            this.chatId = chatId;
        }
    }
}