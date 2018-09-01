package com.dootie.my.modules.giftchest.v2.api;

import com.dootie.my.helpers.item.MyItemStack;
import com.dootie.my.modules.recipes.v2.api.MyRecipe;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Effect;
import org.bukkit.Sound;


public class GiftChest {
    
    private static final List<GiftChest> chests = new ArrayList<GiftChest>();
    
    public MyRecipe recipe = null;
    public List<MyItemStack> rewards = new ArrayList<MyItemStack>();
    public Effect particle = null;
    public int particle_amount = 0;
    public Sound sound = null;
    
    public GiftChest(MyRecipe recipe){
        this.recipe = recipe;
    }
    
    public void addReward(MyItemStack reward){ this.rewards.add(reward); }
    public void removeReward(MyItemStack reward){ this.rewards.remove(reward); }
    public List<MyItemStack> getRewards(){ return this.rewards; }
    
    public void register(){ GiftChest.chests.add(this); }
    public void unregister(){ GiftChest.chests.remove(this); }
    public static List<GiftChest> getGiftchests(){ return GiftChest.chests;}
}
