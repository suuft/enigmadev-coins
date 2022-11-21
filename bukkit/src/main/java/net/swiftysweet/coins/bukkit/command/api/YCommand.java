package net.swiftysweet.coins.bukkit.command.api;

import org.bukkit.plugin.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class YCommand<S extends CommandSender>
        extends Command
        implements CommandExecutor {

    private boolean onlyPlayers = false;

    public YCommand(String command) {
        this(command, new ArrayList<>());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return true;
    }

    public YCommand(String command, List<String> aliases) {
        super(command, "The YCommand. Thank for using.", ("/").concat(command), aliases);
        this.onlyPlayers = ((Class<S>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]).isAssignableFrom(Player.class);
    }

    public YCommand(String command, String... aliases) {
        super(command, "The YCommand. Thank for using.", ("/").concat(command), Arrays.stream(aliases).collect(Collectors.toList()));
        this.onlyPlayers = ((Class<S>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]).isAssignableFrom(Player.class);
    }

    protected abstract void handleExecute(S sender, String[] args);

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        handleExecute((S) commandSender, args);
        return true;
    }

    public void register(Plugin plugin){
        try {
            CommandManager.registerCommand(plugin, this, getName()
                    , getAliases());
        } catch(Exception proebaliSuccess) {
            proebaliSuccess.fillInStackTrace();
        }
    }

}
