package net.swiftysweet.coins.bukkit.listener;

import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.swiftysweet.coins.api.EnigmaCoinsProvider;
import net.swiftysweet.coins.impl.EnigmaCachedCoinsMysql;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class CoinsCacheListener implements Listener {

    public EnigmaCachedCoinsMysql getCacheHandle() {
        return (EnigmaCachedCoinsMysql) EnigmaCoinsProvider.get();
    }

    @EventHandler
    public void onJoin(@NonNull PlayerJoinEvent event) {
        getCacheHandle().update(event.getPlayer().getName());
    }

    @EventHandler
    public void onQuit(@NonNull PlayerJoinEvent event) {
        getCacheHandle().getPlayerCoinsMap().remove(event.getPlayer().getName().toLowerCase());
    }
}
