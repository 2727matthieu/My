package com.dootie.my.modules.uncrafter;

import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.uncrafter.listeners.InventoryClickListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;


public class MUncrafter extends Module {
    
    public static String uncraftPermission;
    
    @Override
    public void run() {
        try {
            this.generateConfig();
            this.registerListeners();
            MUncrafter.uncraftPermission = FileManager.CONFIG_YAML.getString("modules.uncrafter.permission");
        } catch (IOException ex) {
            Logger.getLogger(MUncrafter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generateConfig() throws IOException{
        if(!FileManager.CONFIG_YAML.contains("modules.uncrafter.enabled"))
            FileManager.CONFIG_YAML.set("modules.uncrafter.enabled", true);
        if(!FileManager.CONFIG_YAML.contains("modules.uncrafter.permission"))
            FileManager.CONFIG_YAML.set("modules.uncrafter.permission", "my.uncraft");
        FileManager.CONFIG_YAML.save(FileManager.CONFIG_FILE);
    }
    
    private void registerListeners(){
        if(FileManager.CONFIG_YAML.getBoolean("modules.uncrafter.enabled"))
            Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), My.plugin);
    }
}