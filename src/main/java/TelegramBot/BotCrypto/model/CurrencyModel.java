package TelegramBot.BotCrypto.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class CurrencyModel {
    String cur_Name;
    BigDecimal cur_OfficialRate;
}