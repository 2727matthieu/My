package com.dootie.my.helpers.item;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;


public class MyItemStack extends ItemStack{
    
    private static final List<MyItemStack> myItemStacks = new ArrayList<MyItemStack>();
    
    public Map<String, Object> data = new HashMap<String, Object>();
    
    public MyItemStack(ItemStack itemStack) {
        super(itemStack);
    }
    
    public void setDisplayName(String name){
        ItemMeta im = this.getItemMeta();
        im.setDisplayName(name);
        this.setItemMeta(im);
    }
    
    public void setLore(List<String> lore){
        ItemMeta im = this.getItemMeta();
        im.setLore(lore);
        this.setItemMeta(im);
    }
    
    public void dye(int red, int blue, int green){
        LeatherArmorMeta lam = (LeatherArmorMeta) this.getItemMeta();
        lam.setColor(Color.fromRGB(red, green, blue));
        this.setItemMeta(lam);
    }
    
    public void setSkullOwner(String owner){
        SkullMeta sm = (SkullMeta) this.getItemMeta();
        sm.setOwner(owner);
        this.setItemMeta(sm);
    }
    
    public void register(){ MyItemStack.myItemStacks.add(this); }
    public void unregister(){ MyItemStack.myItemStacks.remove(this); }
    public static List<MyItemStack> getMyItemStacks(){ return MyItemStack.myItemStacks; }
}