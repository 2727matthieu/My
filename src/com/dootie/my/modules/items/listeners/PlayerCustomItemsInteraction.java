/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dootie.my.modules.items.listeners;

import com.dootie.my.modules.items.MBlock;
import com.dootie.my.modules.items.MItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Ruben
 */
public class PlayerCustomItemsInteraction implements Listener {
    
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        BlockFace blockFace = e.getBlockFace();
        ItemStack cblock = e.getPlayer().getItemInHand();
        if (cblock == null) return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        MItemStack mis = new MItemStack(cblock);
        if(mis.data.get("custom.block") != null){
            Location nl = block.getLocation().clone().add(blockFace.getModX(), blockFace.getModY(),blockFace.getModZ());
            MBlock.setCustomBlock((int) mis.data.get("custom.block"), nl);
            cblock.setAmount(cblock.getAmount()-1);
            if(cblock.getAmount() == 0) cblock.setType(Material.AIR);
        }
        if(mis.data.get("custom.item") != null){
            if(block.getType() == Material.GRASS_BLOCK || block.getType() == Material.DIRT) e.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void PlayerDestroyEvent(BlockBreakEvent e) {
        Block block = e.getBlock();
        
        if(MBlock.getCustomBlockID(block.getLocation()) == 0) return;
        e.setCancelled(true);
        
        MBlock.removeCustomBlock(block.getLocation());
        block.breakNaturally();
    }
    
    @EventHandler
    public void onEntityExploded(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            if(MBlock.getCustomBlockID(block.getLocation()) == 0) continue;
            MBlock.removeCustomBlock(block.getLocation());
        }
    }
    
    @EventHandler
    public void MBlockPhysics(BlockPhysicsEvent e) {
        if(e.getChangedType() != Material.BROWN_MUSHROOM_BLOCK) return;
        if(MBlock.getCustomBlockID(e.getBlock().getLocation()) != 0)
            e.setCancelled(true);
    }
}