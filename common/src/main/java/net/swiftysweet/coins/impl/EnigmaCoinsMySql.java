package net.swiftysweet.coins.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.swiftysweet.coins.api.EnigmaCoins;
import net.swiftysweet.coins.mysql.Executor;

import java.util.Locale;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnigmaCoinsMySql implements EnigmaCoins {

    Executor mysql;

    @Override
    public int getCoins(@NonNull String name) {
        return mysql.executeQuery(false, "SELECT * FROM `EnigmaCoins` WHERE `Name` = ? LIMIT 1", rs -> {
            if (!rs.next()) {
                return 0;
            }
            return rs.getInt("Coins");
        }, name.toLowerCase(Locale.ROOT));
    }

    @Override
    public void setCoins(@NonNull String name, int coins) {
        mysql.execute(true, "INSERT INTO `EnigmaCoins`(`Name`, `Coins`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `Coins` = ?", name.toLowerCase(Locale.ROOT), coins, coins);
    }
}
