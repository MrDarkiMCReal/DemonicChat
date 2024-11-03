package org.mrdarkimc.demonicchat;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.demonicchat.chat.ChatEvent;
import org.mrdarkimc.demonicchat.commands.CommandList;
import org.mrdarkimc.demonicchat.hooks.VaultHook;
import org.mrdarkimc.demonicchat.utils.Config;

public final class DemonicChat extends JavaPlugin {
    private static DemonicChat instance;
    public static DemonicChat getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChatEvent(),this);
        getServer().getPluginCommand("DemonicChat").setExecutor(new CommandList());
        instance = this;
        new VaultHook();
        new Config();
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
