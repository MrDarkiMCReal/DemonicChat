package org.mrdarkimc.demonicchat.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mrdarkimc.demonicchat.DemonicChat;

import java.io.File;

public class Config {
    static File configFile;
    static FileConfiguration config;
    private static void loadConfig() {
        configFile = new File(DemonicChat.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            DemonicChat.getInstance().saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public static FileConfiguration getConfig(){
        if (configFile==null){
            loadConfig();
        }
        return config;
    }
}
