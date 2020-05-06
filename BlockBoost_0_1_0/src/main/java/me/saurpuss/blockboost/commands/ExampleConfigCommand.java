package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExampleConfigCommand extends SubCommand {

    private BlockBoost bb;

    public ExampleConfigCommand(BlockBoost plugin) {
        bb = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("bb.example")) {
            sender.sendMessage(ChatColor.RED + "You do not have the §4bb.example §cpermission!");
            return true;
        }

        bb.saveResource("example_config.yml", true);
        sender.sendMessage("[BB] " + ChatColor.GREEN + "Successfully created new example_config.yml in " +
                "BlockBoost plugin folder!");
        return true;
    }

    @Override
    public String name() {
        return "example";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

}
