package com.dootie.my.modules.recipes.v2.listeners;

import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.modules.recipes.v2.api.BrewingRecipe;
import com.dootie.my.modules.recipes.v2.api.helpers.BrewClock;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class CraftInteractions implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void craftInteractions(final InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();
        if (inventory == null) return;
        if (inventory.getType() == InventoryType.WORKBENCH) craftingInteractions(e);
        if (inventory.getType() == InventoryType.BREWING) brewingInteractions(e);
    }

    private void craftingInteractions(InventoryClickEvent e){
        if(e.getSlot() != 0) return;
        
        CraftingInventory inventory = (CraftingInventory) e.getInventory();
        Player player = (Player)e.getWhoClicked();
        ItemStack output = e.getCurrentItem().clone();
        MyRecipe[] recipes = MRecipes.recipes.stream().filter((recipe) -> {
            return recipe.getOutput().getItemStack().equals(output);
        }).toArray((size) -> new MyRecipe[size]);
        
        if(recipes == null || recipes.length == 0) return;
        if(e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT){
            e.setCancelled(true);
            return;
        }
        for(MyRecipe recipe : recipes){
            if(recipe.getPermission() != null && !player.hasPermission(recipe.getPermission())) continue;
            for(int i = 0; i < 9; i++){
                ItemStack current_table_item = inventory.getContents()[i+1].clone();
                if(recipe.getSlots()[i] == null) continue;
                int amount = recipe.getSlots()[i].getItemStack().getAmount();
                int result = current_table_item.getAmount() - amount +1;
                if(result < 0) result = 0;
                current_table_item.setAmount(result);
                inventory.setItem(i+1, current_table_item);
                e.setCurrentItem(output);
            }
            return;
        }
        
        e.setCancelled(true);
        player.sendMessage("§c[My] You don't have permission to craft this item!");
    }

    private void brewingInteractions(InventoryClickEvent e){
        BrewerInventory inventory = (BrewerInventory) e.getClickedInventory();
        BrewingStand holder = inventory.getHolder();
        if(BrewClock.isBrewing(inventory)){
            e.getWhoClicked().sendMessage("§c[My] The brewing already started!");
            e.setCancelled(true);
            return;
        }
        
        if (!(e.getClick() == ClickType.LEFT)) return;
        if(e.getSlot() == 4) return;

        final ItemStack is = e.getCurrentItem();
        final ItemStack is2 = e.getCursor().clone();
        if(is2 == null) return;
        if(is2.getType() == Material.AIR) return;
        
        e.setCancelled(true);
        e.setCursor(is);
        inventory.setItem(e.getSlot(), is2);
        ((Player)e.getView().getPlayer()).updateInventory();
        if(inventory.getIngredient() == null) return;
        
        List<BrewingRecipe> recipes = BrewingRecipe.getRecipe(inventory);
        if(recipes == null) return;
        for(BrewingRecipe recipe : recipes){
            if(recipe.events == null){
                if(holder.getFuelLevel() < 1) continue;
                new BrewClock(inventory).start(recipe);
                return;
            }else{
                if(holder.getFuelLevel() < 1) continue;
                new BrewClock(inventory).start(recipe);
                return;
            }
        }
    }
}
