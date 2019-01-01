package com.dootie.my.modules.itemeffects.listeners;


import com.dootie.my.modules.items.MItemStack;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;


public class BlockBreakListener implements Listener{
    
    @EventHandler
    public void PlayerItemBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
        Block breakedBlock = e.getBlock();
        HashMap<Material, Material> melt = new HashMap<Material, Material>();
        melt.put(Material.COBBLESTONE, Material.STONE);
        melt.put(Material.STONE, Material.STONE);
        melt.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        melt.put(Material.IRON_ORE, Material.IRON_INGOT);
        
        if (tool == null) return;
        
        MItemStack mis = new MItemStack(tool);
        if(mis.data.get("tool.effect") == null) return;
        
        String tool_effect = (String)mis.data.get("tool.effect");
        
        switch(tool_effect){
            case "xpdrop":
                e.setExpToDrop(e.getExpToDrop() + Integer.parseInt((String) mis.data.get("tool.amount")));
                break;
            case "automelt":
                Material other = melt.get(breakedBlock.getType());
                if(other == null) return;
                e.setCancelled(true);
                player.getWorld().dropItem(breakedBlock.getLocation(), new ItemStack(other));
                breakedBlock.setType(Material.AIR);
                this.damageTool(player.getItemInHand());
                break;
            default:
                
        }
    }
    
    public ItemStack damageTool(ItemStack tool) {
        tool.setDurability((short)(tool.getDurability() + 1));
        if (tool.getDurability() == tool.getType().getMaxDurability()) {
            tool.setDurability((short)0);
            tool.setType(Material.AIR);
        }
        return tool;
    }
    
    
}