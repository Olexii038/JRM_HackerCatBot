package ua.javarush;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ua.javarush.TelegramBotContent.*;
import static ua.javarush.TelegramBotUtils.*;

public class MyFirstTelegramBot extends TelegramLongPollingBot {

    private final String BUTTON1 = "step1Button";
    private final String BUTTON2 = "step2Button";
    private final String BUTTON3 = "step3Button";
    private final String BUTTON4 = "step4Button";
    private final String BUTTON5 = "step5Button";
    private final String BUTTON6 = "step6Button";
    private final String BUTTON7 = "step7Button";
    private final String BUTTON8 = "step8Button";
    private List<Step> steps = createData();

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

        String trigger = null;

        if (update.hasMessage()) trigger = update.getMessage().getText();
        else if (update.hasCallbackQuery()) trigger = update.getCallbackQuery().getData();

        if (trigger == null) return;


        for (Step step: steps) {
            if (trigger.equals(step.getTrigger()) && getGlories(chatId) == step.currentGlories) {
                sendMessage(chatId, step);
                addGlories(chatId, step.getGlories());
                break;
            }
        }

    }

    private void sendMessage(Long chatId, Step step) {
        if (step.pictureEnabled) {
            SendPhoto photo = createPhotoMessage(chatId, step.getPicture());
            executeAsync(photo);
        }

        SendMessage message;
        if (step.buttonEnabled) message = createMessage(chatId, step.getText(), step.getButtons());
        else message = createMessage(chatId, step.getText());

        sendApiMethodAsync(message);
    }

    private List<Step> createData () {
        List<Step> steps = new ArrayList<>();

        steps.add(new Step(STEP_1_TEXT, "/start", 0, 20, "step_1_pic", Map.of("Злам холодильника", BUTTON1)));
        steps.add(new Step(STEP_2_TEXT, BUTTON1, 20 , 20, "step_2_pic", Map.of("Взяти сосиску! + 20 слави", BUTTON2,
                "Взяти рибку! + 20 слави", BUTTON2,
                "Скинути банку з огірками! + 20 слави", BUTTON2)));
        steps.add(new Step(STEP_3_TEXT, BUTTON2, 40, 30, "step_3_pic",Map.of("Злам робота-пилососа", BUTTON3)));
        steps.add(new Step(STEP_4_TEXT, BUTTON3, 70, 30, "step_4_pic",Map.of("Відправити робопилосос за їжею! +30 слави", BUTTON4,
                "Проїхатися на робопилососі! +30 слави", BUTTON4,
                "Тікати від робопилососа! +30 слави", BUTTON4)));
        steps.add(new Step(STEP_5_TEXT, BUTTON4, 100, 40, "step_5_pic",Map.of("Одягнути та ввімкнути GoPro!", BUTTON5)));
        steps.add(new Step(STEP_6_TEXT, BUTTON5, 140, 40, "step_6_pic",Map.of("Бігати дахами, знімати на GoPro! +40 слави", BUTTON6,
                "З GoPro нападати на інших котів із засідки! +40 слави", BUTTON6,
                "З GoPro нападати на собак із засідки! +40 слави", BUTTON6)));
        steps.add(new Step(STEP_7_TEXT, BUTTON6, 180, 50, "step_7_pic",Map.of("Злам пароля", BUTTON7)));
        steps.add(new Step(STEP_8_TEXT, BUTTON7, 230, 50, "step_8_pic",Map.of("Вийти на подвір'я", BUTTON8)));
        steps.add(new Step(FINAL_TEXT, BUTTON8, 280, -280, "final_pic"));

        return steps;
    }

    private final class Step {
        private String text;
        private boolean buttonEnabled = false;
        private Map<String, String> buttons;
        private String trigger;
        private int glories;
        private int currentGlories;
        private String picture;
        private boolean pictureEnabled = false;

        public Step (String text, String trigger, int currentGlories, int glories) {
            init(text, trigger, currentGlories, glories);
        }

        public Step (String text, String trigger, int currentGlories, int glories, String picture) {
            init(text, trigger, currentGlories, glories);
            setPicture(picture);
        }

        public Step (String text, String trigger, int currentGlories, int glories, Map<String, String> buttons) {
            init(text, trigger, currentGlories, glories);
            setButtons(buttons);
        }

        public Step (String text, String trigger, int currentGlories, int glories, String picture, Map<String, String> buttons) {
            init(text, trigger, currentGlories, glories);
            setPicture(picture);
            setButtons(buttons);
        }

        private void init(String text, String trigger, int currentGlories, int glories) {
            this.text = text;
            this.trigger = trigger;
            this.currentGlories = currentGlories;
            this.glories = glories;
        }
        private void setPicture(String picture) {
            this.picture = picture;
            pictureEnabled = true;
        }
        private void setButtons(Map<String, String> buttons) {
            this.buttons = buttons;
            buttonEnabled = true;
        }

        public String getText() {
            return text;
        }

        public boolean isButtonEnabled() {
            return buttonEnabled;
        }

        public Map<String, String> getButtons() {
            return buttons;
        }

        public String getTrigger() {
            return trigger;
        }

        public int getGlories() {
            return glories;
        }

        public int getCurrentGlories() {
            return currentGlories;
        }

        public String getPicture() {
            return picture;
        }

        public boolean isPictureEnabled() {
            return pictureEnabled;
        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new MyFirstTelegramBot());
    }
}