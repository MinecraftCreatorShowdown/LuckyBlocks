package net.minecraftcreatorshowdown.luckyblocks;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Luckyblocks extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("giveluckyblock")).setExecutor(new GiveLuckyBlockCommand());//register command
        getServer().getPluginManager().registerEvents(new Events(this), this);//register events class
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "MCS - Lucky Blocks: Enabled");//enable message
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "MCS - Lucky Blocks: Disabled");
    }
}
