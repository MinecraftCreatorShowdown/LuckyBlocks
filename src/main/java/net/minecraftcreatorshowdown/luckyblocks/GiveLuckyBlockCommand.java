package net.minecraftcreatorshowdown.luckyblocks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class GiveLuckyBlockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)){//return if command sent in console
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("giveluckyblock")) {
            //make item stack
            ItemStack lbs = new ItemStack(Material.ITEM_FRAME, 16);//make item stack
            ItemMeta lbsm = lbs.getItemMeta();//get item stack meta
            lbsm.displayName(Component.text("MCS Lucky Block").decoration(TextDecoration.ITALIC, false));//assign name
            lbsm.setCustomModelData(100);//assign custom model data, 100 is the lucky block
            lbs.setItemMeta(lbsm);//reassign meta back into item stack
            player.getInventory().addItem(lbs);//give player item
            player.sendMessage("Gave player 16 Lucky Blocks");
        }
        return true;
    }
}
