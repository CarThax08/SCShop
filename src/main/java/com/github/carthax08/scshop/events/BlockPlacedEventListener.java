package com.github.carthax08.scshop.events;

import com.github.carthax08.scshop.Main;
import com.github.carthax08.scshop.data.ShopYmlFile;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.File;
import java.io.IOException;

public class BlockPlacedEventListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block block = event.getBlock();
        if(checkIfBlockIsSign(block)){
            Sign sign = (Sign) block;
            if(sign.getLine(0).equalsIgnoreCase("[Shop]")){
                if(!sign.getLine(1).isEmpty() || !sign.getLine(2).isEmpty() || !sign.getLine(3).isEmpty()){
                    event.getPlayer().sendMessage("Please check the Spigot page for the proper formatting.");
                }else{
                    ShopYmlFile.setupShopConfig(String.valueOf(block.getX()) + block.getY() + block.getZ() ,block);
                    FileConfiguration signConfig = Main.signConfigMap.get(block);
                    File file = Main.fileConfigMap.get(signConfig);
                    signConfig.set("info.location", block.getLocation().toString());
                    signConfig.set("data.type", sign.getLine(2));
                    signConfig.set("data.currency", sign.getLine(3));
                    signConfig.set("data.price", sign.getLine(1));
                    signConfig.set("data.player", event.getPlayer().getUniqueId().toString());
                    event.getPlayer().sendMessage("Please punch the sign with the item you want to sell in a stack of how many you want to sell!");
                    try {
                        signConfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean checkIfBlockIsSign(Block block) {
        switch (block.getType()) {
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case ACACIA_SIGN:
            case ACACIA_WALL_SIGN:
            case WARPED_SIGN:
            case WARPED_WALL_SIGN:
            case CRIMSON_SIGN:
            case CRIMSON_WALL_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
                return true;
        }
        return false;
    }

}
