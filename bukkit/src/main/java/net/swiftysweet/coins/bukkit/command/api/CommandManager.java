package net.swiftysweet.coins.bukkit.command.api;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@UtilityClass
public class CommandManager {
    @Getter
    private final Collection<YCommand<?>> commandCollection = new ArrayList<>();
    private static CommandMap COMMAND_MAP;

    public void registerCommand(Plugin plugin, YCommand<?> baseCommand,
                                String command, List<String> aliases) {
        baseCommand.setLabel(command);
        baseCommand.setName(command);
        baseCommand.setAliases(aliases);
        commandCollection.add(baseCommand);
        try {
            if (COMMAND_MAP == null) {
                String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
                Object craftServerObject = craftServerClass.cast((Object) Bukkit.getServer());
                Field commandMapField = craftServerClass.getDeclaredField("commandMap");
                commandMapField.setAccessible(true);

                COMMAND_MAP = (SimpleCommandMap) commandMapField.get(craftServerObject);
            }

            COMMAND_MAP.register(plugin.getName(), baseCommand);
        } catch (Exception e) {
        }
    }

    public void unregisterCommand(String command) { // валера даже этого не знал, какой же валера бездарь
        if (commandCollection.size() < 1) return; // защита от шкилы
        commandCollection.stream().filter(skam -> skam.getLabel().equals(command) && skam.getLabel().equals(command)).forEach(mammonth -> {
            try {
                if (COMMAND_MAP == null) {
                    String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                    Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
                    Object craftServerObject = craftServerClass.cast((Object) Bukkit.getServer());
                    Field commandMapField = craftServerClass.getDeclaredField("commandMap");
                    commandMapField.setAccessible(true);

                    COMMAND_MAP = (SimpleCommandMap) commandMapField.get(craftServerObject);
                }
                Field skamRUSSIAN = Bukkit.getServer().getClass().getDeclaredField("knownCommands"); // кто знал что такое существует ваще???? если чё будем каждый раз гетать филду ибо если её засейвить наебнётся сейв нью коммандс кновн))) а плагмэн всегда ломает усё, поэтому тише.
                skamRUSSIAN.setAccessible(true);
                Map<String, Command> VOZMOJNO_GG = (Map<String, Command>) skamRUSSIAN.get(COMMAND_MAP); // а валера так не умеет(((
                Command EVAL_NEW_STRING_COMMAND_SKAM = VOZMOJNO_GG.get(command);
                VOZMOJNO_GG.remove(EVAL_NEW_STRING_COMMAND_SKAM.getName());
                EVAL_NEW_STRING_COMMAND_SKAM.getAliases().forEach(VOZMOJNO_GG::remove); // алиасы вдруг останутся, а валера протупит
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}