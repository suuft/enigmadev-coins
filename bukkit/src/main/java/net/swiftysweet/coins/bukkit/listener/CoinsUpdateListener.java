package net.swiftysweet.coins.bukkit.listener;

import lombok.NonNull;
import net.swiftysweet.coins.api.util.Instances;
import net.swiftysweet.coins.bukkit.EnigmaCoinsBukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CoinsUpdateListener implements Listener {

    private EnigmaCoinsBukkit getPlugin() {
        return Instances.getInstance(EnigmaCoinsBukkit.class);
    }

    @EventHandler
    public void onJoin(@NonNull PlayerJoinEvent event) {
        if (!getPlugin().getLatestVersion().equalsIgnoreCase(getPlugin().getDescription().getVersion()) && getPlugin().getConfig().getBoolean("EnigmaCoins.NotifyAboutUpdates")) {
            event.getPlayer().sendMessage("§c§lEnigmaCoins §7:: §fUpdate plugin to latest version!");
        }
    }

}
