package com.dootie.my.modules.itemeffects.listeners;


import com.dootie.my.modules.recipes.v2.MRecipes;
import com.dootie.my.helpers.item.MyItemStack;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import com.dootie.my.helpers.ItemStackUtils;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class PlayerInteractListener implements Listener {
    
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        ItemStack tool = e.getPlayer().getItemInHand();

        if (tool == null) return;
        
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            for(MyItemStack mis : MyItemStack.getMyItemStacks()){
                if(!ItemStackUtils.compare(mis, tool, true)) continue;
                if(mis.data.get("tool.effect") == null) continue;
                if(!mis.data.get("tool.effect").equals("breakbedrock")) continue;
                if(block.getType() != Material.BEDROCK) continue;
                block.setType(Material.AIR);
                block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(Material.BEDROCK));
                tool.setDurability((short) (tool.getDurability() + 1));
            }
        }
    }
}