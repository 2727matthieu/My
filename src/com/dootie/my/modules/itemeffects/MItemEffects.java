package com.dootie.my.modules.itemeffects;

import com.dootie.my.My;
import com.dootie.my.modules.itemeffects.listeners.BlockBreakListener;
import com.dootie.my.modules.itemeffects.listeners.EntityDamageByEntityListener;
import com.dootie.my.modules.itemeffects.listeners.PlayerInteractListener;
import com.dootie.my.modules.itemeffects.listeners.PlayerItemConsumeListener;
import com.dootie.my.modules.Module;
import java.util.logging.Level;
import org.bukkit.Bukkit;


public class MItemEffects extends Module {

    @Override
    public void run() {
        My.logger.log(Level.INFO, "Enabling ItemEffects Module");
        this.registerEvents();
        My.logger.log(Level.INFO, "Enabled!");
    }
    
    private void registerEvents(){
        Bukkit.getServer().getPluginManager().registerEvents(new BlockBreakListener(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), My.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerItemConsumeListener(), My.plugin);
    }
}
