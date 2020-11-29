package com.github.carthax08.scshop;

import com.github.carthax08.scshop.events.BlockPlacedEventListener;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class Main extends JavaPlugin {
    private static Main instance;
    public static HashMap<Block, FileConfiguration> signConfigMap = new HashMap<>();
    public static HashMap<FileConfiguration, File> fileConfigMap = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getServer().getPluginManager().registerEvents(new BlockPlacedEventListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static void reloadConfigFile(){
        instance.reloadConfig();
    }
}
