package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BlockBoostCommand implements CommandExecutor {

    private BlockBoost bb;

    public BlockBoostCommand(BlockBoost plugin) {
        bb = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bb.admin")) {
            // TODO improve
            sender.sendMessage("BlockBoost v1.0 - Author SaurPuss");
            return true;
        }


        if (args.length >= 1) {
            // Attempt to reload the plugin
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof Player && !sender.hasPermission("bb.reload"))
                    sender.sendMessage(ChatColor.RED + "You do not have the bb.reload permission!");
                else {
                    sender.sendMessage("Reloading BB");
                    bb.reloadManagers();
                }

                return true;
            }

            // TODO world check?
            else if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage("List");
                return true;
            }
        }

        return false;
    }
}
