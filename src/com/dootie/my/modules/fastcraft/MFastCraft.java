package com.dootie.my.modules.fastcraft;

import com.dootie.my.My;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.fastcraft.listeners.FastCraftMenuListener;
import com.dootie.my.modules.fastcraft.listeners.PlayerClickWorkbench;
import com.dootie.my.modules.items.MItemStack;
import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.modules.recipes.v2.api.RecipeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


public class MFastCraft extends Module {
    
    public static FastCraftInventoryManager fastCraftInventoryManager = new FastCraftInventoryManager();
    
    @Override
    public void run() {
        My.logger.log(Level.INFO, "MFastCraft > Loading fastcraft...");
        My.logger.log(Level.INFO, "MFastCraft > Converting vanilla recipes...");
        for(Recipe recipe : MRecipes.vanilla_recipes){
            if(recipe instanceof ShapelessRecipe){
                ShapelessRecipe srecipe = (ShapelessRecipe) recipe;
                List<ItemStack> ingredientList = srecipe.getIngredientList();
                
                List<MItemStack> materials = new ArrayList<MItemStack>();
                for(ItemStack ing : ingredientList){
                    boolean done = false;
                    MItemStack mis = new MItemStack(ing);
                    for(MItemStack material : materials){
                        if(mis.getItemStack().isSimilar(material.getItemStack())){
                            material.getItemStack().setAmount(material.getItemStack().getAmount() + mis.getItemStack().getAmount());
                            done = true;
                        }
                    }
                    if(!done) materials.add(mis);
                }
                
                new FastRecipe(new MItemStack(srecipe.getResult()), materials).registerRecipe();
            }else if(recipe instanceof ShapedRecipe){
                ShapedRecipe srecipe = (ShapedRecipe) recipe;
                Map<Character, ItemStack> ingredients = srecipe.getIngredientMap();
                String[] shapeencr = srecipe.getShape();
                String shape = "";
                for(String part : shapeencr) shape += part;
                
                List<MItemStack> materials = new ArrayList<MItemStack>();
                for(Character chr : ingredients.keySet()){
                    ItemStack ingredient = ingredients.get(chr);
                    if(ingredient == null) continue;
                    int amount = StringUtils.countMatches(shape, chr);
                    ingredient.setAmount(amount);
                    boolean done = false;
                    for(MItemStack material : materials){
                        if(ingredient.isSimilar(material.getItemStack())){
                            material.getItemStack().setAmount(material.getItemStack().getAmount() + ingredient.getAmount());
                            done = true;
                        }
                    }
                    if(!done) materials.add(new MItemStack(ingredient));
                    
                }
                
                new FastRecipe(new MItemStack(srecipe.getResult()), materials).registerRecipe();
            }
        }
        My.logger.log(Level.INFO, "MFastCraft > Converted!");
        My.logger.log(Level.INFO, "MFastCraft > Converting custom recipes...");
        for(MyRecipe recipe : MRecipes.recipes){
            if(recipe.getType() != RecipeType.CRAFT) continue;
            List<MItemStack> materials = new ArrayList<MItemStack>();
            for(MItemStack mis : recipe.getSlots()){
                if(mis == null || mis.getItemStack().getType() == Material.AIR) continue;
                boolean done = false;
                for(MItemStack material : materials){
                    if(mis.getItemStack().isSimilar(material.getItemStack())){
                        material.getItemStack().setAmount(material.getItemStack().getAmount() + mis.getItemStack().getAmount());
                        done = true;
                    }
                }
                if(!done) materials.add(mis);
            }
            FastRecipe frecipe = new FastRecipe(recipe.getOutput(), materials);
            frecipe.setPermission(recipe.getPermission());
            frecipe.registerRecipe();
        }
        My.logger.log(Level.INFO, "MFastCraft > Converted!");
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerClickWorkbench(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new FastCraftMenuListener(), My.plugin);
        My.logger.log(Level.INFO, "MFastCraft > Loaded fastcraft!");
    }
    
}
