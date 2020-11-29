package com.github.carthax08.scshop.events;

import com.github.carthax08.scshop.Main;
import com.github.carthax08.simplecurrencies.SimpleCurrencies;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class BlockInteractEventHandler implements Listener {

    @EventHandler
    public void onBlockRightClick(PlayerInteractEvent e){
        if(Main.signConfigMap.containsKey(e.getClickedBlock())){
            if(e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Sign sign = (Sign) e.getClickedBlock();
                FileConfiguration signConfig = Main.signConfigMap.get(e.getClickedBlock());
                File file = Main.fileConfigMap.get(signConfig);
                assert sign != null;
                if (sign.getLine(1).equalsIgnoreCase("[item]") && sign.getLine(2).equalsIgnoreCase("[amount]") && sign.getLine(3).equalsIgnoreCase("[price]")) {
                    sign.setLine(1, e.getItem().getType().toString());
                    sign.setLine(2, String.valueOf(e.getItem().getAmount()));
                    sign.setLine(3, signConfig.getString("data.price"));
                    signConfig.set("data.item", e.getItem());
                    signConfig.set("data.amount", e.getItem().getAmount());
                    try {
                        signConfig.save(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Sign sign = (Sign) e.getClickedBlock();
                FileConfiguration signConfig = Main.signConfigMap.get(sign);
                int price = Integer.parseInt(signConfig.getString("data.price"));
                String currency = signConfig.getString("data.currency");
                if(signConfig.getString("data.type").equalsIgnoreCase("selling")) {
                    if (SimpleCurrencies.getCurrency(currency, e.getPlayer()) >= price) {
                        SimpleCurrencies.removeCurrency(currency, e.getPlayer(), price);
                    } else {
                        e.getPlayer().sendMessage("You do not have enough " + currency + " to purchase this item!");
                    }
                }else if(signConfig.getString("data.type").equalsIgnoreCase("buying")){
                    Player playerToCheck = null;
                    OfflinePlayer offlinePlayerToCheck = null;
                    Boolean canBeSold = false;
                    Boolean offline = false;
                    for(Player player : Bukkit.getOnlinePlayers()){
                        if(player.getUniqueId().toString().equals(signConfig.getString("data.player"))){
                            playerToCheck = player;
                        }
                    }
                    for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
                        if(player.getUniqueId().toString().equals(signConfig.getString("data.player"))){
                            offlinePlayerToCheck = player;
                        }
                    }
                    if(offlinePlayerToCheck == null && playerToCheck != null){
                        if(SimpleCurrencies.getCurrency(signConfig.getString("data.currency"), offlinePlayerToCheck) >= signConfig.getInt("data.price")){
                            canBeSold = true;
                        }
                    }else if(playerToCheck == null && offlinePlayerToCheck != null){
                        if(SimpleCurrencies.getCurrency(signConfig.getString("data.currency"), offlinePlayerToCheck) >= signConfig.getInt("data.price")){
                            canBeSold = true;
                            offline = true;
                        }
                    }
                    if(canBeSold) {
                        int amountSold = 0;
                        for (ItemStack item : e.getPlayer().getInventory().getContents()) {
                            if (item.getAmount() >= signConfig.getInt("data.amount") && item.equals(signConfig.getItemStack("data.item"))) {
                                if (item.getAmount() == signConfig.getInt("data.amount")) {
                                    e.getPlayer().getInventory().remove(item);
                                    break;
                                } else {
                                    item.setAmount(item.getAmount() - signConfig.getInt("data.amount"));
                                    break;
                                }
                            }
                        }
                        if(offline = true) {
                            SimpleCurrencies.removeCurrency(currency, offlinePlayerToCheck, price);
                        }else{
                            SimpleCurrencies.removeCurrency(currency, playerToCheck, price);
                        }
                    }
                }
            }
        }
    }

}
