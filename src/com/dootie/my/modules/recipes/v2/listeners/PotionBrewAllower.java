package com.dootie.my.modules.recipes.v2.listeners;

import com.dootie.my.My;
import com.dootie.my.modules.recipes.v2.helpers.BrewingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;


public class PotionBrewAllower implements Listener {
    
        @EventHandler(priority = EventPriority.HIGHEST)
        public void potionBrewAllower(final InventoryClickEvent e) {
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().getType() != InventoryType.BREWING) return;
            if (!(e.getClick() == ClickType.LEFT)) return;
            if(e.getSlot() == 4) return;
            
            final ItemStack is = e.getCurrentItem(); //We want to get the item in the slot
            final ItemStack is2 = e.getCursor().clone(); //And the item in the cursor
            if(is2 == null) return;
            if(is2.getType() == Material.AIR) return;

            e.setCursor(is);
            e.getClickedInventory().setItem(e.getSlot(), is2);
            ((Player)e.getView().getPlayer()).updateInventory();
            
            e.setCancelled(true);
            
            BrewerInventory binventory = (BrewerInventory) e.getInventory();
            
            if(binventory.getIngredient() == null) return;
            BrewingRecipe recipe = BrewingRecipe.getRecipe(binventory);
            if(recipe == null) return;
            if(((BrewerInventory) e.getClickedInventory()).getHolder().getFuelLevel() < 1) return;
            recipe.startBrewing((BrewerInventory) e.getClickedInventory());
        }
        
        
}
