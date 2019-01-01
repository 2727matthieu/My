package com.dootie.my.modules.recipes.v2.listeners;


import com.dootie.my.My;
import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.modules.items.MItemStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;


public class PrepareItemCraftListener implements Listener {
    
    @EventHandler
    public void PrepareItemCraft(PrepareItemCraftEvent e) {
        if(e.getRecipe() == null){
            My.devLog("precraft > No recipes found. Code 1.");
            return;
        }
        CraftingInventory inventory = e.getInventory();
        
        MyRecipe[] recipes = MRecipes.recipes.stream().filter((recipe) -> e.getRecipe().getResult().equals(recipe.getOutput().getItemStack())).toArray((size) -> new MyRecipe[size]);
        
        if(recipes == null || recipes.length == 0) return;
        
        for(MyRecipe recipe : recipes){           
            My.devLog("precraft > Checking recipe.");
            boolean next = false;
            for(int i = 0; i < 9 ; i++){
                if(next) continue;
                My.devLog("precraft > Checking slot {0}", i+1);
                MItemStack miri = new MItemStack(inventory.getContents()[i+1]);
                if(recipe.getSlots()[i] == null && miri.getItemStack().getType() == Material.AIR) continue;
                MItemStack mirs = recipe.getSlots()[i].clone();
                if(mirs.getItemStack() == null || mirs.getItemStack().getType() == Material.AIR){
                    My.devLog("precraft > This slot is empty or air in configuration.");
                    if(i == 8){
                        My.devLog("precraft > Requeriments meet: can craft.");
                        return;
                    }
                    continue;
                }

                if(!miri.getItemStack().isSimilar(mirs.getItemStack())){
                    My.devLog("precraft > Requeriments are not meet: wrong ingredients:");
                    My.devLog("precraft > Found: "+miri.getItemStack());
                    My.devLog("precraft > Expected: "+mirs.getItemStack());
                    next = true;
                }
                if(miri.getItemStack().getAmount() < mirs.getItemStack().getAmount()){
                    My.devLog("precraft > Requeriments are not meet: wrong amount.");
                    next = true;
                }
                if(i == 8){
                    My.devLog("precraft > Requeriments meet: can craft.");
                    return;
                }
            }
        }
        inventory.setResult(new ItemStack(Material.AIR, 0));
        My.devLog("precraft > Requeriments not meet: can not craft.");
    }
}