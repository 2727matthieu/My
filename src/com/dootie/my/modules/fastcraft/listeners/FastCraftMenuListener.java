package com.dootie.my.modules.fastcraft.listeners;

import com.dootie.my.modules.fastcraft.MFastCraft;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class FastCraftMenuListener implements Listener {
    
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if(e.getClickedBlock() == null) return;
        if(e.getClickedBlock().getType() != Material.CRAFTING_TABLE) return;
        if(!e.getPlayer().hasPermission("my.fastcraft.use")) return;
        e.setCancelled(true);
        MFastCraft.fastCraftInventoryManager.openFastCraft(e.getPlayer(), 0, 1, null);
        
    }
}
