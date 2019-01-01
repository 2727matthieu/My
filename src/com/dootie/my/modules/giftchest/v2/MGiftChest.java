package com.dootie.my.modules.giftchest.v2;


import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.modules.items.MItemStack;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.giftchest.v2.api.GiftChest;
import com.dootie.my.modules.giftchest.v2.listeners.GiftChestPlayerIteractListener;
import com.dootie.my.modules.items.MItems;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;


public class MGiftChest extends Module {
    
    @Override
    public void run() {
        Bukkit.getServer().getPluginManager().registerEvents(new GiftChestPlayerIteractListener(), My.plugin);
        for(final String key : FileManager.GIFTCHEST_YAML.getKeys(false)){
            My.logger.log(Level.INFO, "Creating giftchest {0}", key);

            GiftChest giftchest = new GiftChest(key);
            for(String material : FileManager.GIFTCHEST_YAML.getConfigurationSection(key+".items").getKeys(false)){
                MItemStack mis = MItems.getItemFromYAML(FileManager.GIFTCHEST_YAML, key+".items."+material);
                MItems.registerItem(key, mis);
                giftchest.addReward(mis);
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
    }
}