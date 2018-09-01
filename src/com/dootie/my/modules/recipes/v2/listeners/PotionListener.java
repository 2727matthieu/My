package com.dootie.my.modules.recipes.v2.listeners;

import com.dootie.my.modules.recipes.v2.helpers.BrewingRecipe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;


public class PotionListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void PotionListener(InventoryClickEvent e){
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.BREWING) return; 
    }
}