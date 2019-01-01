package com.dootie.my.modules.recipes.v2.api.helpers;

import com.dootie.my.My;
import com.dootie.my.modules.items.MItemStack;
import com.dootie.my.modules.recipes.v2.api.BrewingRecipe;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.scheduler.BukkitRunnable;


public class BrewClock extends BukkitRunnable {
    private static final List<BrewerInventory> brewTasks = new ArrayList<BrewerInventory>();
    
    private BrewingStand stand;
    private BrewingRecipe recipe;
    private int time = 400;
    private int task;
    
    public static boolean isBrewing(BrewerInventory inventory){
        return BrewClock.brewTasks.contains(inventory);
    }
    
    public BrewClock(BrewerInventory inventory) {
        this.stand = inventory.getHolder();
        this.recipe = null;
        BrewClock.brewTasks.add(inventory);
    }
    
    public void start(BrewingRecipe recipe){
        this.recipe = recipe;
        this.time = 400;
        this.runTaskTimerAsynchronously(My.plugin, 0L, 1L);
    }
    
    public void stop(){
        BrewClock.brewTasks.remove(this.stand.getInventory());
        this.cancel();
    }
    
    @Override
    public void run() {
        if(time == 0){
            time = 400;
            if(this.recipe.events == null){
                int available = this.stand.getInventory().getIngredient().getAmount();
                int needed = this.recipe.getMaterial().getItemStack().getAmount();
                int result = available - needed;
                if(result < 0) result = 0;
                this.stand.getInventory().getIngredient().setAmount(result);
            }else{
                MItemStack consume = this.recipe.events.consumeMaterial(new MItemStack(this.stand.getInventory().getIngredient()));
                this.stand.getInventory().setIngredient(consume.getItemStack());
            }
            
            for(int i = 0; i < 3 ; i ++) {
                if(this.recipe.events != null){
                    if(this.stand.getInventory().getItem(i) == null) continue;
                    MItemStack output = this.recipe.events.endBrew(new MItemStack(this.stand.getInventory().getItem(i)));
                    this.stand.getInventory().setItem(i, output.getItemStack());
                }
                else{
                    if(this.stand.getInventory().getItem(i) == null || this.stand.getInventory().getItem(i).getType() == Material.AIR || !recipe.getInput().getItemStack().equals(this.stand.getInventory().getItem(i))) continue;
                    this.stand.getInventory().setItem(i, this.recipe.getOutput().getItemStack());
                }
            }
            
            this.stand = this.stand.getInventory().getHolder();
        }
        if(time == 400){
            int fuel = this.stand.getFuelLevel();
            this.stand.setFuelLevel(fuel - 1);
        }
        
        List<BrewingRecipe> recipes = BrewingRecipe.getRecipe(this.stand.getInventory());
        if(recipes == null || !recipes.contains(this.recipe)){
            this.stop();
            return;
        }
        
        stand.setBrewingTime(--time);
        try {
            this.stand.update();
        }catch(Exception e){
            
        }
    }
}