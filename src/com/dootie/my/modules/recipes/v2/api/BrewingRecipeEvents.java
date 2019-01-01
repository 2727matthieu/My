package com.dootie.my.modules.recipes.v2.api;

import com.dootie.my.modules.items.MItemStack;
import org.bukkit.inventory.BrewerInventory;


public abstract class BrewingRecipeEvents {
    public BrewingRecipeEvents(){}
    
    public abstract boolean startBrew(BrewerInventory inventory);
    public abstract MItemStack consumeMaterial(MItemStack material);
    public abstract MItemStack endBrew(MItemStack output);
}
