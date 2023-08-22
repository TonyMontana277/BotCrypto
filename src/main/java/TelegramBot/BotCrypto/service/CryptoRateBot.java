package TelegramBot.BotCrypto.service;

import TelegramBot.BotCrypto.config.BotConfig;
import TelegramBot.BotCrypto.model.CurrencyModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


import java.io.IOException;

@Component
@AllArgsConstructor
public class CryptoRateBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            } else {
                String cryptoName;
                if (messageText.toLowerCase().equals(messageText)) { // Check if it's all lowercase
                    cryptoName = messageText.toUpperCase(); // Convert to uppercase
                } else {
                    cryptoName = messageText; // Leave as is
                }
                try {
                    CurrencyModel currencyModel = new CurrencyModel();
                    String currency = CryptoService.getCurrencyRate(cryptoName, currencyModel);
                    sendMessage(chatId, currency);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "Enter the cryptocurrency whose official exchange rate" + "\n" +
                "you want to know in relation to USD." + "\n" +
                "For example: BTC";
        sendMessage(chatId, answer);
    }
}
