package com.dootie.my.modules.commands.v2.api;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;


public abstract class AbstractCommand extends BukkitCommand {
    
    public String name;
    
    public AbstractCommand(String name, String description, String usage, String permission, List<String> aliases) {
        super(name);
        this.name = name;
        if(description != null) this.description = description;
        if(permission != null) this.setPermission(permission);
        if(aliases != null) this.setAliases(aliases);
        this.usageMessage = usage;
    }
    
    public void register(Server bukkitServer) {
        try {
            String package_base = Bukkit.getServer().getClass().getPackage().getName();
            
            Class<?> craftServerClass = Class.forName(package_base+".CraftServer");
            Class<?> simpleCommandMap = Class.forName("org.bukkit.command.SimpleCommandMap");
            
            Object craftServer = craftServerClass.cast(bukkitServer);
            Object commandMap = simpleCommandMap.cast(craftServerClass.getMethod("getCommandMap").invoke(craftServer));
            simpleCommandMap.getMethod("register", String.class, Command.class).invoke(commandMap, this.name, this);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AbstractCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public abstract void run(CommandSender sender, String alias, String[] args);
    public abstract void noPermission(CommandSender sender);
    
    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(this.getPermission() != null){
            if (!sender.hasPermission(this.getPermission())) {
                this.noPermission(sender);
                return true;
            }
        }
        this.run(sender, alias, args);
        return true;
    }
}