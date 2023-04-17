package com.example.bottest.service;

import com.example.bottest.model.CurrencyConversionService;
import com.example.bottest.model.CurrencyModServise;
import com.example.bottest.model.Users;
import com.example.bottest.model.enums.Constant;
import com.example.bottest.repository.UserRepository;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
@RequiredArgsConstructor
public class BotService {

    private final UserRepository userRepository;
    private final AdminServicee adminServicee;


    public Optional<Double> parseDuble(String messageText) {
        try {
            return Optional.of(Double.parseDouble(messageText));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public String getCurrencyButton(Constant saved, Constant current) {
        return saved == current ? current + "✅" : current.name();
    }

    @SneakyThrows
    public SendMessage natija(Message message, Update update) {
        String messageText = message.getText();
        Optional<Double> value = parseDuble(messageText);
        Constant originalCurrency = CurrencyModServise.getOriginalCurrency(message.getChatId());
        Constant targetCurrency = CurrencyModServise.getTargetCurrency(message.getChatId());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        double ratio = CurrencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
        adminServicee.registerStory(update, originalCurrency.name(), targetCurrency.name(), date, ratio, value.get());
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(
                        String.format(
                                "%4.2f %s   \uD83D\uDD00️ %4.2f %s",
                                value.get(), originalCurrency, (value.get() * ratio), targetCurrency))
                .build();

    }


    public SendMessage handleMessage(Message message) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Constant originalCurrency =
                CurrencyModServise.getOriginalCurrency(message.getChatId());
        Constant targetCurrency = CurrencyModServise.getTargetCurrency(message.getChatId());
        for (Constant currency : Constant.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(originalCurrency, currency))
                                    .callbackData("ORIGINAL:" + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData("TARGET:" + currency)
                                    .build()));
        }


        return SendMessage.builder()
                .text("Valyutalarni tanlab summani kiriting")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }


    public EditMessageReplyMarkup handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Constant newCurrency = Constant.valueOf(param[1]);
        switch (action) {
            case "ORIGINAL" -> CurrencyModServise.setOriginalCurrency(message.getChatId(), newCurrency);
            case "TARGET" -> CurrencyModServise.setTargetCurrency(message.getChatId(), newCurrency);
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Constant originalCurrency = CurrencyModServise.getOriginalCurrency(message.getChatId());
        Constant targetCurrency = CurrencyModServise.getTargetCurrency(message.getChatId());
        for (Constant currency : Constant.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(originalCurrency, currency))
                                    .callbackData("ORIGINAL:" + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData("TARGET:" + currency)
                                    .build()));
        }
        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }




    public SendMessage Contact(Update update, String chatId) throws IOException {
        Contact contact = update.getMessage().getContact();
        Users user = userRepository.findByChatId(chatId).get();
        user.setPhone(contact.getPhoneNumber());
        userRepository.save(user);
        return SendMessage.builder()
                .text("Hizmatlar haqida⬇️")
                .chatId(chatId)
                .build();
    }



    public SendMessage ConverterButton(Update update, String chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> listList = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("Converter");
        button.setCallbackData("converter");
        buttons.add(button);
        listList.add(buttons);
        inlineKeyboardMarkup.setKeyboard(listList);
        return SendMessage.builder()
                .text("Hizmatlar haqida⬇️")
                .chatId(chatId)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    public SendMessage daleteKeyboard(Update update, String chatId) {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        return SendMessage.builder()
                .chatId(chatId)
                .text("/help")
                .replyMarkup(replyKeyboardRemove)
                .build();
    }

    public SendMessage message(Update update, String chatId) {
        return SendMessage.builder()
                .text(update.getMessage().getText())
                .chatId(chatId)
                .build();
    }
}
