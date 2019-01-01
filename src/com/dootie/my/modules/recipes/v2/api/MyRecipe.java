package com.dootie.my.modules.recipes.v2.api;

import com.dootie.my.modules.items.MItemStack;
import com.dootie.my.My;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


public class MyRecipe {
    
    private Map<String, Object> data = new HashMap<String, Object>();

    public MyRecipe(String id, RecipeType type){
        data.put("id", id);
        data.put("type", type);
    }
    
    public RecipeType getType(){ return (RecipeType) data.get("type"); }
    public void setOutput(MItemStack output){ data.put("output", output); }
    public MItemStack getOutput(){ return (MItemStack) data.get("output"); }
    public void setPermission(String permission){ data.put("permission", permission); }
    public String getPermission(){ return (String) data.get("permission"); }
    public void setShaped(boolean shaped){ data.put("shaped", shaped); }
    public void setSlot(int slot, MItemStack item){
        if(!this.data.containsKey("slot")) this.data.put("slot", new MItemStack[9]);
        ((MItemStack[])data.get("slot"))[slot] = item;
    }
    public MItemStack[] getSlots(){ return (MItemStack[]) data.get("slot"); }
    public void setInput(MItemStack mis){
       this.data.put("input", mis);
    }
    public void setExperience(float experience){
       this.data.put("experience", experience);
    }
    public void setMaterial(MItemStack mis){
       this.data.put("material", mis);
    }
    /* Process */
    public boolean register(){
        My.devLog("myrecipe : output > Checking output");
        My.devLog("myrecipe : output > MItemStack {0}", this.data.get("output"));
        if(this.data.get("output") == null) return false;
        My.devLog("myrecipe : output > ItemStack {0}", ((MItemStack) this.data.get("output")).getItemStack());
        if(((MItemStack) this.data.get("output")).getItemStack() == null) return false;
        
        switch (this.getType()) {
            case CRAFT:
                if ((boolean) this.data.get("shaped")) {
                    ShapedRecipe sr = new ShapedRecipe(((MItemStack) this.data.get("output")).getItemStack());
                    sr.shape(new String[]{"123", "456", "789"});
                    MItemStack[] slots = (MItemStack[]) this.data.get("slot");
                    for(int i = 0; i < 9; i++)
                        if(slots[i] != null)
                            sr.setIngredient(Integer.toString(i+1).charAt(0), slots[i].getItemStack().getType(), slots[i].getItemStack().getDurability());
                    return Bukkit.addRecipe((org.bukkit.inventory.Recipe)sr);
                }else{
                    ShapelessRecipe sr = new ShapelessRecipe(((MItemStack) this.data.get("output")).getItemStack());
                    for(MItemStack mis : (MItemStack[]) this.data.get("slot"))
                        if(mis != null) {
                            sr.addIngredient(mis.getItemStack().getType(), mis.getItemStack().getDurability());
                        }
                    return Bukkit.addRecipe((org.bukkit.inventory.Recipe)sr);
                }
            case FURNACE:
                FurnaceRecipe fr = new FurnaceRecipe( ((MItemStack) this.data.get("output")).getItemStack(), (((MItemStack) this.data.get("input")).getItemStack()).getType());
                if(this.data.containsKey("experience")) fr.setExperience((float) this.data.get("experience"));
                return Bukkit.addRecipe((org.bukkit.inventory.Recipe)fr);
            case BREW:
                BrewingRecipe brewingRecipe = new BrewingRecipe(
                        (MItemStack) this.data.get("material"),
                        (MItemStack) this.data.get("input"),
                        (MItemStack) this.data.get("output"),
                        false
                );
                return brewingRecipe.register();
            default: return false;
        }
    }
}