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
        // TODO command manager?

        if ((sender instanceof Player) && !sender.hasPermission("bb.admin"))
                return true;

        // Attempt to reload the plugin
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("bb.reload") || (sender instanceof ConsoleCommandSender))
                bb.reloadManagers();
            else
                sender.sendMessage(ChatColor.DARK_RED + "You do not have the bb.reload permission!");
            return true;
        }

        // TODO world check?
        else if (args[0].equalsIgnoreCase("list")) {
            List<String> list = new ArrayList<>();
            if (args.length == 1) {
                // list all blocks of all types
                for (BB type : BB.values())
                    list.addAll(bb.getBlockManager().getBlockMap(type));

            } else {
                if (args[1].equalsIgnoreCase("all")) {
                    StringJoiner joiner = new StringJoiner("§6, ", "§6Valid Boost Block types: ",
                            "§6.");
                    for (BB type : BB.values()) {
                        joiner.add(ChatColor.YELLOW + type.toString());
                    }

                    sender.sendMessage(joiner.toString());
                    return true;
                } else {
                    try {
                        BB type = BB.valueOf(args[1].toUpperCase());
                        list.addAll(bb.getBlockManager().getBlockMap(type));
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + args[1] + " is not a valid Boost Block " +
                                "type! Use " + ChatColor.BLUE + "/bb list all" + ChatColor.RED +
                                " to list all available types");
                        return true;
                    }
                }
            }

            if (list.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "No valid Boost Blocks found!");
                return true;
            }

            sender.sendMessage("Active Boost Blocks: ");
            list.forEach(sender::sendMessage);
        }

        return true;
    }
}
