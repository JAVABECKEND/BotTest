package com.example.bottest.service;

import com.example.bottest.model.Story;
import com.example.bottest.model.Users;
import com.example.bottest.model.enums.State;
import com.example.bottest.repository.StoryRepository;
import com.example.bottest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AdminServicee {


    private final UserRepository userRepository;
    private final StoryRepository storyRepository;


    public SendMessage deleteMessages(String chatId){
        return SendMessage.builder().text("Delete").chatId(chatId).build();
    }

    public SendDocument SendDocument(Update update, String chatId) {

        return null;
    }
    public static SendDocument SendDocumentStory(Update update, String chatId) {
        return null;
    }


    public SendDocument SendDocumentPDFList(Update update, String chatId) {

        return null;
    }

    public SendMessage help(Update update, String chatId) {
        return SendMessage.builder()
                .text("/start orqali botni ishga tushurasiz\n/admin orqali siz admin bo'lishingiz mumkin")
                .chatId(chatId)
                .build();
    }

    public SendMessage buttons(Update update, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Assalomu Alaykum Admin \uD83D\uDC4B" + update.getMessage().getFrom().getFirstName());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        KeyboardButton button1 = new KeyboardButton("history\uD83D\uDCC3");
        KeyboardButton button2 = new KeyboardButton("users\uD83D\uDCC3");
        KeyboardButton button3 = new KeyboardButton("currencies\uD83D\uDCC3");
        KeyboardButton button4 = new KeyboardButton("News");
        KeyboardButton button5 = new KeyboardButton("Reclama");

        row2.add(button2);
        row2.add(button3);

        row1.add(button1);
        row1.add(button4);
        row1.add(button5);

        rows.add(row1);
        rows.add(row2);
        replyKeyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public void registerStory(Update update, String orginal, String target, String date, Double rateTo, Double rateFrom) {
        Optional<Users> byChatId = userRepository.findByChatId(String.valueOf(update.getMessage().getChatId()));
        Users user = byChatId.get();
        Story story=new Story();
        story.setDate(date);
        story.setOriginal(orginal);
        story.setTarget(target);
        story.setValuefrom(rateFrom);
        story.setValueto(rateTo);
        story.setUserName(user.getUserName());
//        Long id = user.getId();
//        Long id1 = story.getId();
        storyRepository.save(story);
    }




    public SendPhoto sendReclama(Update update, String chatId) {

        return null;
    }


    @SneakyThrows
    public void news(Update update, String chatId) {
        StringBuilder builder = new StringBuilder();
        Connection connection = Jsoup.connect("https://openbudget.uz/boards/6/123514/");
        Document document = connection.get();
        Elements dataList = document.getElementsByClass("initiatives-side__vote__info");

        Elements titleList = document.getElementsByClass("small-news__title");
        for (Element element : dataList) {
            for (Element element1 : element.getElementsByTag("a")) {
                builder.append("\uD83D\uDCA5"+element1.text() + "\n\n");
            }
        }
    }



    public SendMessage help(String chatId, Update update) {
        return SendMessage.builder()
                .text("/start orqali botni ishga tushurasiz\n\n/converter orqali valyutani hisoblashingiz mumkin")
                .chatId(chatId)
                .build();

    }

    public SendMessage start(Update update, String chatId) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestContact(true);
        button.setText("Raqamni yuborish");
        row.add(button);
        rowList.add(row);
        registerUser(update, chatId);
        replyKeyboardMarkup.setKeyboard(rowList);
        String text;
        if (update.getMessage().getFrom().getFirstName() != null) {
            text = update.getMessage().getFrom().getFirstName();
        } else {
            text = "";
        }

        return SendMessage.builder()
                .text("Xush kelibsiz! " + text + " Registratsiyadan o'ting!")
                .replyMarkup(replyKeyboardMarkup)
                .chatId(String.valueOf(update.getMessage().getChatId()))
                .build();
    }

    public void registerUser(Update update, String chatId) {
//
        Optional<Users> byChatId = userRepository.findByChatId(chatId);
        if (!byChatId.isPresent()) {

            Users user=new Users();
            user.setChatId(chatId);
            user.setFirstName(update.getMessage().getFrom().getFirstName());
            user.setUserName(update.getMessage().getFrom().getUserName());
            user.setLastName(update.getMessage().getFrom().getLastName());
            user.setState(State.START);
            userRepository.save(user);

        }
    }
}
