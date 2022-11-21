package net.swiftysweet.coins.bukkit.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.swiftysweet.coins.api.EnigmaCoinsProvider;
import net.swiftysweet.coins.bukkit.EnigmaCoinsBukkit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CoinsPlaceholder extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        // Зачем? - НАДО!
        return true;
    }
    @Override
    public String getAuthor() {
        return "swiftysweet";
    }

    @Override
    public String getIdentifier() {
        return "enigmacoins";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
    @Override
    public String getRequiredPlugin() {
        return "EnigmaCoins";
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        switch (params.toLowerCase()) {
            case "bal":
            case "coins":
            case "count":
            case "amount":
            case "balance":
            case "value":
                return EnigmaCoinsProvider.get().getCoins(player.getName()) + "";
        }

        return "§cN/A";
    }
}
