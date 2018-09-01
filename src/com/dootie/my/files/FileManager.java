package com.dootie.my.files;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {
    
    final private static File FOLDER = new File("plugins/my");
    public static File CONFIG_FILE;
    public static YamlConfiguration CONFIG_YAML;
    public static File RECIPES_FILE;
    public static YamlConfiguration RECIPES_YAML;
    public static File COMMANDS_FILE;
    public static YamlConfiguration COMMANDS_YAML;
    public static File GIFTCHEST_FILE;
    public static YamlConfiguration GIFTCHEST_YAML;
    public static File WEBSERVICE_FILE;
    public static YamlConfiguration WEBSERVICE_YAML;
    
    static {
        try {
            FOLDER.mkdir();
            CONFIG_FILE = new File(FOLDER.getPath()+"/config.yml");
            if(!CONFIG_FILE.exists()) CONFIG_FILE.createNewFile();
            CONFIG_YAML = YamlConfiguration.loadConfiguration(CONFIG_FILE);
            
            RECIPES_FILE = new File(FOLDER.getPath()+"/recipes.yml");
            if(!RECIPES_FILE.exists()) RECIPES_FILE.createNewFile();
            RECIPES_YAML = YamlConfiguration.loadConfiguration(RECIPES_FILE);
            
            COMMANDS_FILE = new File(FOLDER.getPath()+"/commands.yml");
            if(!COMMANDS_FILE.exists()) COMMANDS_FILE.createNewFile();
            COMMANDS_YAML = YamlConfiguration.loadConfiguration(COMMANDS_FILE);
            
            GIFTCHEST_FILE = new File(FOLDER.getPath()+"/giftchest.yml");
            if(!GIFTCHEST_FILE.exists()) GIFTCHEST_FILE.createNewFile();
            GIFTCHEST_YAML = YamlConfiguration.loadConfiguration(GIFTCHEST_FILE);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}