package com.github.carthax08.scshop.data;

import com.github.carthax08.scshop.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ShopYmlFile {
    public static File file;
    public static FileConfiguration customConfig;

    public static void setupShopConfig(String name, Block sign){
        file = new File(Bukkit.getPluginManager().getPlugin("SCShop").getDataFolder(), "/shops/" + name + ".yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        customConfig = YamlConfiguration.loadConfiguration(file);
        Main.signConfigMap.put(sign, customConfig);
        Main.fileConfigMap.put(customConfig, file);
    }
    public static void saveConfig(){
    }
}
