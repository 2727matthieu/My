package com.dootie.my.modules.itemeffects.listeners;


import com.dootie.my.modules.items.MItemStack;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerItemConsumeListener implements Listener {
    
    @EventHandler
    public void playerFoodConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        ItemStack food = e.getItem();
        
        MItemStack mis = new MItemStack(food);
        
        
        
        this.changeValues(e, player, mis, food);
          
        if(mis.data.get("food.potionEffects") == null) return;
        for (String str : (ArrayList<String>) mis.data.get("food.potionEffects")) {
            String[] effect = str.split(":");
            if(effect.length == 3)
                player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect[0]), Integer.parseInt(effect[1]), Integer.parseInt(effect[2])));
            else if(effect.length == 8)
                player.addPotionEffect(
                        new PotionEffect(PotionEffectType.getByName(effect[0]),
                                Integer.parseInt(effect[1]),
                                Integer.parseInt(effect[2]),
                                Boolean.parseBoolean(effect[3]),
                                Boolean.parseBoolean(effect[4])
                        ));
        }
    }
    
    public void changeValues(PlayerItemConsumeEvent e, Player player, MItemStack mis, ItemStack food){
        if(mis.data.get("food.fill") == null) return;
        if(mis.data.get("food.saturation") == null) return;
        
        food.setAmount(food.getAmount() - 1);
        e.setCancelled(true);
        player.setFoodLevel(player.getFoodLevel() + (int) mis.data.get("food.fill"));
        player.setSaturation(player.getSaturation() + (float)mis.data.get("food.saturation"));
    }
}

