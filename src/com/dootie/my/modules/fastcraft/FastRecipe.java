package com.dootie.my.modules.fastcraft;

import com.dootie.my.modules.items.MItemStack;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class FastRecipe {
    
    private static final List<FastRecipe> recipes = new ArrayList<FastRecipe>();
    private List<MItemStack> materials = new ArrayList<MItemStack>();
    private MItemStack result;
    private String permission = null;
    
    public static List<FastRecipe> getRecipes(){ return FastRecipe.recipes; }
    
    public FastRecipe(MItemStack result, List<MItemStack> materials){
        this.materials = materials;
        this.result = result;
    }
    
    public void setPermission(String permission){ this.permission = permission; }
    public boolean hasPermission(Player player){ return permission == null ? true : player.hasPermission(this.permission); }
    
    public boolean containsIngredient(MItemStack mis){
        return this.materials.stream().anyMatch((material) -> {
            return material.getItemStack().isSimilar(mis.getItemStack());
        });
    }
    
    public List<MItemStack> getMaterials(){ return this.materials; }
    public MItemStack getResult(){ return this.result; }
    
    public boolean registerRecipe(){
        if(recipes.contains(this)) return false;
        recipes.add(this);
        return true;
    }
}
