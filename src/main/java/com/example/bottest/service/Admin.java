package com.example.bottest.service;


import com.example.bottest.model.InlineUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Service
public class Admin {

    public SendMessage welcome(Update update, String chatId) {
        return SendMessage.builder()
                .text("Hush kelibsiz!\n quyidagi amaliyotlarni birini tanlang?")
                .chatId(chatId)
                .replyMarkup(InlineUtil.inlineKeyboardMarkup(
                        InlineUtil.listList(
                                InlineUtil.rowlist(
                                        InlineUtil.button("Userlar royhatti ", "#userslist"),
                                        InlineUtil.button("Convertatsiyalar royhati", "#converlist")
                                ),
                                InlineUtil.rowlist(
                                        InlineUtil.button("Valyutalar kursi", "#valyutalist"),
                                        InlineUtil.button("Yangiliklar jonatish", "#newssend")
                                ),
                                InlineUtil.rowlist(
                                        InlineUtil.button("Reklama jonatish", "#reklama"))))).build();
    }


    public SendDocument SendDocument(Update update, String chatId) {

        return null;
    }

    public void newsSend() throws IOException, TelegramApiException {

    }


    public SendPhoto newsReklama(Update update, String chatId) {
        return null;
    }



    public void newsReklama() {

    }



    public SendDocument ConvertatsiyaList(CallbackQuery callbackQuery) {

        return null;
    }
}
