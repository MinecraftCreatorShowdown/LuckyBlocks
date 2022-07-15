package net.minecraftcreatorshowdown.luckyblocks;

import net.kyori.adventure.sound.SoundStop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class Events implements Listener {
    Luckyblocks plugin;

    public Events(Luckyblocks plugin) {this.plugin = plugin;}

    //CUSTOMBLOCKPLACEEVENT
    //Detects when a Custom Block is placed and returns the custom model data of the block placed and the item frame placed
    @EventHandler//custom block place
    public void onSpawnEntityCB(EntitySpawnEvent event){
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.ITEM_FRAME){//if entity spawned is an item frame
            ItemFrame itemFrame = (ItemFrame) entity;
            List<Entity> nearbyEntities = (List<Entity>) entity.getLocation().getWorld().getNearbyEntities(entity.getLocation(), 5, 5, 5);
            if(!nearbyEntities.isEmpty()){//if list not null
                for (Entity nearbyEntity : nearbyEntities) {//for loop to make sure we don't get players
                    if (nearbyEntity.getType() != EntityType.PLAYER) continue;
                    Player player = (Player) nearbyEntity;//cast into player
                    ItemStack playerItem = player.getInventory().getItemInMainHand();
                    if(playerItem.getType() != Material.ITEM_FRAME) continue;//if not item frame continue
                    if(!playerItem.hasItemMeta()) continue;//if no meta continue
                    if(!playerItem.getItemMeta().hasCustomModelData()) continue;//if no cmd continue
                    if(itemFrame.getItem().getType() == Material.ITEM_FRAME) continue;//if this entity is the entity that the 1 tick delay spawns, ignore it
                    //call event
                    if(playerItem.getItemMeta().getCustomModelData() == 100){
                        //intentionally making 1 tick of delay so the block appearing and the code running happen at the same time
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            entity.getLocation().getBlock().setType(Material.LIGHT_BLUE_STAINED_GLASS);//place base block
                            Bukkit.dispatchCommand(getServer().getConsoleSender(), "execute positioned "+ entity.getLocation().getBlock().getX() + " " + entity.getLocation().getBlock().getY() + " " + entity.getLocation().getBlock().getZ() + " positioned ^ ^ ^-0.5 align xyz run summon armor_stand ~ ~ ~ {Fire:999999,Small:1b,Marker:1b,Invisible:1b,ArmorItems:[{},{},{},{id:\"minecraft:item_frame\",Count:1b,tag:{CustomModelData:100}}]}");//at all item frames that match the chars of the one we just placed (before it gets killed so hopefully it only runs once), place the armor stand for the texture
                            entity.remove();//kill entity
                        }, 1L); //20 Tick = 1 Second
                    }
                }
            }
        }
    }

    //CUSTOMARMORSTANDBLOCKBREAKEVENT
    @EventHandler//custom block break ARMOR STAND
    public void onBlockBreakCBAS(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        //CUSTOM BLOCK BREAKING
        List<Entity> nearbyEntities = (List<Entity>) block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 1, 1, 1);
        if(!nearbyEntities.isEmpty()){//if list not null
            for (Entity nearbyEntity : nearbyEntities) {//for loop to make sure we don't get players
                if (nearbyEntity.getType() == EntityType.ARMOR_STAND) {//if nearest entity is armor stand
                    ArmorStand armorStand = (ArmorStand) nearbyEntity;//cast into armor stand
                    if (armorStand.isMarker()) {//make sure we got the right armor stand
                        if (armorStand.getLocation().getBlock().getLocation().getBlockX() != block.getLocation().getBlock().getX() || armorStand.getLocation().getBlock().getLocation().getBlockY() != block.getLocation().getBlock().getY() || armorStand.getLocation().getBlock().getLocation().getBlockZ() != block.getLocation().getBlock().getZ()) continue;
                        if (armorStand.getEquipment().getHelmet() == null) return;//make sure it has a helmet
                        if (!armorStand.getEquipment().getHelmet().hasItemMeta()) return;//make sure it has nbt
                        if (!armorStand.getEquipment().getHelmet().getItemMeta().hasCustomModelData()) return;//make sure it has nbt
                        //call event
                        armorStand.remove();//remove it
                        event.setDropItems(false);
                        block.getWorld().stopSound(SoundStop.named(Sound.BLOCK_GLASS_BREAK));
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 5, 1);
                        LuckyBlockEvent(player, block);
                    }
                }
            }
        }
    }

    public void LuckyBlockEvent(Player player, Block block){
        int min = 1;
        int max = 6;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        if(random_int == 1){
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND_SWORD, 1));
        }
        else if(random_int == 2){
            for (int i = 0; i < 5; i++) {
                block.getWorld().spawn(block.getLocation(), Bee.class, bee -> {
                    bee.setAnger(630);
                    bee.setTarget(player);
                });
            }
        }
        else if(random_int == 3){
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                block.getWorld().spawn(block.getLocation(), TNTPrimed.class, TNTPrimed -> {
                    TNTPrimed.setFuseTicks(finalI);
                });
            }
        }
        else if(random_int == 4){
            block.getLocation().getBlock().setType(Material.IRON_BLOCK);
        }
        else if(random_int == 5){
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLDEN_APPLE, 1));
        }
        else if(random_int == 6){
            block.getWorld().spawn(block.getLocation(), Donkey.class, entity -> {
                Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(1.5);
            });
        }
    }
}