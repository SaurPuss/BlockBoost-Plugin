package me.saurpuss.blockboost.blocks.single;

import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CommandBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private boolean consoleSender;
        private String command;
        private String permission;

        public Builder(Material material) {
            this.material = material;
        }

        public Builder withWorld(String world) {
            this.world = world;

            return this;
        }

        public Builder withIncludeWorld(boolean includeWorld) {
            this.includeWorld = includeWorld;

            return this;
        }

        public Builder withConsoleSender(boolean consoleSender) {
            this.consoleSender = consoleSender;

            return this;
        }

        public Builder withCommand(String command) {
            this.command = command;

            return this;
        }

        public Builder withPermission(String permission) {
            this.permission = permission;

            return this;
        }

        public CommandBlock build() {
            CommandBlock block = new CommandBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.consoleSender = this.consoleSender;
            block.command = this.command;
            block.permission = this.permission;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private boolean consoleSender;
    private String command;
    private String permission;

    private CommandBlock() {}

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    protected void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String getWorld() {
        return world;
    }

    @Override
    public void setWorld(String world) {
        this.world = world;
    }

    @Override
    public boolean isIncludeWorld() {
        return includeWorld;
    }

    @Override
    public void setIncludeWorld(boolean includeWorld) {
        this.includeWorld = includeWorld;
    }

    public boolean getConsoleSender() {
        return consoleSender;
    }

    public void setConsoleSender(boolean consoleSender) {
        this.consoleSender = consoleSender;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toColorString() {
        return ChatColor.GREEN + material.toString() + ChatColor.GRAY + " (world: " + world +
                ", include: " + includeWorld + ", command: " + command + ", permission node: " +
                permission + ")";
    }

    @Override
    public String toString() {
        return material.toString() + " (world: " + world + ", include: " + includeWorld +
                ", command: " + command + ", permission node: " + permission + ")";
    }

    @Override
    public void activate(final Player player) {
        // Permission check
        if (!player.hasPermission(permission) && permission != null && !permission.equals("")) {
            return;
        }

        String cmd = command.replaceAll("%PLAYER%", player.getName())
                .replaceAll("%PLAYERUUID", player.getUniqueId().toString());

        Bukkit.dispatchCommand((consoleSender ? Bukkit.getConsoleSender() : player), cmd);
    }
}
