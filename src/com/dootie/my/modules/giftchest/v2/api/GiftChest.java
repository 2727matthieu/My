package com.dootie.my.modules.giftchest.v2.api;

import com.dootie.my.modules.items.MItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.bukkit.Effect;
import org.bukkit.Sound;


public class GiftChest {
    
    private static final Map<String, GiftChest> chests = new HashMap<String, GiftChest>();
    
    private final String id;
    public List<MItemStack> rewards = new ArrayList<MItemStack>();
    public Effect particle = null;
    public int particle_amount = 0;
    public Sound sound = null;
    
    public GiftChest(String id){
        this.id = id;
    }
    
    public void addReward(MItemStack reward){ this.rewards.add(reward); }
    public void removeReward(MItemStack reward){ this.rewards.remove(reward); }
    public List<MItemStack> getRewards(){ return this.rewards; }
    
    public void register(){ GiftChest.chests.put(id, this); }
    public void unregister(){ GiftChest.chests.remove(id); }
    
    @Nullable
    public static GiftChest getGiftchest(String id){ return GiftChest.chests.get(id);}
}
