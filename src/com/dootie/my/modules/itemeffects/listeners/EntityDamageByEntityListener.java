package com.dootie.my.modules.itemeffects.listeners;


import com.dootie.my.helpers.item.MyItemStack;
import com.dootie.my.helpers.ItemStackUtils;
import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDamageByEntityListener implements Listener {
    
    @EventHandler
    public void EntityDamageByEntity(EntityDamageByEntityEvent event) {
        try{
            LivingEntity damager = (LivingEntity)event.getDamager();
            LivingEntity damaged = (LivingEntity)event.getEntity();
            ItemStack weapon = damager.getEquipment().getItemInMainHand();
            
            if (weapon == null) return;
            for(MyItemStack output : MyItemStack.getMyItemStacks()){
                if(!ItemStackUtils.compare(weapon, output, true)) continue;
                this.addPotionEffect(output, damaged);
            }
        }catch(ClassCastException ex){
            
        }
    }
    
    public void addPotionEffect(MyItemStack mis, LivingEntity damaged){
        if(mis.data.get("weapon.potionEffects") == null) return;
        for (String str : (List<String>) mis.data.get("weapon.potionEffects")) {
            String[] effect = str.split(":");
            if(effect.length == 3)
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect[0]), Integer.parseInt(effect[1]), Integer.parseInt(effect[2])));
            else if(effect.length == 8)
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect[0]), Integer.parseInt(effect[1]), Integer.parseInt(effect[2]), Boolean.parseBoolean(effect[3]), Boolean.parseBoolean(effect[4])));
        }
    }
}