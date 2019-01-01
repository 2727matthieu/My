package com.dootie.my.modules.itemeffects.listeners;


import com.dootie.my.modules.items.MItemStack;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class PlayerInteractListener implements Listener {
    
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        ItemStack tool = e.getPlayer().getItemInHand();

        if (tool == null) return;
        
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            MItemStack mis = new MItemStack(tool);
            if(mis.data.get("tool.effect") == null) return;
            if(!mis.data.get("tool.effect").equals("breakbedrock")) return;
            if(block.getType() != Material.BEDROCK) return;
            block.setType(Material.AIR);
            block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(Material.BEDROCK));
            tool.setDurability((short) (tool.getDurability() + 1));
        }
    }
}