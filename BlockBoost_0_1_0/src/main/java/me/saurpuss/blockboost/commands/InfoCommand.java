package me.saurpuss.blockboost.commands;

import me.saurpuss.blockboost.util.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class InfoCommand extends SubCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        return Bukkit.dispatchCommand(sender, "version blockboost");
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
