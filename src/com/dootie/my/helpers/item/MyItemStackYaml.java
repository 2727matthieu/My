package com.dootie.my.helpers.item;


import com.dootie.my.My;
import com.dootie.my.files.FileManager;
import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.helpers.item.MyItemStack;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


public class MyItemStackYaml {
    public static MyItemStack getItemFromYAML(YamlConfiguration config, String path){
        /*
        Alternative mode
        */
        if(config.getString(path+".fromOutputRecipe") != null)
            for(MyRecipe recipe : MRecipes.recipes)
                if(recipe.data.get("id").equals(config.getString(path+".fromOutputRecipe")))
                    return recipe.getOutput();
        /*
        Standart mode
        */
        /* Material */
        My.devLog("Getting item : mode > 1");
        My.devLog("Getting item : path > {0}", path);
        
        MyItemStack mis = new MyItemStack(new ItemStack(Material.getMaterial(config.getString(path+".material"))));
        /* Amount */
        mis.setAmount(config.getInt(path+".amount"));
        if(mis.getAmount() == 0) mis.setAmount(1);
        /* Damage */
        mis.setDurability((short) config.getInt(path+".damage"));
        /* Enchantments */
        if (FileManager.RECIPES_YAML.getStringList(path+".enchantments") != null)
            for(String enchant : FileManager.RECIPES_YAML.getStringList(path+".enchantments"))
                if(enchant.contains(":"))
                    mis.addUnsafeEnchantment(Enchantment.getByName(enchant.split(":")[0]), Integer.parseInt(enchant.split(":")[1]));
                else mis.addUnsafeEnchantment(Enchantment.getByName(enchant.split(":")[0]), 1);
        
        /* Name */
        if(config.getString(path+".name") != null)
            mis.setDisplayName(config.getString(path+".name").replace("&", "§"));

        /* Lore */
        if (FileManager.RECIPES_YAML.getStringList(path+".lore") != null){
            List<String> lore = new ArrayList<String>();
            for(String line : FileManager.RECIPES_YAML.getStringList(path+".lore"))
                lore.add(line.replace("&", "§"));
            mis.setLore(lore);
        }

        /* Color */
        if((mis.getType() == Material.LEATHER_HELMET ||
                mis.getType() == Material.LEATHER_CHESTPLATE ||
                mis.getType() == Material.LEATHER_LEGGINGS ||
                mis.getType() == Material.LEATHER_BOOTS) && config.get(path+".dye.red") != null)
            mis.dye(config.getInt(path+".dye.red"), 
                    config.getInt(path+".dye.blue"), 
                    config.getInt(path+".dye.green"));

        /* Skull Owner */
        if(mis.getType() == Material.SKULL_ITEM){
            SkullMeta sm = (SkullMeta) mis.getItemMeta();
            sm.setOwner((String) config.getString(path+".skull.owner"));
            mis.setItemMeta(sm);
        }

        /* NBT Tags */
        String pack = getServer().getClass().getPackage().getName();
        String version = pack.substring(pack.lastIndexOf('.') + 1);
        String r_package_craft = "org.bukkit.craftbukkit."+version;
        String r_package_serv = "net.minecraft.server."+version;
        try {
            Class<?> craftItemStack = Class.forName(r_package_craft+".inventory.CraftItemStack");
            Class<?> itemStack = Class.forName(r_package_serv+".ItemStack");
            Class<?> nbtTagCompound = Class.forName(r_package_serv+".NBTTagCompound");
            Class<?> nbtTagList = Class.forName(r_package_serv+".NBTTagList");
            Class<?> nbtTagBase = Class.forName(r_package_serv+".NBTBase");
            Class<?> nbtTagString = Class.forName(r_package_serv+".NBTTagString");
            Class<?> nbtTagDouble = Class.forName(r_package_serv+".NBTTagDouble");
            Object nmsStack = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, mis);
            Object compound = ((boolean) itemStack.getMethod("hasTag").invoke(nmsStack)) ?
                    itemStack.getMethod("getTag").invoke(nmsStack) :
                    nbtTagCompound.newInstance();
            Object modifiers = nbtTagList.newInstance();
            if(config.getStringList(path+".NBT") != null){
                for(String list : config.getStringList(path+".NBT")){
                    String[] nbtdata = list.split(":");
                    Object icompound = nbtTagCompound.newInstance();
                    for(int i = 0;i < nbtdata.length && nbtdata.length % 2 == 0; i = i + 2){
                        if(MyItemStackYaml.isInteger(nbtdata[i+1]))
                            nbtTagCompound.getMethod("set", String.class, nbtTagBase).invoke(icompound, nbtdata[i], nbtTagDouble.getConstructor(Double.class).newInstance(Double.parseDouble(nbtdata[i+1])));
                        else
                            nbtTagCompound.getMethod("set", String.class, nbtTagBase).invoke(icompound, nbtdata[i], nbtTagString.getConstructor(String.class).newInstance(nbtdata[i+1]));
                    }
                    nbtTagList.getMethod("add", nbtTagCompound).invoke(modifiers, icompound);
                }
                nbtTagCompound.getMethod("set", String.class, nbtTagBase).invoke(compound, "AttributeModifiers", modifiers);
                itemStack.getMethod("setTag", nbtTagCompound).invoke(nmsStack, compound);
                mis = new MyItemStack((ItemStack) craftItemStack.getMethod("asBukkitCopy", itemStack).invoke(null, nmsStack));
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            Logger.getLogger(MyItemStackYaml.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* Data */
        Map<String, Object> data = new HashMap<String, Object>();
        if(FileManager.RECIPES_YAML.getConfigurationSection(path+".data") != null)
            for (String item_data : FileManager.RECIPES_YAML.getConfigurationSection(path+".data").getKeys(true)) {
                Object object = FileManager.RECIPES_YAML.getConfigurationSection(path+".data").get(item_data);
                data.put(item_data, object);
            }
        mis.data = data;
        My.devLog("Getting item : return > {0}", mis);
        return mis;
    }
    
    private static boolean isInteger(String string){
        try {
            int a = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
