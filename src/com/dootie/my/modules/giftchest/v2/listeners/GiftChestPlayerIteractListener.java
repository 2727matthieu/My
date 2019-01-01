package com.dootie.my.modules.giftchest.v2.listeners;


import com.dootie.my.modules.giftchest.v2.api.GiftChest;
import com.dootie.my.modules.items.MItemStack;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class GiftChestPlayerIteractListener implements Listener {
    
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block b = event.getClickedBlock();
        double xdrop = (double)b.getX() + 0.5;
        double ydrop = (double)b.getY() + 1.5;
        double zdrop = (double)b.getZ() + 0.5;
        World world = b.getWorld();
        Location droploc = new Location(world, xdrop, ydrop, zdrop);
        ItemStack is = event.getPlayer().getItemInHand();
        if (is.getType() != Material.PLAYER_HEAD) return;
        
        MItemStack mis = new MItemStack(is);
        if(mis.data.get("giftchest") == null) return;
        
        GiftChest gc = GiftChest.getGiftchest((String) mis.data.get("giftchest"));
        
        event.setCancelled(true);
        if(is.getAmount() == 1)
            event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        else{
            is.setAmount(is.getAmount()-1);
            event.getPlayer().getInventory().setItemInMainHand(is);
        }
                    
        Random random = new Random();
        ItemStack drop = gc.getRewards().get(random.nextInt(gc.getRewards().size())).getItemStack();
        world.playEffect(droploc, gc.particle, gc.particle_amount);
        if(gc.sound != null)
            world.playSound(droploc, gc.sound, 3.0F, 0.5F);
        world.dropItem(droploc, drop);
    }
}