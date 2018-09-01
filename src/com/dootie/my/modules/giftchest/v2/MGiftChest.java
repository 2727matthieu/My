package com.dootie.my.modules.giftchest.v2;


import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.helpers.item.MyItemStack;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.giftchest.v2.api.GiftChest;
import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.helpers.item.MyItemStackYaml;
import com.dootie.my.modules.giftchest.v2.listeners.GiftChestPlayerIteractListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;


public class MGiftChest extends Module {
    
    @Override
    public void run() {
        try {
            this.generateExample();
            Bukkit.getServer().getPluginManager().registerEvents(new GiftChestPlayerIteractListener(), My.plugin);
            for(final String key : FileManager.GIFTCHEST_YAML.getKeys(false)){
                My.logger.log(Level.INFO, "Creating giftchest {0}", key);
                MyRecipe recipe = this.getMatchingRecipe(key);
                
                if(recipe == null){
                    My.logger.log(Level.WARNING, "Can't match giftchest {0} with any recipe!", key);
                    continue;
                }
                
                GiftChest giftchest = new GiftChest(recipe);
                for(String material : FileManager.GIFTCHEST_YAML.getConfigurationSection(key+".items").getKeys(false)){
                    MyItemStack mis = MyItemStackYaml.getItemFromYAML(FileManager.GIFTCHEST_YAML, key+".items."+material);
                    giftchest.addReward(mis);
                    mis.register();
                }
                if(FileManager.GIFTCHEST_YAML.getString(key+".particle.name") != null){
                    giftchest.particle = Effect.valueOf(FileManager.GIFTCHEST_YAML.getString(key+".particle.name"));
                    giftchest.particle_amount = FileManager.GIFTCHEST_YAML.getInt(key+".particle.amount");
                }else{
                    giftchest.particle = Effect.MOBSPAWNER_FLAMES;
                    giftchest.particle_amount = 8;
                }
                if(FileManager.GIFTCHEST_YAML.getString(key+".sound") != null){
                    giftchest.sound = Sound.valueOf(FileManager.GIFTCHEST_YAML.getString(key+".sound"));
                }
                giftchest.register();
                My.logger.log(Level.INFO, "Created giftchest {0}", key);
            }
        } catch (IOException ex) {
            Logger.getLogger(MGiftChest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generateExample() throws IOException{
        if(FileManager.GIFTCHEST_FILE.length() > 0) return;
        FileManager.GIFTCHEST_YAML.set("MegaChest.items.stone.material", "STONE");
        FileManager.GIFTCHEST_YAML.set("MegaChest.items.stone.amount", 4);
        FileManager.GIFTCHEST_YAML.set("MegaChest.items.stone.enchantments", Arrays.asList("DAMAGE_ALL:2"));
        FileManager.GIFTCHEST_YAML.set("MegaChest.items.diamond.material", "DIAMOND");
        FileManager.GIFTCHEST_YAML.set("MegaChest.items.diamond.amount", 1);
        FileManager.GIFTCHEST_YAML.set("MegaChest.items.diamond.name", "&4Diamond");
        FileManager.GIFTCHEST_YAML.set("MegaChest.items.diamond.enchantments", Arrays.asList("DAMAGE_ALL:4"));
        
        FileManager.GIFTCHEST_YAML.set("MegaChest.sound", "BLOCK_GRASS_BREAK");
        FileManager.GIFTCHEST_YAML.set("MegaChest.particle.name", "SMOKE");
        FileManager.GIFTCHEST_YAML.set("MegaChest.particle.amount", 10);
        FileManager.GIFTCHEST_YAML.save(FileManager.GIFTCHEST_FILE);
    }
    
    private MyRecipe getMatchingRecipe(String id){
        if(MRecipes.recipes != null)
            for(MyRecipe recipe : MRecipes.recipes)
                if(recipe.data.get("id").equals(id))
                    return recipe;
        return null;
    }
}