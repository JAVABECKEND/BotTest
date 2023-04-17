package com.example.bottest.bot;
import com.example.bottest.service.AdminServicee;
import com.example.bottest.service.BotService;
import com.example.bottest.twilio.TwilioSms;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
@RequiredArgsConstructor
public class TestBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;

    private final BotService servisBot;
    private final AdminServicee adminServicee;



    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            SendMessage sendMessage = new SendMessage();
            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().hasText()) {
                Message message = update.getMessage();
                String text = message.getText();
                log(update.getMessage().getChatId().toString(), update.getMessage().getFrom().getUserName(), text);
                switch (text) {
                    case "/start":
                        execute(adminServicee.start(update, chatId));
                        break;
                    case "/converter":
                        execute(servisBot.handleMessage(message));
                        break;
                    case "/admin":
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Sizga sms kodi boradi\nKod ni kiriting!");
                        execute(sendMessage);
                        TwilioSms.smsCode();
                        break;
                    case "history\uD83D\uDCC3":
                        break;
                    case "users\uD83D\uDCC3":
                        execute(adminServicee.SendDocument(update, chatId));
                        break;
                    case "currencies\uD83D\uDCC3":
                        execute(adminServicee.SendDocumentPDFList(update, chatId));
                        break;
                    case "Reclama":
                        break;
                    case "News":
                        adminServicee.news(update, chatId);
                        break;
                    case "0000":
                        execute(adminServicee.buttons(update, chatId));
                        break;
                    case "/help":
                        execute(adminServicee.help(chatId, update));
                    default:
                        execute(servisBot.natija(message, update));
                }


            } else if (update.getMessage().hasContact()) {
                execute(servisBot.Contact(update, chatId));
                execute(servisBot.daleteKeyboard(update, chatId));

            }


        } else if (update.hasCallbackQuery()) {
            execute(servisBot.handleCallback(update.getCallbackQuery()));
        }


    }

    public void log(String chatId, String userName, String text) {
        System.out.println(chatId + "--" + userName + " : " + text);

    }


}
