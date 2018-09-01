package com.dootie.my.helpers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class ItemStackUtils {
    
    public static boolean compareType(ItemStack a, ItemStack b){
        return a.getType().equals(b.getType());
    }
    
    public static boolean compareAmount(ItemStack a, ItemStack b){
        return a.getAmount() == b.getAmount();
    }
    
    public static boolean compareEnchants(ItemStack a, ItemStack b){
        return (a.getEnchantments() != null && b.getEnchantments() != null) ? 
                a.getEnchantments().equals(b.getEnchantments()):
                a.getEnchantments() == null && b.getEnchantments() == null;
    }
    
    public static boolean compareName(ItemStack a, ItemStack b){
        return (a.getItemMeta() != null && b.getItemMeta() != null) ?
                    ((a.getItemMeta().getDisplayName() == null || b.getItemMeta().getDisplayName() == null) ?
                        (a.getItemMeta().getDisplayName() == null && b.getItemMeta().getDisplayName() == null):
                        ((!a.getItemMeta().getDisplayName().equals("") && !b.getItemMeta().getDisplayName().equals("")) ?
                            a.getItemMeta().getDisplayName().equals(b.getItemMeta().getDisplayName()):
                            a.getItemMeta().getDisplayName().equals("") && b.getItemMeta().getDisplayName().equals(""))
                ):a.getItemMeta() == null && b.getItemMeta() == null;
    }
    
    public static boolean compareLore(ItemStack a, ItemStack b){
        return (a.getItemMeta() == null || b.getItemMeta() == null) ?
                    a.getItemMeta() == null && b.getItemMeta() == null:
                    ((a.getItemMeta().getLore() != null && b.getItemMeta().getLore() != null) ?
                        a.getItemMeta().getLore().equals(b.getItemMeta().getLore()):
                        a.getItemMeta().getLore() == null && b.getItemMeta().getLore() == null);
    }
    
    public static boolean compare(ItemStack a, ItemStack b, boolean amount){
        if(a == null || b == null) return false;
        return ItemStackUtils.compareType(a, b) &&
                (ItemStackUtils.compareAmount(a, b) || !amount) &&
                ItemStackUtils.compareEnchants(a, b) &&
                ItemStackUtils.compareName(a, b) &&
                ItemStackUtils.compareLore(a, b);
    }
    
    public static ItemStack damageTool(ItemStack tool) {
        tool.setDurability((short)(tool.getDurability() + 1));
        if (tool.getDurability() == tool.getType().getMaxDurability()) {
            tool.setDurability((short)0);
            tool.setType(Material.AIR);
        }
        return tool;
    }
}