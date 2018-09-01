package com.dootie.my.modules.recipes.v2;

import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.modules.recipes.v2.api.RecipeType;
import com.dootie.my.helpers.item.MyItemStackYaml;
import com.dootie.my.modules.recipes.v2.listeners.CraftItemListener;
import com.dootie.my.modules.recipes.v2.listeners.PrepareItemCraftListener;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.recipes.v2.listeners.PotionBrewAllower;
import com.dootie.my.modules.recipes.v2.listeners.PotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;


public class MRecipes extends Module{
    
    public static List<MyRecipe> recipes;
    
    @Override
    public void run(){
        try {
            MRecipes.recipes = new ArrayList<MyRecipe>();
            this.generateExamples();
            this.registerEvents();
            this.unregisterRecipes();
            this.registerRecipes();
        } catch (IOException ex) {
            Logger.getLogger(MRecipes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generateExamples() throws IOException{
        if(FileManager.RECIPES_FILE.length() > 0) return;
        FileManager.RECIPES_YAML.set("MegaStone.type", "craft");
        FileManager.RECIPES_YAML.set("MegaStone.shaped", false);
        FileManager.RECIPES_YAML.set("MegaStone.permission", "my.stone");
        FileManager.RECIPES_YAML.set("MegaStone.slot.1.material", "COBBLESTONE");
        FileManager.RECIPES_YAML.set("MegaStone.output.material", "STONE");
        FileManager.RECIPES_YAML.set("MegaStone.output.amount", 1);
        FileManager.RECIPES_YAML.set("MegaChest.type", "craft");
        FileManager.RECIPES_YAML.set("MegaChest.shaped", false);
        FileManager.RECIPES_YAML.set("MegaChest.permission", "my.chest");
        FileManager.RECIPES_YAML.set("MegaChest.slot.1.material", "GOLD_BLOCK");
        FileManager.RECIPES_YAML.set("MegaChest.slot.1.material", "CHEST");
        FileManager.RECIPES_YAML.set("MegaChest.output.material", "SKULL_ITEM");
        FileManager.RECIPES_YAML.set("MegaChest.output.amount", 1);
        FileManager.RECIPES_YAML.set("MegaChest.output.damage", 3);
        FileManager.RECIPES_YAML.set("MegaChest.output.name", "Mega&6Chest");
        FileManager.RECIPES_YAML.set("MegaChest.output.skull.owner", "MHF_Chest");
        FileManager.RECIPES_YAML.save(FileManager.RECIPES_FILE);
    }
    
    private void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(new CraftItemListener(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PrepareItemCraftListener(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PotionBrewAllower(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PotionListener(), My.plugin);
    }
    
    private void unregisterRecipes() {
        new RecipeUnregister(FileManager.RECIPES_YAML.getStringList("remove-vanilla")).unregister();
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
            recipe.data.put("id", id);
            /* Shaped */
            recipe.setShaped(FileManager.RECIPES_YAML.getBoolean(id + ".shaped"));
            /* Material */
            if(FileManager.RECIPES_YAML.getConfigurationSection(id + ".material") != null)
                recipe.setMaterial(MyItemStackYaml.getItemFromYAML(FileManager.RECIPES_YAML, id + ".material"));
            /* Input */
            if(FileManager.RECIPES_YAML.getConfigurationSection(id + ".input") != null)
                recipe.setInput(MyItemStackYaml.getItemFromYAML(FileManager.RECIPES_YAML, id + ".input"));
            /* Experience */
            if(FileManager.RECIPES_YAML.getDouble(id + ".experience") != 0)
                recipe.setExperience((float) FileManager.RECIPES_YAML.getDouble(id + ".experience"));
            /* Slots */
            boolean e_s = false;
            for(int i = 1; i < 10 && !e_s; i++){
                String slot = FileManager.RECIPES_YAML.getString(id + ".slot."+i);
                if(slot == null) continue;
                recipe.setSlot(i-1, MyItemStackYaml.getItemFromYAML(FileManager.RECIPES_YAML,id+".slot."+i));
            }
            if(e_s) continue;
            /* Permission */
            recipe.setPermission(FileManager.RECIPES_YAML.getString(id + ".permission"));
            /* Output */
            recipe.setOutput(MyItemStackYaml.getItemFromYAML(FileManager.RECIPES_YAML,id+".output"));
            if (recipe.register()) {
                MRecipes.recipes.add(recipe);
                recipe.getOutput().register();
                My.logger.log(Level.INFO, "Created recipe {0}", id);
            } else
                My.logger.log(Level.WARNING, "Uknown error creating recipe {0}", id);
        }
    }
}