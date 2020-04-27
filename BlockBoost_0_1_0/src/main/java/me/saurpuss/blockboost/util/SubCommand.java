package me.saurpuss.blockboost.util;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    public abstract boolean onCommand(CommandSender sender, String args[]);

    public abstract String name();

    public abstract String[] aliases();

}
