package com.dootie.my.modules.uncrafter.listeners;

import com.dootie.my.My;
import com.dootie.my.modules.uncrafter.MUncrafter;
import com.dootie.my.helpers.ItemStackUtils;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;


public class InventoryClickListener implements Listener  {
    
    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        HumanEntity player = e.getWhoClicked();
        boolean remove = false;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() == null) return;
        if(!e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)) return;
        if(!player.hasPermission(MUncrafter.uncraftPermission)) return;
        if(e.getSlot() != 0) return;
        ItemStack item = e.getCursor();
        if(item.getType() == Material.AIR) return;
        List<Recipe> recipes = Bukkit.getServer().getRecipesFor(item);
        for(Recipe r : recipes){
            if(!ItemStackUtils.compare(item, r.getResult(), true)) continue;
            try{
                ShapedRecipe recipe = (ShapedRecipe) r;
                Map<Character, ItemStack> ingredient = recipe.getIngredientMap();
                for(char key : ingredient.keySet()){
                    if(ingredient.get(key) == null) continue;
                    player.getInventory().addItem(new ItemStack(ingredient.get(key).getType(), ingredient.get(key).getAmount()));
                }
                remove = true;
                break;
            }catch(ClassCastException ex){
                try{
                    ShapelessRecipe recipe = (ShapelessRecipe) r;
                    for(ItemStack i : recipe.getIngredientList())
                        player.getInventory().addItem(new ItemStack(i.getType(), i.getAmount()));
                    remove = true;
                    break;
                }catch(ClassCastException exe){
                    Logger.getLogger(My.class.getName()).log(Level.SEVERE, null, exe);
                }
            }
        }
        if(remove) player.setItemOnCursor(new ItemStack(Material.AIR));
    }
}