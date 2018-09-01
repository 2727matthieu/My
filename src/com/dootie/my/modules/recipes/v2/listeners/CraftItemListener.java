package com.dootie.my.modules.recipes.v2.listeners;



import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.helpers.ItemStackUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;


public class CraftItemListener implements Listener{
    
    @EventHandler
    public void CraftItem(CraftItemEvent e) {
        Player player = (Player)e.getWhoClicked();
        ItemStack output = e.getCurrentItem();

        for(MyRecipe recipe : MRecipes.recipes){
            if(ItemStackUtils.compare(recipe.getOutput(), output, true)){
                if(recipe.getPermission() == null) return;
                if(player.hasPermission(recipe.getPermission())) return;
                
                e.setCancelled(true);
                player.sendMessage("§cYou don't have permission to craft this item!");
            }
        }
    }
}