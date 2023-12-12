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
        if (chatId == null) return;

        Data userData = data.get(chatId);
        if (userData == null) {
            userData = new Data(chatId);
            data.put(chatId, userData);
        }

        if (update.hasMessage()) {
            Message inputMessage = update.getMessage();
            String inputText = inputMessage.getText();

            String text = "";

            if (userData.name == null) {
                if (inputText.equals("/start")) {
                    text = "Привіт, як тебе звати?";
                    userData.question = true;
                } else if (userData.question) {
                    String name;

                    if (inputText.toLowerCase().contains("мене звуть")) {
                        name = inputText.toLowerCase().replace("мене звуть", "").strip();
                    } else if (inputText.toLowerCase().contains("мене звати")) {
                        name = inputText.toLowerCase().replace("мене звати", "").strip();
                    } else if (inputText.toLowerCase().contains("моє ім'я")) {
                        name = inputText.toLowerCase().replace("моє ім'я", "").strip();
                    } else return;

                    name = getFormattedName(name);

                    userData.name = name;
                    userData.question = false;

                    text = "Радий знайомству, " + name + ", я - *Кіт*";
                }
            } else {
                if (inputText.equals("/forget_me")) {
                    data.remove(chatId);
                    text = "Done";
                } else {
                    text = "Hi, " + userData.name;
                }
            }

            if (!text.equals("")) {
                SendMessage message = createMessage(chatId, text);

                sendApiMethodAsync(message);
            }
        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new MyFirstTelegramBot());
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