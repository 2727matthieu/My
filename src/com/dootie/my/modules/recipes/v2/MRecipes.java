package com.dootie.my.modules.recipes.v2;

import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.modules.recipes.v2.api.RecipeType;
import com.dootie.my.modules.recipes.v2.listeners.PrepareItemCraftListener;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.items.MItemStack;
import com.dootie.my.modules.items.MItems;
import com.dootie.my.modules.recipes.v2.listeners.CraftInteractions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;


public class MRecipes extends Module{
    
    public static List<Recipe> vanilla_recipes;
    public static List<MyRecipe> recipes;
    
    @Override
    public void run(){
        MRecipes.vanilla_recipes = new ArrayList<Recipe>();
        MRecipes.recipes = new ArrayList<MyRecipe>();
        this.registerEvents();
        this.unregisterRecipes();
        this.saveVanillaRecipes();
        this.registerRecipes();
    }
    
    private void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(new PrepareItemCraftListener(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new CraftInteractions(), My.plugin);
    }
    
    private void unregisterRecipes() {
        new RecipeUnregister(FileManager.RECIPES_YAML.getStringList("remove-vanilla")).unregister();
    }
    
    private void saveVanillaRecipes(){
        My.logger.log(Level.INFO, "MRecipes : vanilla > Saving vanilla recipes...");
        Iterator ri = Bukkit.getServer().recipeIterator();
        while (ri.hasNext()) {
            Recipe recipe = (Recipe)ri.next();
            MRecipes.vanilla_recipes.add(recipe);
        }
        My.logger.log(Level.INFO, "MRecipes : vanilla > Saved!");
    }

    private void registerRecipes() {
        for (String id : FileManager.RECIPES_YAML.getKeys(false)) {
            if(id.equals("NAME") || id.equals("remove-vanilla")) continue;
            My.logger.log(Level.INFO, "Creating recipe {0}", id);
            MyRecipe recipe;
            /* Recipe Type */
            if (FileManager.RECIPES_YAML.getString(id + ".type") == null){
                My.logger.log(Level.WARNING, "Error creating recipe {0}: Undefined recipe type", id);
                continue;
            }
            try {
                recipe = new MyRecipe(id,RecipeType.valueOf(FileManager.RECIPES_YAML.getString(id + ".type").toUpperCase()));
            }catch(NullPointerException ex){
                My.logger.log(Level.WARNING, "Error creating recipe {0}: Uknown recipe type {1}",
                    new Object[]{id, FileManager.RECIPES_YAML.getString(id + ".type")});
                continue;
            }
            /* Shaped */
            recipe.setShaped(FileManager.RECIPES_YAML.getBoolean(id + ".shaped"));
            /* Material */
            if(FileManager.RECIPES_YAML.getConfigurationSection(id + ".material") != null)
                recipe.setMaterial(MItems.getItemFromYAML(FileManager.RECIPES_YAML, id + ".material"));
            /* Input */
            if(FileManager.RECIPES_YAML.getConfigurationSection(id + ".input") != null)
                recipe.setInput(MItems.getItemFromYAML(FileManager.RECIPES_YAML, id + ".input"));
            /* Experience */
            if(FileManager.RECIPES_YAML.getDouble(id + ".experience") != 0)
                recipe.setExperience((float) FileManager.RECIPES_YAML.getDouble(id + ".experience"));
            /* Slots */
            boolean e_s = false;
            for(int i = 1; i < 10 && !e_s; i++){
                String slot = FileManager.RECIPES_YAML.getString(id + ".slot."+i);
                if(slot == null) continue;
                recipe.setSlot(i-1, MItems.getItemFromYAML(FileManager.RECIPES_YAML,id+".slot."+i));
            }
            if(e_s) continue;
            /* Permission */
            recipe.setPermission(FileManager.RECIPES_YAML.getString(id + ".permission"));
            /* Output */
            MItemStack output = MItems.getItemFromYAML(FileManager.RECIPES_YAML,id+".output");
            recipe.setOutput(output);
            if (recipe.register()) {
                MRecipes.recipes.add(recipe);
                My.logger.log(Level.INFO, "Created recipe {0}", id);
            } else
                My.logger.log(Level.WARNING, "Recipe {0} creation failed!", id);
        }
    }
}