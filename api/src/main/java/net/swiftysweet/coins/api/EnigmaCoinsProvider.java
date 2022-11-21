package net.swiftysweet.coins.api;

import lombok.experimental.UtilityClass;
import net.swiftysweet.coins.api.util.Instances;

@UtilityClass
public class EnigmaCoinsProvider {

    public EnigmaCoins get() {
        return Instances.getInstance(EnigmaCoins.class);
    }

    public void register(EnigmaCoins coins) {
        Instances.addInstance(coins);
    }
}
