package techreborn.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.api.recipe.IRecipeCompact;
import techreborn.items.ItemCells;
import techreborn.items.ItemIngots;
import techreborn.items.ItemPlates;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeCompact implements IRecipeCompact {

    HashMap<String, ItemStack> recipes = new HashMap<>();

    ArrayList<String> missingItems = new ArrayList<>();

    public RecipeCompact() {
        recipes.put("industrialDiamond", new ItemStack(Items.diamond));
        recipes.put("industrialTnt", new ItemStack(Blocks.tnt));
        recipes.put("copperIngot", ItemIngots.getIngotByName("copper"));
        recipes.put("tinIngot", ItemIngots.getIngotByName("tin"));
        recipes.put("bronzeIngot", ItemIngots.getIngotByName("bronze"));
        recipes.put("leadIngot", ItemIngots.getIngotByName("lead"));
        recipes.put("silverIngot", ItemIngots.getIngotByName("silver"));
        recipes.put("iridiumOre", ItemIngots.getIngotByName("Iridium"));
        recipes.put("plateiron", ItemPlates.getPlateByName("iron"));
        recipes.put("iridiumPlate", ItemPlates.getPlateByName("iridium"));
        recipes.put("cell", ItemCells.getCellByName("empty"));
        recipes.put("airCell", ItemCells.getCellByName("empty"));
    }

    @Override
    public ItemStack getItem(String name) {
        if(!recipes.containsKey(name)){
            if(!missingItems.contains(name)){
                missingItems.add(name);
            }
            return new ItemStack(ModItems.missingRecipe);
        } else {
            return recipes.get(name);
        }
    }

    public void saveMissingItems(File mcDir) throws IOException {
        File missingItemsFile = new File(mcDir, "TechRebornMissingItems.txt");
        if(missingItemsFile.exists()){
            missingItemsFile.delete();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(missingItemsFile));
        for(String str : missingItems){
            writer.write(str);
            writer.newLine();
        }
        writer.close();
    }

}
