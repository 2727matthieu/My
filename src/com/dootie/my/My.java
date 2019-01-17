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
import com.dootie.my.modules.fastcraft.MFastCraft;
import com.dootie.my.modules.giftchest.v2.MGiftChest;
import com.dootie.my.modules.itemeffects.MItemEffects;
import com.dootie.my.modules.items.MItems;
import com.dootie.my.modules.uncrafter.MUncrafter;
import org.bukkit.entity.Player;


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
            My.plugin = this;
            My.logger = this.getLogger();
            
            My.devLog("core > Starting...");
            
            this.generateConfig();
            
            My.devlog = FileManager.CONFIG_YAML.getBoolean("dev");
            
            My.devLog("modules > Loading modules...");
            Module.modules.add(new MItems());
            if(FileManager.CONFIG_YAML.getBoolean("modules.recipes")) Module.modules.add(new MRecipes());
            if(FileManager.CONFIG_YAML.getBoolean("modules.commands")) Module.modules.add(new MCommand());
            if(FileManager.CONFIG_YAML.getBoolean("modules.giftchests")) Module.modules.add(new MGiftChest());
            if(FileManager.CONFIG_YAML.getBoolean("modules.fastcraft")) Module.modules.add(new MFastCraft());
            if(FileManager.CONFIG_YAML.getBoolean("modules.uncrafter.enabled")) Module.modules.add(new MUncrafter());
            if(FileManager.CONFIG_YAML.getBoolean("modules.itemeffects")) Module.modules.add(new MItemEffects());
            My.devLog("modules > Modules loaded.");
            My.devLog("modules > Executing modules...");
            Module.executeModules();
            My.devLog("modules > Modules executed.");
            My.devLog("core > Done!");
            if(FileManager.CONFIG_YAML.getBoolean("tutorial")){
                My.logger.log(Level.SEVERE, "------------------------------------------------");
                My.logger.log(Level.SEVERE, "");
                My.logger.log(Level.SEVERE, "Looks like it's the first time using this plugin");
                My.logger.log(Level.SEVERE, "");
                My.logger.log(Level.SEVERE, "Remember to read the page!");
                My.logger.log(Level.SEVERE, "https://www.spigotmc.org/resources/5176/");
                My.logger.log(Level.SEVERE, "");
                My.logger.log(Level.SEVERE, "Disable this message changing the config file");
                My.logger.log(Level.SEVERE, "From 'tutorial: true' to 'tutorial: false'");
                My.logger.log(Level.SEVERE, "");
                My.logger.log(Level.SEVERE, "------------------------------------------------");
            }
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
        if(FileManager.CONFIG_YAML.get("modules.uncrafter.enabled") == null)
            FileManager.CONFIG_YAML.set("modules.uncrafter.enabled", true);
        if(FileManager.CONFIG_YAML.get("modules.fastcraft") == null)
            FileManager.CONFIG_YAML.set("modules.fastcraft", true);
        if(FileManager.CONFIG_YAML.get("modules.itemeffects") == null)
            FileManager.CONFIG_YAML.set("modules.itemeffects", true);
        if(FileManager.CONFIG_YAML.get("tutorial") == null)
            FileManager.CONFIG_YAML.set("tutorial", true);
        if(FileManager.CONFIG_YAML.get("dev") == null)
            FileManager.CONFIG_YAML.set("dev", false);
        FileManager.CONFIG_YAML.save(FileManager.CONFIG_FILE);
        My.devLog("files : config > Check completed.");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, String[] args) {
        Player player = (Player) sender;
        
        if(args.length == 0) args = new String[]{"help"};
        switch(args[0].toLowerCase()){
            case "getitem":
                if(player.hasPermission("my.getitem")){
                    if(args.length != 2){
                        player.sendMessage("§c[My] Bad usage.");
                        break;
                    }
                    if(MItems.items.get(args[1]) != null){
                        player.getInventory().addItem(MItems.items.get(args[1]).getItemStack());
                        player.sendMessage("§a[My] You got the item!");
                    }else{
                        player.sendMessage("§c[My] Item not found.");
                    }
                    
                }else{
                    player.sendMessage("§c[My] You don't have permission!");
                }
                break;
            case "help":
                player.sendMessage("§9Avaiable commands"
                        + "\n§r/my getitem §b<item id>§r §7Get a custom item by id.§r");
                break;
            default:
                player.sendMessage("§c[My] Command not found.");
                break;
        }
        return true;
    }
    
    
    
}