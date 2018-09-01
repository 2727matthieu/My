package com.dootie.my.modules.recipes.v2.listeners;


import com.dootie.my.My;
import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.helpers.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;


public class PrepareItemCraftListener implements Listener {
    
    @EventHandler
    public void PrepareItemCraft(PrepareItemCraftEvent e) {
        CraftingInventory inventory = e.getInventory();
        for(MyRecipe recipe : MRecipes.recipes){
            
            if(e.getRecipe() == null || recipe == null){
                My.devLog("precraft > null. next");
                continue;
            }
            
            if(ItemStackUtils.compare(e.getRecipe().getResult(), recipe.getOutput(), true))
                for(ItemStack mis_r:recipe.getSlots()){
                    if(mis_r == null) continue;
                    if(!this.contains(inventory.getContents(), mis_r))
                        inventory.setResult(new ItemStack(Material.AIR));
                }
        }
    }
    
    private boolean contains(ItemStack[] isa, ItemStack is){
        for(ItemStack ist : isa){
            if(is == null) return true;
            if(ist == null) continue;
            if(ItemStackUtils.compare(ist, is, false)) return true;
        }
        return false;
    }
}