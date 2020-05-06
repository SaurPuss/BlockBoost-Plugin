package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ListBlocksCommand extends SubCommand {

    private BlockBoost bb;

    ListBlocksCommand(BlockBoost plugin) {
        bb = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        List<AbstractBlock> list;
        if (args.length == 0) { // Display all active boost blocks
            List<String> temp = new ArrayList<>();
            for (BB type : BB.values()) {
                list = new ArrayList<>(bb.getBlockManager().getBlockList(type));
                if (!list.isEmpty()) {
                    temp.add(ChatColor.BLUE + "Registered " + type.toString() + " blocks: ");
                    list.forEach(block -> temp.add(block.toColorString()));
                }
            }

            temp.forEach(sender::sendMessage); // TODO pagination
            return true;
        }

        else if (args[0].equalsIgnoreCase("type")) {
            StringJoiner joiner = new StringJoiner("§a, §2", ChatColor.GREEN +
                    "Available BlockBoost types: §2", "§a.");
            for (BB type : BB.values()) {
                joiner.add(type.toString());
            }

            sender.sendMessage(joiner.toString());
            return true;
        }

        else {
            BB type = BB.getIfPresent(args[0]);
            if (type == null) {
                sender.sendMessage(ChatColor.RED + args[0] + " is not a valid Boost Block! Use §2/bb list " +
                        "type §cto see available BlockBoost types!");
                return false;
            }

            list = new ArrayList<>(bb.getBlockManager().getBlockList(type));
            if (list.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "No registered " + type.name() + " Boost Blocks found!");
                return true;
            }

            sender.sendMessage(ChatColor.BLUE + "Registered " + type.toString() + " blocks: ");
            list.forEach(block -> sender.sendMessage(block.toColorString())); // TODO pagination
            return true;
        }
    }

    @Override
    public String name() {
        return "list";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
