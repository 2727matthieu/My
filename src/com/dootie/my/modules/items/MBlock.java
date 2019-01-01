package com.dootie.my.modules.items;

import com.dootie.my.files.FileManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.configuration.ConfigurationSection;


public class MBlock {
    
    private static Map<String,Integer> locations = new HashMap<String,Integer>();
    
    public static List<Location> getCustomBlockLocations(int id){
        List<Location> locs = new ArrayList<Location>();
        for (String key : locations.keySet()) {
            if(locations.get(key) == id){
                String[] split = key.split(",");
                Location loc = new Location(Bukkit.getWorld(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]));
                locs.add(loc);
            }
        }
        
        return locs;
    }
    
    public static int getCustomBlockID(Location location){
        String key = location.getWorld().getName() + ","+location.getBlockX() + ","+location.getBlockY()+","+location.getBlockZ();
        if(locations.containsKey(key)) return locations.get(key);
        return 0;
    }
    
    protected static void registerBlocks(ConfigurationSection loc){
        for(String key : loc.getKeys(false)) locations.put(key, loc.getInt(key));
    }
    
    public static void removeCustomBlock(Location location){
        locations.remove(location.getWorld().getName() + ","+location.getBlockX() + ","+location.getBlockY()+","+location.getBlockZ());
        try {
            FileManager.DATA_YAML.save(FileManager.DATA_FILE);
        } catch (IOException ex) {
            Logger.getLogger(MBlock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void setCustomBlock(int id, Location location){
        locations.put(location.getWorld().getName() + ","+location.getBlockX() + ","+location.getBlockY()+","+location.getBlockZ(), id);
        FileManager.DATA_YAML.set("cbl", locations);
        try {
            FileManager.DATA_YAML.save(FileManager.DATA_FILE);
        } catch (IOException ex) {
            Logger.getLogger(MBlock.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Block block = location.getBlock();
        
        if(id < 54) block.setType(Material.BROWN_MUSHROOM_BLOCK);
        
        BlockData blockData = block.getBlockData();
        MultipleFacing multiFacing = (MultipleFacing) blockData;
        Boolean[] faces;
        
        switch(id){
            //                               UP     DOWN   NORTH  SOUTH  WEST   EAST
            case   1: faces = new Boolean[]{ false, false, false, false, true , false }; break;
            case   2: faces = new Boolean[]{ false, false, true , false, false, false }; break;
            case   3: faces = new Boolean[]{ false, false, false, true , true , false }; break;
            case   4: faces = new Boolean[]{ false, false, true , false, false, false }; break;
            case   5: faces = new Boolean[]{ false, false, true , false, true , false }; break;
            case   6: faces = new Boolean[]{ false, false, true , true , false, false }; break;
            case   7: faces = new Boolean[]{ false, false, true , true , true , false }; break;
            case   8: faces = new Boolean[]{ true , false, true , true , false, false }; break;
            case   9: faces = new Boolean[]{ true , false, true , true , true , false }; break;
            case  10: faces = new Boolean[]{ false, false, false, false, false, true }; break;
            
            default : faces = new Boolean[]{ false, false, false, false, true , false }; break;
        }
        
        multiFacing.setFace(BlockFace.UP,    faces[0]);
        multiFacing.setFace(BlockFace.DOWN,  faces[1]);
        multiFacing.setFace(BlockFace.NORTH, faces[2]);
        multiFacing.setFace(BlockFace.SOUTH, faces[3]);
        multiFacing.setFace(BlockFace.WEST,  faces[4]);
        multiFacing.setFace(BlockFace.EAST,  faces[5]);
        
        block.setBlockData(multiFacing);
    }
}
