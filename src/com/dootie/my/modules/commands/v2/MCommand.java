package com.dootie.my.modules.commands.v2;

import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.commands.v2.api.AbstractCommand;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class MCommand extends Module {

    @Override
    public void run() {
        try {
            this.generateExample(); 
            this.registerCommands();
        } catch (IOException ex) {
            Logger.getLogger(MCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generateExample() throws IOException{
        if(FileManager.COMMANDS_FILE.length() > 0) return;
        FileManager.COMMANDS_YAML.set("sendinfo.arguments", -1);
        FileManager.COMMANDS_YAML.set("sendinfo.description", "Send an informative message");
        FileManager.COMMANDS_YAML.set("sendinfo.usage", "/sendinfo <message>");
        FileManager.COMMANDS_YAML.set("sendinfo.permission", "my.sendinfo");
        FileManager.COMMANDS_YAML.set("sendinfo.aliases", Arrays.asList("si"));
        FileManager.COMMANDS_YAML.set("sendinfo.commands", Arrays.asList("say [§cInfo§r] {-1}", "server:tell {sender.name} You send information ;)"));
        FileManager.COMMANDS_YAML.save(FileManager.COMMANDS_FILE);
    }
    
    private void registerCommands(){
        Set<String> keys = FileManager.COMMANDS_YAML.getKeys(false);
        for(final String key : keys){
            My.logger.log(Level.INFO, "Creating command {0}", key);
            (new AbstractCommand(key,
                            FileManager.COMMANDS_YAML.getString(key+".description"),
                            FileManager.COMMANDS_YAML.getString(key+".usage"),
                            FileManager.COMMANDS_YAML.getString(key+".permission"),
                            FileManager.COMMANDS_YAML.getStringList(key+".aliases")){
                                
                                int nargs = FileManager.COMMANDS_YAML.getInt(key+".arguments");
                                List<String> list;
                                @Override
                                public void run(CommandSender sender, String alias, String[] args) {
                                    if(this.nargs != args.length && this.nargs != -1){
                                        sender.sendMessage(ChatColor.RED + "Usage: "+this.usageMessage);
                                        return;
                                    }
                                    
                                    this.list = FileManager.COMMANDS_YAML.getStringList(key+".commands");
                                    List<String> prepared = new ArrayList<String>();
                                    for(String command : this.list){
                                        try{
                                            for(int i = 0; i < args.length; i++)
                                                command = command.replace("{"+i+"}", args[i]);

                                            String repl = "";
                                            for(String arg : args) repl += arg+" ";
                                            command = command.replace("{-1}", repl);
                                            prepared.add(command);
                                        }catch(IndexOutOfBoundsException ex){
                                            sender.sendMessage(ChatColor.RED + "Usage: "+this.usageMessage);
                                            return;
                                        }
                                    }
                                    
                                    for(String prep : prepared)
                                        if(prep.startsWith("server:"))
                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), prep.replace("server:", "").replace("{sender.name}", sender.getName()));
                                        else
                                            Bukkit.getServer().dispatchCommand(sender, prep);
                                }

                                @Override
                                public void noPermission(CommandSender sender) {
                                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                                }
                                
                            }).register(My.plugin.getServer());
            My.logger.log(Level.INFO, "Created command {0}", key);
        }
    }
}