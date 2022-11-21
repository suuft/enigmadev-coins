package net.swiftysweet.coins.api;

import lombok.NonNull;

public interface EnigmaCoins {

    int getCoins(@NonNull String name);

    void setCoins(@NonNull String name, int coins);

    default void takeCoins(@NonNull String name, int coins) {
        setCoins(name, getCoins(name) - coins);
    }

    default void giveCoins(@NonNull String name, int coins) {
        setCoins(name, getCoins(name) + coins);
    }

    default boolean hasCoins(@NonNull String name, int coins) {
        return getCoins(name) >= coins;
    }
}
