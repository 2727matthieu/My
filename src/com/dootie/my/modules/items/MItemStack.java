package com.dootie.my.modules.items;

import com.dootie.my.My;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;


public final class MItemStack {
    
    public Map<String, Object> data;
    
    public MItemStack(@Nullable ItemStack itemStack) {
        this.data = new HashMap<String, Object>();
        this.data.put("super", itemStack);
        if(itemStack == null) return;
        String id = (String) this.getData("MY_ID");
        if(id != null){
            this.data.putAll(MItems.items.get(id).data);
            this.data.put("super", itemStack);
            My.devLog("MItemStack : create > Found ID {0}.", id);
        }else My.devLog("MItemStack : create > No ID found.");
    }
    
    public ItemStack getItemStack(){ return (ItemStack) data.get("super"); }
    
    @Nullable
    public String getDisplayName(){
        if(this.getItemStack().getType() == Material.AIR) return null;
        ItemMeta im = this.getItemStack().getItemMeta();
        return im.getDisplayName();
    }
    
    public void setDisplayName(String name){
        ItemMeta im = this.getItemStack().getItemMeta();
        im.setDisplayName(name);
        this.getItemStack().setItemMeta(im);
    }
    
    @Nullable
    public List<String> getLore(){
        if(this.getItemStack().getType() == Material.AIR) return null;
        ItemMeta im = this.getItemStack().getItemMeta();
        return im.getLore();
    }
    
    public void setLore(List<String> lore){
        if(this.getItemStack().getType() == Material.AIR) return;
        ItemMeta im = this.getItemStack().getItemMeta();
        im.setLore(lore);
        this.getItemStack().setItemMeta(im);
    }
    
    public void dye(int red, int blue, int green){
        LeatherArmorMeta lam = (LeatherArmorMeta) this.getItemStack().getItemMeta();
        lam.setColor(Color.fromRGB(red, green, blue));
        this.getItemStack().setItemMeta(lam);
    }
    
    public void setSkullOwner(String owner){
        SkullMeta sm = (SkullMeta) this.getItemStack().getItemMeta();
        sm.setOwner(owner);
        this.getItemStack().setItemMeta(sm);
    }
    
    public void setNBTTag(Object[] values){
        ItemStack new_item = NBTEditor.setItemTag(this.getItemStack(), values[0], Arrays.copyOfRange(values, 1, values.length));
        this.data.put("super", new_item);
    }
    
    public void setData(String id, Object value){
        ItemStack new_item = NBTEditor.setItemTag(this.getItemStack(), value, id);
        this.data.put("super", new_item);
    }
    
    @Nullable
    public Object getData(String id){
        try{
            return NBTEditor.getItemTag(this.getItemStack(), id);
        }catch(Exception ex){
            return null;
        }
    }
    
    @Override
    public MItemStack clone(){
        return new MItemStack(new ItemStack(this.getItemStack()));
    }
}