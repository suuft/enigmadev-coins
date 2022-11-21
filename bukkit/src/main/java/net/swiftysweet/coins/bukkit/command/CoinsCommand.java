package net.swiftysweet.coins.bukkit.command;

import com.google.common.base.Joiner;
import lombok.NonNull;
import net.swiftysweet.coins.api.EnigmaCoins;
import net.swiftysweet.coins.api.EnigmaCoinsProvider;
import net.swiftysweet.coins.bukkit.EnigmaCoinsBukkit;
import net.swiftysweet.coins.bukkit.command.api.YCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class CoinsCommand extends YCommand<CommandSender> {
    public CoinsCommand(List<String> aliases) {
        super("enigmacoins", aliases);
    }

    @Override
    protected void handleExecute(CommandSender sender, String[] args) {
        EnigmaCoins api = EnigmaCoinsProvider.get();
        if (args.length == 0) {
            sender.sendMessage(sender instanceof Player ?
                    // if sender is player
                    sender.hasPermission("enigmacoins.balance") ? msg("Balance").replace("$player_balance", "" + api.getCoins(sender.getName())) : msg("DontHasPermission").replace("$permission", "enigmacoins.balance") :
                    // if sender is console
                    getHelp());
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(
                    sender.hasPermission("enigmacoins.balance.others") ? msg("BalanceOther").replace("$player_balance", "" + api.getCoins(args[0])) : msg("DontHasPermission").replace("$permission", "enigmacoins.balance.others"));
            return;
        }
        switch (args[0].toLowerCase()) {
            case "take":
            case "remove":
            case "rem": {
                if (!sender.hasPermission("enigmacoins.admin")) {
                    sender.sendMessage(msg("DontHasPermission").replace("$permission", "enigmacoins.admin"));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(msg("TakeUsage"));
                    break;
                }
                String targetPlayer = args[1];
                int coins = 0;
                try {
                    coins = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(msg("NotNumber"));
                    break;
                }
                api.takeCoins(targetPlayer, coins);
                sender.sendMessage(msg("Success").replace("$player_balance", "" + api.getCoins(targetPlayer)));
                break;
            }
            case "give":
            case "add":
            case "append": {
                if (!sender.hasPermission("enigmacoins.admin")) {
                    sender.sendMessage(msg("DontHasPermission").replace("$permission", "enigmacoins.admin"));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(msg("GiveUsage"));
                    break;
                }
                String targetPlayer = args[1];
                int coins = 0;
                try {
                    coins = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(msg("NotNumber"));
                    break;
                }
                api.giveCoins(targetPlayer, coins);
                sender.sendMessage(msg("Success").replace("$player_balance", "" + api.getCoins(targetPlayer)));
                break;
            }
            case "set":
            case "install":
            case "s": {
                if (!sender.hasPermission("enigmacoins.admin")) {
                    sender.sendMessage(msg("DontHasPermission").replace("$permission", "enigmacoins.admin"));
                    return;
                }
                if (args.length != 3) {
                    sender.sendMessage(msg("SetUsage"));
                    break;
                }
                String targetPlayer = args[1];
                int coins = 0;
                try {
                    coins = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(msg("NotNumber"));
                    break;
                }
                api.setCoins(targetPlayer, coins);
                sender.sendMessage(msg("Success").replace("$player_balance", "" + api.getCoins(targetPlayer)));
                break;
            }

            default:
                sender.sendMessage(getHelp());
        }

    }

    private String msg(@NonNull String key) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("EnigmaCoins.Messages." + key, "&cN/A"));
    }

    private String getHelp() {
        return ChatColor.translateAlternateColorCodes('&', Joiner.on("\n").join(getConfig().getStringList("EnigmaCoins.Messages.Help")));
    }
    private FileConfiguration getConfig() {
        return EnigmaCoinsBukkit.getInstance().getConfig();
    }

}
