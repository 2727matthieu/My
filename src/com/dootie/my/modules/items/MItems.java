package com.dootie.my.modules.items;

import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.modules.Module;
import com.dootie.my.modules.items.listeners.PlayerCustomItemsInteraction;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


public class MItems extends Module {
    
    public static Map<String, MItemStack> items;
    
    @Override
    public void run(){
        MItems.items = new HashMap<String, MItemStack>();
        if(FileManager.DATA_YAML.get("cbl") != null)
            MBlock.registerBlocks(FileManager.DATA_YAML.getConfigurationSection("cbl"));
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerCustomItemsInteraction(), My.plugin);
        this.registerItems();
    }
    
    private void registerItems() {
        for (File file : FileManager.ITEMS_FOLDER.listFiles()) {
            My.logger.log(Level.INFO, "MItems : pack > Loading pack {0}", file.getName());
            YamlConfiguration yamlc = YamlConfiguration.loadConfiguration(file);
            for(String item_id : yamlc.getKeys(false)){
                My.logger.log(Level.INFO, "MItems : item > Loading item {0}", item_id);
                MItemStack item = MItems.getItemFromYAML(yamlc, item_id);
                if(item == null){
                    My.logger.log(Level.INFO, "MItems : item > Can not load item {0}", item_id);
                    continue;
                }
                MItems.registerItem(item_id, item);
                My.logger.log(Level.INFO, "MItems : item > Loaded item {0}", item_id);
            }
            My.logger.log(Level.INFO, "MItems : pack > Loaded pack {0}", file.getName());
        }
    }
    
    public static void registerItem(String id, MItemStack mis){
        mis.setData("MY_ID", id);
        MItems.items.put(id, mis);
    }
    
    @Nullable
    public static MItemStack getItemFromYAML(YamlConfiguration config, String path){
        /*** From custom ***/
        String fcustom_id = config.getString(path+".custom");
        if(fcustom_id != null){
            My.devLog("Getting item : mode > 0");
            /* Item */
            My.devLog("Getting item : ID > {0}", fcustom_id);
            MItemStack mis = MItems.items.get(fcustom_id);
            if(mis == null){
                My.logger.log(Level.WARNING, "Error: Custom item {0} does not exist!", fcustom_id);
                return null;
            }
            if(mis.getItemStack() == null){
                My.logger.log(Level.WARNING, "Error: Custom item {0} does not have any ItemStack.", fcustom_id);
                return null;
            }
            /* Amount */
            if(config.get(path+".amount") != null)
                mis.getItemStack().setAmount(config.getInt(path+".amount"));
            if(mis.getItemStack().getAmount() == 0) mis.getItemStack().setAmount(1);
            
            return mis;
        }
        /*** Standart mode ***/
        /* Material */
        My.devLog("Getting item : mode > 1");
        My.devLog("Getting item : path > {0}", path);
        
        String material_id = config.getString(path+".material");
        My.devLog("Getting item : material > {0}", material_id);
        Material material = Material.getMaterial(material_id);
        
        if(material == null){
            My.logger.log(Level.WARNING, "Material {0} does not exist!", material_id);
            return null;
        }
        MItemStack mis = new MItemStack(new ItemStack(material));
        
        /* Amount */
        mis.getItemStack().setAmount(config.getInt(path+".amount"));
        if(mis.getItemStack().getAmount() == 0) mis.getItemStack().setAmount(1);
        /* Damage */
        mis.getItemStack().setDurability((short) config.getInt(path+".damage"));
        /* Enchantments */
        if (config.getStringList(path+".enchantments") != null)
            for(String enchant : config.getStringList(path+".enchantments"))
                if(enchant.contains(":"))
                    mis.getItemStack().addUnsafeEnchantment(Enchantment.getByName(enchant.split(":")[0]), Integer.parseInt(enchant.split(":")[1]));
                else mis.getItemStack().addUnsafeEnchantment(Enchantment.getByName(enchant.split(":")[0]), 1);
        
        /* Name */
        if(config.getString(path+".name") != null)
            mis.setDisplayName(config.getString(path+".name").replace("&", "§"));

        /* Lore */
        if (config.getStringList(path+".lore") != null){
            List<String> lore = new ArrayList<String>();
            for(String line : config.getStringList(path+".lore"))
                lore.add(line.replace("&", "§"));
            mis.setLore(lore);
        }

        /* Color */
        if((mis.getItemStack().getType() == Material.LEATHER_HELMET ||
                mis.getItemStack().getType() == Material.LEATHER_CHESTPLATE ||
                mis.getItemStack().getType() == Material.LEATHER_LEGGINGS ||
                mis.getItemStack().getType() == Material.LEATHER_BOOTS) && config.get(path+".dye.red") != null)
            mis.dye(config.getInt(path+".dye.red"), 
                    config.getInt(path+".dye.blue"), 
                    config.getInt(path+".dye.green"));

        /* Skull Owner */
        if(mis.getItemStack().getType() == Material.PLAYER_HEAD){
            SkullMeta sm = (SkullMeta) mis.getItemStack().getItemMeta();
            sm.setOwner((String) config.getString(path+".skull.owner"));
            mis.getItemStack().setItemMeta(sm);
        }

        /* NBT Tags */
        if (config.getStringList(path+".nbt") != null){
            for(String nbte : config.getStringList(path+".nbt")){
                My.devLog("Getting item : NBT > Creating new tag...");
                String[] split = nbte.split(",");
                Object[] o = new Object[split.length];
                for(int i = 0; i < split.length; i++){
                    String data = split[i];
                    My.devLog("Getting item : NBT > Getting element {0}", data);
                    if(data.equals("null")) continue;
                    String[] transform = data.split(":");
                    switch(transform[0]){
                        case "String": o[i] = transform[1]; break;
                        case "Integer": o[i] = Integer.valueOf(transform[1]); break;
                        case "Double": o[i] = Double.valueOf(transform[1]); break;
                        case "Long": o[i] = Long.valueOf(transform[1]); break;
                        case "Float": o[i] = Float.valueOf(transform[1]); break;
                        case "Short": o[i] = Short.valueOf(transform[1]); break;
                        case "Byte": o[i] = Byte.valueOf(transform[1]); break;
                    }
                }
                My.devLog("Getting item : NBT > Setting tag");
                mis.setNBTTag(o);
                My.devLog("Getting item : NBT > Created NBT tag");
            }
        }

        My.devLog("Getting item > ItemStack ready {0}", mis.getItemStack());
        
        /* Data */
        if(config.getConfigurationSection(path+".data") != null)
            for (String item_data : config.getConfigurationSection(path+".data").getKeys(true)) {
                Object object = config.getConfigurationSection(path+".data").get(item_data);
                mis.data.put(item_data, object);
                My.devLog("Getting item : data > "+item_data+": "+object);
            }
        
        /* Custom items */
        if(mis.data.containsKey("custom.item")){
            mis.getItemStack().setType(Material.DIAMOND_HOE);
            mis.setNBTTag(new Object[]{((int) mis.data.get("custom.item")) + 1, "Damage"});
            mis.setNBTTag(new Object[]{(byte) 1, "Unbreakable"});
            mis.setNBTTag(new Object[]{"generic.attackDamage", "AttributeModifiers", null, "AttributeName"});
            mis.setNBTTag(new Object[]{"generic.attackDamage", "AttributeModifiers", 0, "Name"});
            mis.setNBTTag(new Object[]{"mainhand", "AttributeModifiers", 0, "Slot"});
            mis.setNBTTag(new Object[]{0d, "AttributeModifiers", 0, "Amount"});
            mis.setNBTTag(new Object[]{0, "AttributeModifiers", 0, "Operation"});
            mis.setNBTTag(new Object[]{1L, "AttributeModifiers", 0, "UUIDLeast"});
            mis.setNBTTag(new Object[]{1L, "AttributeModifiers", 0, "UUIDMost"});
        }
        
        My.devLog("Getting item : verify > {0}", mis.getItemStack());
        My.devLog("Getting item : return > {0}", mis);
        return mis;
    }
}