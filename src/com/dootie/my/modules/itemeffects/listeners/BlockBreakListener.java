package com.dootie.my.modules.itemeffects.listeners;


import com.dootie.my.helpers.item.MyItemStack;
import com.dootie.my.helpers.ItemStackUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
        HashMap<Material, Material> smelt = new HashMap<Material, Material>();
        smelt.put(Material.COBBLESTONE, Material.STONE);
        smelt.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        smelt.put(Material.IRON_ORE, Material.IRON_INGOT);
        
        if (tool == null) return;
        for(MyItemStack mis : MyItemStack.getMyItemStacks()){
            if(!ItemStackUtils.compare(tool, mis, true)) continue;
            if(!mis.data.containsKey("tool.effect")) continue;
            String tool_effect = (String)mis.data.get("tool.effect");
            if(tool_effect.equals("autosmelt")){
                Iterator it2 = smelt.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry pair2 = (Map.Entry) it2.next();
                    if (pair2.getKey() == breakedBlock.getType()) {
                        player.getWorld().dropItem(breakedBlock.getLocation(), new ItemStack((Material)pair2.getValue()));
                        e.setCancelled(true);
                        breakedBlock.setType(Material.AIR);
                        this.damageTool(player.getItemInHand());
                        return;
                    }
                    it2.remove();
                }
            }else
            if(tool_effect.equals("xpdrop")){
                e.setExpToDrop(e.getExpToDrop() + Integer.parseInt((String) mis.data.get("tool.amount")));
                return;
            }
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