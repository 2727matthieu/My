package com.dootie.my.modules.recipes.v2.api;

import com.dootie.my.helpers.item.MyItemStack;
import com.dootie.my.My;
import com.dootie.my.modules.recipes.v2.helpers.BrewingRecipe;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


public class MyRecipe {
    
    public Map<String, Object> data = new HashMap<String, Object>();

    public MyRecipe(String id, RecipeType type){
        data.put("id", id);
        data.put("type", type);
    }
    
    public RecipeType getType(){ return (RecipeType) data.get("type"); }
    public void setOutput(MyItemStack output){ data.put("output", output); }
    public MyItemStack getOutput(){ return (MyItemStack) data.get("output"); }
    public void setPermission(String permission){ data.put("permission", permission); }
    public String getPermission(){ return (String) data.get("permission"); }
    public void setShaped(boolean shaped){ data.put("shaped", shaped); }
    public void setSlot(int slot, MyItemStack item){
        if(!this.data.containsKey("slot")) this.data.put("slot", new MyItemStack[9]);
        ((MyItemStack[])data.get("slot"))[slot] = item;
    }
    public MyItemStack[] getSlots(){ return (MyItemStack[]) data.get("slot"); }
    public void setInput(MyItemStack mis){
       this.data.put("input", mis);
    }
    public void setExperience(float experience){
       this.data.put("experience", experience);
    }
    public void setMaterial(MyItemStack mis){
       this.data.put("material", mis);
    }
    /* Process */
    public boolean register(){
        switch (this.getType()) {
            case CRAFT:
                if ((boolean) this.data.get("shaped")) {
                    ShapedRecipe sr = new ShapedRecipe((ItemStack) this.data.get("output"));
                    sr.shape(new String[]{"123", "456", "789"});
                    MyItemStack[] slots = (MyItemStack[]) this.data.get("slot");
                    for(int i = 0; i < 9; i++)
                        if(slots[i] != null)
                            sr.setIngredient(Integer.toString(i+1).charAt(0), slots[i].getType(), slots[i].getDurability());
                    return Bukkit.addRecipe((org.bukkit.inventory.Recipe)sr);
                }else{
                    ShapelessRecipe sr = new ShapelessRecipe((ItemStack) this.data.get("output"));
                    for(MyItemStack mis : (MyItemStack[]) this.data.get("slot"))
                        if(mis != null) {
                            sr.addIngredient(mis.getType(), mis.getDurability());
                        }
                    return Bukkit.addRecipe((org.bukkit.inventory.Recipe)sr);
                }
            case FURNACE:
                FurnaceRecipe fr = new FurnaceRecipe( (ItemStack) this.data.get("output"), ((ItemStack) this.data.get("input")).getType());
                if(this.data.containsKey("experience")) fr.setExperience((float) this.data.get("experience"));
                return Bukkit.addRecipe((org.bukkit.inventory.Recipe)fr);
            case BREW:
                BrewingRecipe brewingRecipe = new BrewingRecipe(
                        ((ItemStack) this.data.get("material")),
                        ((ItemStack) this.data.get("input")),
                        ((ItemStack) this.data.get("output")),
                        false
                );
                return brewingRecipe.register();
//            new BrewingRecipe(((ItemStack) this.data.get("input")).getType(), (inventory, item, material, pos) -> {
//            if (item.getType() != ((MyItemStack[]) this.data.get("slot"))[0].getType()) return;
//            ((Inventory) inventory).setItem(pos, (ItemStack) this.data.get("output"));
            default: return false;
        }
    }
}