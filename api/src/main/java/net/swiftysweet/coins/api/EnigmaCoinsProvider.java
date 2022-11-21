package net.swiftysweet.coins.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EnigmaCoinsProvider {

    private EnigmaCoins instance;
    public EnigmaCoins get() {
        return instance;
    }

    public void register(EnigmaCoins coins) {
        instance = coins;
    }
}
