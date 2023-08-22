package TelegramBot.BotCrypto.service;

import TelegramBot.BotCrypto.model.CurrencyModel;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CryptoService {
    private static final String API_KEY = "0dc14c51-5368-48a5-9d85-ddd607b776d5";

    public static String getCurrencyRate(String cryptoName, CurrencyModel model) throws IOException {
        String apiUrl = String.format("https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol=%s&convert=USD", cryptoName.toUpperCase());
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-CMC_PRO_API_KEY", API_KEY);
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = connection.getInputStream();
            Scanner scanner = new Scanner(responseStream);
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();

            JSONObject jsonObject = new JSONObject(response);

            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONObject cryptoObject = dataObject.getJSONObject(cryptoName);

            BigDecimal priceBigDecimal = cryptoObject.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price").setScale(15, RoundingMode.HALF_UP);

            model.setCur_Name(cryptoName);
            model.setCur_OfficialRate(priceBigDecimal);




            return String.format("Official rate of %s to USD: %.15f", model.getCur_Name(), model.getCur_OfficialRate());
        } else {
            return "Failed to fetch rate";
        }
    }
}
