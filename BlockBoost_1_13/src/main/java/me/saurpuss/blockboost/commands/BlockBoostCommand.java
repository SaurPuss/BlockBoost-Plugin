package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlockBoostCommand implements CommandExecutor {

    private BlockBoost bb;

    public BlockBoostCommand(BlockBoost plugin) {
        bb = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        // bb list bounce



        // bb reload
        if (sender.hasPermission("bb.reload") && args[0].equalsIgnoreCase("reload")) {
                bb.reloadManagers();
                return true;
        }

        return true;
    }
}
