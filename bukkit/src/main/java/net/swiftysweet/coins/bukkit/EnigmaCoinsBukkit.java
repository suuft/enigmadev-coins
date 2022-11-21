package net.swiftysweet.coins.bukkit;

import lombok.Getter;
import net.swiftysweet.coins.api.EnigmaCoinsProvider;
import net.swiftysweet.coins.api.util.Instances;
import net.swiftysweet.coins.bukkit.command.CoinsCommand;
import net.swiftysweet.coins.bukkit.listener.CoinsCacheListener;
import net.swiftysweet.coins.bukkit.placeholder.CoinsPlaceholder;
import net.swiftysweet.coins.impl.EnigmaCachedCoinsMysql;
import net.swiftysweet.coins.impl.EnigmaCoinsMySql;
import net.swiftysweet.coins.mysql.Executor;
import net.swiftysweet.coins.mysql.SQLConnection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class EnigmaCoinsBukkit extends JavaPlugin {

    @Getter
    private Executor database;

    @Override
    public void onEnable() {
        getLogger().info("Enabling EnigmaCoins v" + getDescription().getName());
        getLogger().info("Try connect to MySQL database..");

        database = Executor.getExecutor(SQLConnection.newBuilder()
                .setHost(getConfig().getString("MySql.Host"))
                .setPort(getConfig().getInt("MySql.Port"))
                .setUsername(getConfig().getString("MySql.User"))
                .setPassword(getConfig().getString("MySql.Password"))
                .setDatabase(getConfig().getString("MySql.Database"))

                .createTable("EnigmaCoins", "`Name` VARCHAR(32) NOT NULL PRIMARY KEY, `Coins` INT NOT NULL DEFAULT '0'")
                .build());
        getLogger().info("Plugin has successfully connected to MySQL database..");

        EnigmaCoinsProvider.register(getConfig().getBoolean("MySql.Caching") ?
                        new EnigmaCachedCoinsMysql(database) : new EnigmaCoinsMySql(database));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) new CoinsPlaceholder().register();
        if (getConfig().getBoolean("MySql.Caching")) getServer().getPluginManager().registerEvents(new CoinsCacheListener(), this);
        new CoinsCommand(getConfig().getStringList("")).register(this);
        Instances.addInstance(this);

        getLogger().info("Plugin has successfully enabled!");

    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
