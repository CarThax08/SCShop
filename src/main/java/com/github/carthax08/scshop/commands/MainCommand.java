package com.github.carthax08.scshop.commands;

import com.github.carthax08.scshop.Main;
import com.github.carthax08.simplecurrencies.SimpleCurrencies;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1){
            sender.sendMessage("You must provide an argument!");
            return false;
        }else{
            if(args[0].equalsIgnoreCase("reload")){
                Main.reloadConfigFile();
            }
        }
        return false;
    }
}
