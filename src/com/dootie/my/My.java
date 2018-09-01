package com.dootie.my;



import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.dootie.my.files.FileManager;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.modules.commands.v2.MCommand;
import com.dootie.my.modules.giftchest.v2.MGiftChest;
import com.dootie.my.modules.itemeffects.MItemEffects;
import com.dootie.my.modules.uncrafter.MUncrafter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;


public class My extends JavaPlugin implements Listener {
    
    public static Plugin plugin;
    public static Logger logger;
    private static boolean devlog = false;
    private final String welcome = "Welcome to my plugin source code, happy learning ;)";
    
    public static void devLog(String log){ if(My.devlog) logger.log(Level.INFO, log); }
    public static void devLog(String log, Object object){ if(My.devlog) logger.log(Level.INFO, log, object); }
    
    @Override
    public void onEnable() {
        try {
            My.devLog("core > Starting...");
            
            My.plugin = this;
            My.logger = this.getLogger();
            this.generateConfig();
            
            My.devlog = FileManager.CONFIG_YAML.getBoolean("dev");
            
            My.devLog("modules > Loading modules...");
            if(FileManager.CONFIG_YAML.getBoolean("modules.recipes")) Module.modules.add(new MRecipes());
            if(FileManager.CONFIG_YAML.getBoolean("modules.commands")) Module.modules.add(new MCommand());
            if(FileManager.CONFIG_YAML.getBoolean("modules.giftchests")) Module.modules.add(new MGiftChest());
            if(FileManager.CONFIG_YAML.getBoolean("modules.uncrafter.enabled")) Module.modules.add(new MUncrafter());
            if(FileManager.CONFIG_YAML.getBoolean("modules.itemeffects")) Module.modules.add(new MItemEffects());
            My.devLog("modules > Modules loaded.");
            My.devLog("modules > Executing modules...");
            Module.executeModules();
            My.devLog("modules > Modules executed.");
            My.devLog("core > Done!");
        } catch (IOException ex) {
            Logger.getLogger(My.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void generateConfig() throws IOException {
        My.devLog("files : config > Checking configuration...");
        if(FileManager.CONFIG_YAML.get("modules.recipes") == null)
            FileManager.CONFIG_YAML.set("modules.recipes", true);
        if(FileManager.CONFIG_YAML.get("modules.commands") == null)
            FileManager.CONFIG_YAML.set("modules.commands", true);
        if(FileManager.CONFIG_YAML.get("modules.giftchests") == null)
            FileManager.CONFIG_YAML.set("modules.giftchests", true);
        if(FileManager.CONFIG_YAML.get("modules.uncrafter") == null)
            FileManager.CONFIG_YAML.set("modules.uncrafter", true);
        if(FileManager.CONFIG_YAML.get("modules.itemeffects") == null)
            FileManager.CONFIG_YAML.set("modules.itemeffects", true);
        if(FileManager.CONFIG_YAML.get("dev") == null)
            FileManager.CONFIG_YAML.set("dev", false);
        FileManager.CONFIG_YAML.save(FileManager.CONFIG_FILE);
        My.devLog("files : config > Check completed.");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        sender.sendMessage("§9MY §ris working!");
        return false;
    }
    
    
    
}