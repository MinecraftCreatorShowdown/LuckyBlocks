package net.minecraftcreatorshowdown.luckyblocks;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Luckyblocks extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "MCS - Lucky Blocks: Enabled");
        getServer().getPluginManager().registerEvents(new Events(this), this);//register events class
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "MCS - Lucky Blocks: Disabled");
    }
}
