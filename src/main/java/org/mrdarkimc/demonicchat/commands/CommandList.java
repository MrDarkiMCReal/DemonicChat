package org.mrdarkimc.demonicchat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.mrdarkimc.demonicchat.utils.Config;

public class CommandList implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equals("DemonicChat")){
            switch (strings[0]){
                case "reload":
                    Config.getInstance.reload();
                    return true;
            }
        }
        return true;
    }
}
