package com.dootie.my.modules.recipes.v2.api;

import com.dootie.my.modules.items.MItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;


public class BrewingRecipe {
    private final static List<BrewingRecipe> recipes = new ArrayList<BrewingRecipe>();
    private final MItemStack material;
    private final MItemStack input;
    private final MItemStack output;
    private final boolean perfect;
    public final BrewingRecipeEvents events;

    public BrewingRecipe(MItemStack material, MItemStack input, MItemStack output, boolean perfect) {
        this.material = material;
        this.input = input;
        this.output = output;
        this.perfect = perfect;
        this.events = null;
    }
    
    public BrewingRecipe(BrewingRecipeEvents events, boolean perfect) {
        this.material = null;
        this.input = null;
        this.output = null;
        this.perfect = perfect;
        this.events = events;
    }
    
    public MItemStack getMaterial() { return this.material; }
    public MItemStack getInput() { return this.input; }
    public MItemStack getOutput() { return this.output; }
    
    public boolean isPerfect() { return perfect; }
    
    public boolean register(){ return BrewingRecipe.recipes.add(this); }

    /**
     * Get the BrewRecipe of the given recipe , will return null if no recipe is found
     * @param inventory The inventory
     * @return The recipe
     */
    @Nullable
    public static List<BrewingRecipe> getRecipe(BrewerInventory inventory){
        List<BrewingRecipe> verified = new ArrayList<BrewingRecipe>();
        
        BrewingRecipe[] candidates = recipes.stream().filter(recipe -> {
            return recipe.events != null ||
                    (inventory.getIngredient() != null &&
                    inventory.getIngredient().isSimilar(recipe.getMaterial().getItemStack()) &&
                    inventory.getIngredient().getAmount() >= recipe.getMaterial().getItemStack().getAmount());
        }).toArray((size) -> new BrewingRecipe[size]);
        
        if(candidates == null || candidates.length == 0) return null;
        for(BrewingRecipe recipe : candidates){
            for(int i = 0 ; i < 3 ; i++){
                if(inventory.getItem(i) == null) continue;
                if(inventory.getItem(i).getType() == Material.AIR) continue;
                if(recipe.events != null){
                    if(recipe.events.startBrew(inventory)){ verified.add(recipe); }
                }else{
                    if(inventory.getItem(i).equals(recipe.getInput().getItemStack())){
                        verified.add(recipe);
                    }
                }
                    
            }
        }
        return verified;
    }
}