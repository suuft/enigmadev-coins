package net.swiftysweet.coins.bukkit;

import lombok.Getter;
import net.swiftysweet.coins.api.EnigmaCoinsProvider;
import net.swiftysweet.coins.bukkit.command.CoinsCommand;
import net.swiftysweet.coins.bukkit.command.api.CommandManager;
import net.swiftysweet.coins.bukkit.listener.CoinsCacheListener;
import net.swiftysweet.coins.bukkit.listener.CoinsUpdateListener;
import net.swiftysweet.coins.bukkit.placeholder.CoinsPlaceholder;
import net.swiftysweet.coins.impl.EnigmaCachedCoinsMysql;
import net.swiftysweet.coins.impl.EnigmaCoinsMySql;
import net.swiftysweet.coins.mysql.Executor;
import net.swiftysweet.coins.mysql.SQLConnection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Getter
public class EnigmaCoinsBukkit extends JavaPlugin {

    @Getter
    private static EnigmaCoinsBukkit instance;
    private Executor database;
    private String latestVersion;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        getLogger().info("Enabling EnigmaCoins v" + getDescription().getName());
        saveDefaultConfig();
        getLogger().info("Try connect to MySQL database..");

        database = Executor.getExecutor(SQLConnection.newBuilder()
                .setHost(getConfig().getString("EnigmaCoins.MySql.Host"))
                .setPort(getConfig().getInt("EnigmaCoins.MySql.Port"))
                .setUsername(getConfig().getString("EnigmaCoins.MySql.User"))
                .setPassword(getConfig().getString("EnigmaCoins.MySql.Password"))
                .setDatabase(getConfig().getString("EnigmaCoins.MySql.Database"))

                .createTable("EnigmaCoins", "`Name` VARCHAR(32) NOT NULL PRIMARY KEY, `Coins` INT NOT NULL DEFAULT '0'")
                .build());
        getLogger().info("Plugin has successfully connected to MySQL database..");

        EnigmaCoinsProvider.register(getConfig().getBoolean("EnigmaCoins.MySql.Caching") ?
                        new EnigmaCachedCoinsMysql(database) : new EnigmaCoinsMySql(database));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) new CoinsPlaceholder().register();
        if (getConfig().getBoolean("EnigmaCoins.MySql.Caching")) getServer().getPluginManager().registerEvents(new CoinsCacheListener(), this);
        new CoinsCommand(getConfig().getStringList("EnigmaCoins.CoinsAliases")).register(this);

        getLogger().info("Plugin has successfully enabled!");

        if (getConfig().getBoolean("EnigmaCoins.CheckUpdates", true))  {
            checkUpdates();
            getServer().getPluginManager().registerEvents(new CoinsUpdateListener(), this);

        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        CommandManager.unregisterCommand("enigmacoins");
        getLogger().info("Plugin has successfully disabled!");

    }

    private void checkUpdates() {
        try {
            getLogger().info("Checking for updates...");
            URL url = new URL( "https://raw.githubusercontent.com/swiftysweet/enigmadev-coins/master/VERSION");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(1200);
            conn.setReadTimeout(1200);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                latestVersion = in.readLine().trim();
                if (!latestVersion.equalsIgnoreCase(getDescription().getVersion())) {
                    getLogger().warning("You have an outdated version of EnigmaCoins on your server. Update - https://google.com");
                } else {
                    getLogger().info("You have the latest version of EnigmaCoins on your server.");
                }
            }
        } catch (IOException e) {
            getLogger().warning("Error checking for updates. Please try again later");
        }
    }
}
