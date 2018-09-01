package com.dootie.my.modules.recipes.v2.helpers;


import com.dootie.my.My;
import com.dootie.my.helpers.ItemStackUtils;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BrewingRecipe {
    private static List<BrewingRecipe> recipes = new ArrayList<BrewingRecipe>();
    private ItemStack material;
    private ItemStack input;
    private ItemStack output;
    private boolean perfect;

    public BrewingRecipe(ItemStack material, ItemStack input, ItemStack output, boolean perfect) {
        this.material = material;
        this.input = input;
        this.output = output;
        this.perfect = perfect;
    }
    
    public ItemStack getMaterial() { return this.material; }
    public ItemStack getInput() { return this.input; }
    public ItemStack getOutput() { return this.output; }
    
    public boolean isPerfect() { return perfect; }
    
    public boolean register(){ return BrewingRecipe.recipes.add(this); }

    /**
     * Get the BrewRecipe of the given recipe , will return null if no recipe is found
     * @param inventory The inventory
     * @return The recipe
     */
    @Nullable
    public static BrewingRecipe getRecipe(BrewerInventory inventory){
        for(BrewingRecipe recipe : recipes){
            if(ItemStackUtils.compare(recipe.getMaterial(), inventory.getIngredient(), false)){
                for(int i = 0 ; i < 3 ; i++){
                    if(inventory.getItem(i) == null) continue;
                    if(inventory.getItem(i).getType() == Material.AIR) continue;
                    if(ItemStackUtils.compare(recipe.getInput(), inventory.getItem(i), true))
                        return recipe;
                }
            }
        }
        return null;
    }
 
    public void startBrewing(BrewerInventory inventory) {
        new BrewClock(this, inventory);
    }
 
    private class BrewClock extends BukkitRunnable {
        private BrewerInventory inventory;
        private BrewingRecipe recipe;
        private BrewingStand stand;
        private int time = 400;
     
        public BrewClock(BrewingRecipe recipe , BrewerInventory inventory) {
            this.recipe = recipe;
            this.inventory = inventory;
            this.stand = inventory.getHolder();
            this.runTaskTimerAsynchronously(My.plugin, 0L, 1L);
        }
     
        @Override
        public void run() {
            if(time == 0){
                inventory.setIngredient(new ItemStack(Material.AIR));
                for(int i = 0; i < 3 ; i ++) {
                    if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR || !ItemStackUtils.compare(recipe.getInput(), inventory.getItem(i), true)) continue;
                    ((Inventory) inventory).setItem(i, (ItemStack) this.recipe.getOutput());
                }
                cancel();
                return;
            }else if(time == 400){
                int fuel = this.stand.getFuelLevel();
                this.stand.setFuelLevel(fuel - 1);
            }
            
            if(inventory.getIngredient() == null){
                stand.setBrewingTime(400); //Reseting everything
                cancel();
                return;
            }
            
            time--;
            stand.setBrewingTime(time);
            
            this.stand.update();
        }
    }
}