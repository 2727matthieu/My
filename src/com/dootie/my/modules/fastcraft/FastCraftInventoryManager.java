package com.dootie.my.modules.fastcraft;

import com.dootie.my.My;
import com.dootie.my.modules.items.MItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class FastCraftInventoryManager {
    public FastCraftInventoryManager(){
        
    }
    
    public void openFastCraft(Player player, int page, int mode, @Nullable final Inventory custom){
        Bukkit.getScheduler().runTaskAsynchronously(My.plugin, new Runnable() {
            @Override
            public void run() {
                Inventory inventory;
                if(custom == null)
                    inventory = Bukkit.createInventory(null, 54, "My - FastCraft");
                else{
                    inventory = custom;
                }
                    
                int count = 0;
                int startcount = page*45;
                int maxcount = startcount+45;
                inventory.clear();
                for(FastRecipe recipe : FastRecipe.getRecipes()){
                    if(count == maxcount) break;
                    if(!recipe.hasPermission(player)) continue;
                    MItemStack mis = recipe.getResult().clone();
                    
                    boolean contains = false;
                    for(MItemStack material : recipe.getMaterials()){
                        if(!player.getInventory().containsAtLeast(material.getItemStack(), 1)) continue;
                        contains = true;
                        break;
                    }
                    
                    if(!contains) continue;
                    if(count++ < startcount) continue;
                    
                    List<String> lore = mis.getLore();
                    if(lore == null) lore = new ArrayList<String>();
                    lore.add("");
                    lore.add("§r§lRequired:");
                    
                    for(MItemStack material : recipe.getMaterials()){
                        String name = material.getDisplayName();
                        if(name == null || name.equals("")) name = material.getItemStack().getType().toString();
                        if(player.getInventory().containsAtLeast(material.getItemStack(), material.getItemStack().getAmount())){
                            lore.add("§r§a§l"+material.getItemStack().getAmount()+" §r§a" + name);
                        }else{
                            lore.add("§r§c§l"+ material.getItemStack().getAmount() + " §r§c" + name );
                        }
                    }
                    mis.setLore(lore);
                    mis.setData("craft", FastRecipe.getRecipes().indexOf(recipe));
                    inventory.addItem(mis.getItemStack());
                }
                
                MItemStack mis;
                if(page != 0){
                    mis = new MItemStack(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));
                    mis.setDisplayName("§c§lPrevious page");
                    mis.setLore(Arrays.asList("§rClick to go to the previous page."));
                    mis.setData("go", page-1);
                    inventory.setItem(45, mis.getItemStack());
                }else{
                    mis = new MItemStack(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                    mis.setDisplayName(" ");
                    inventory.setItem(45, mis.getItemStack());
                }
                
                for(int i = 46; i < 53; i++){
                    mis = new MItemStack(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                    mis.setDisplayName(" ");
                    inventory.setItem(i, mis.getItemStack());
                }
                
                mis = new MItemStack(new ItemStack(Material.CRAFTING_TABLE));
                mis.setDisplayName("§c§lOpen a crafting table");
                mis.setLore(Arrays.asList("§rClick to open a crafting table."));
                mis.setData("go", -1);
                inventory.setItem(49, mis.getItemStack());
                
                if(count == maxcount){
                    mis = new MItemStack(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));
                    mis.setDisplayName("§c§lNext page");
                    mis.setLore(Arrays.asList("§rClick to go to the next page."));
                    mis.setData("go", page+1);
                    inventory.setItem(53, mis.getItemStack());
                }else{
                    mis = new MItemStack(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                    mis.setDisplayName(" ");
                    inventory.setItem(53, mis.getItemStack());
                }
                if(custom == null) player.openInventory(inventory);
            }
        });
    }
}
