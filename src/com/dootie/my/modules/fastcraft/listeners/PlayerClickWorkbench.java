package com.dootie.my.modules.fastcraft.listeners;

import com.dootie.my.modules.fastcraft.FastRecipe;
import com.dootie.my.modules.fastcraft.MFastCraft;
import com.dootie.my.modules.items.MItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class PlayerClickWorkbench implements Listener {
    
    @EventHandler
    public void PlayerInteractEvent(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        String name = e.getClickedInventory().getName();
        if(!name.equals("My - FastCraft")) return;
        e.setCancelled(true);
        if(new MItemStack(e.getCurrentItem()).getData("craft") == null){
            menuInteraction(e);
            return;
        }
        FastRecipe recipe = FastRecipe.getRecipes().get((int) new MItemStack(e.getCurrentItem()).getData("craft"));
        PlayerInventory inventory = e.getWhoClicked().getInventory();
        
        for(MItemStack mis : recipe.getMaterials()){
            if(!inventory.containsAtLeast(mis.getItemStack(), mis.getItemStack().getAmount())){
                e.getWhoClicked().sendMessage("§c[My] You don't have enough materials!");
                return;
            }
        }
        for(MItemStack mis : recipe.getMaterials()){
            int amount = mis.getItemStack().getAmount();
            for(ItemStack is : inventory.getContents()){
                if(is == null) continue;
                if(!is.isSimilar(mis.getItemStack())) continue;
                if(amount == 0) break;
                int currentamount = is.getAmount();
                int finalamount = is.getAmount() - amount;
                if(finalamount < 0) finalamount = 0;
                amount = amount - (currentamount - finalamount);
                is.setAmount(finalamount);
            }
        }
        
        inventory.addItem(recipe.getResult().getItemStack());
        Player player = (Player) e.getWhoClicked();
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
                
    }
    
    public void menuInteraction(InventoryClickEvent e){
        if(new MItemStack(e.getCurrentItem()).getData("go") == null) return;
        int dir = (int) new MItemStack(e.getCurrentItem()).getData("go");
        
        if(dir >= 0){
            MFastCraft.fastCraftInventoryManager.openFastCraft((Player) e.getWhoClicked(), dir, 1, e.getClickedInventory());
            return;
        }
        if(dir == -1){
            e.getWhoClicked().openInventory(Bukkit.createInventory(null, InventoryType.WORKBENCH));
        }
    }
}
