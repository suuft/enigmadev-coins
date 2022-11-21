package net.swiftysweet.coins.impl;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.swiftysweet.coins.api.EnigmaCoins;
import net.swiftysweet.coins.mysql.Executor;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnigmaCachedCoinsMysql implements EnigmaCoins {

    EnigmaCoinsMySql coinsMySql;
    TObjectIntMap<String> playerCoinsMap = new TObjectIntHashMap<>();

    public EnigmaCachedCoinsMysql(Executor executor) {
        coinsMySql = new EnigmaCoinsMySql(executor);
    }

    @Override
    public int getCoins(@NonNull String name) {
        return playerCoinsMap.putIfAbsent(name.toLowerCase(), coinsMySql.getCoins(name));
    }

    @Override
    public void setCoins(@NonNull String name, int coins) {
        playerCoinsMap.put(name.toLowerCase(), coins);
        coinsMySql.setCoins(name, coins);
    }

    public void update(@NonNull String name) {
        playerCoinsMap.remove(name.toLowerCase());
        getCoins(name);
    }
}
