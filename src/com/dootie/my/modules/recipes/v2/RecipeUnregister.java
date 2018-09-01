package com.dootie.my.modules.recipes.v2;


import com.dootie.my.My;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class RecipeUnregister {
    List<String> materials = new ArrayList<String>();

    public RecipeUnregister(List<String> materials) {
        for (String material : materials) {
            this.materials.add(material);
        }
    }

    public Boolean unregister() {
        if (this.materials == null) {
            return false;
        }
        if (this.materials.isEmpty()) {
            return false;
        }
        Iterator ri = Bukkit.getServer().recipeIterator();
        while (ri.hasNext()) {
            Recipe recipe = (Recipe)ri.next();
            ItemStack result = recipe.getResult();
            for (String r : this.materials) {
                MaterialEntry mat;
                if (r.contains("MATERIAL") || (mat = this.getMaterial(r)) == null || !result.getType().equals((Object)mat.material) || result.getData().getData() != mat.data) continue;
                ri.remove();
                String mat_name = mat.material.name() + (mat.data > 0 ? new StringBuilder().append(":").append(mat.data).toString() : "");
                My.logger.log(Level.INFO, "Removed vanilla recipe for item {0}", mat_name);
            }
        }
        return true;
    }

    private MaterialEntry getMaterial(String str) {
        String[] arrstring;
        if (str.contains(":")) {
            arrstring = str.split(":");
        } else {
            String[] arrstring2 = new String[1];
            arrstring = arrstring2;
            arrstring2[0] = str;
        }
        String[] sl = arrstring;
        if (sl.length == 0) {
            return null;
        }
        Material mat = Material.getMaterial((String)sl[0].toUpperCase());
        if (mat == null) {
            try {
                mat = Material.getMaterial((String)sl[0]);
            }
            catch (Exception e) {
                // empty catch block
            }
        }
        if (mat != null) {
            byte data = 0;
            if (sl.length > 1) {
                data = Byte.parseByte(sl[1]);
            }
            return new MaterialEntry(mat, data);
        }
        return null;
    }

    private class MaterialEntry {
        public Material material;
        public byte data;

        public MaterialEntry(Material mat, byte data) {
            this.material = mat;
            this.data = data;
        }
    }

}
