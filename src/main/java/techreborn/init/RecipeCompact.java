package techreborn.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.api.recipe.IRecipeCompact;
import techreborn.blocks.BlockMachineFrame;
import techreborn.items.ItemCells;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
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

    boolean inited = false;

    public void init(){
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
        recipes.put("electronicCircuit", ItemParts.getPartByName("electronicCircuit"));
        recipes.put("advancedCircuit", ItemParts.getPartByName("advancedCircuit"));
        recipes.put("rubberWood", new ItemStack(ModBlocks.rubberLog));
        recipes.put("resin", ItemParts.getPartByName("rubberSap"));
        recipes.put("carbonPlate", ItemPlates.getPlateByName("carbon"));
        recipes.put("reBattery", new ItemStack(ModItems.reBattery));
        recipes.put("machine", BlockMachineFrame.getFrameByName("machine", 1));
        recipes.put("advancedMachine", BlockMachineFrame.getFrameByName("advancedMachine", 1));
        recipes.put("extractor", new ItemStack(ModBlocks.Extractor));
        recipes.put("generator", new ItemStack(ModBlocks.Generator));
        recipes.put("macerator", new ItemStack(ModBlocks.Grinder));
        recipes.put("diamondDrill", new ItemStack(ModItems.diamondDrill));
        recipes.put("miningDrill", new ItemStack(ModItems.ironDrill));
        recipes.put("solarPanel", new ItemStack(ModBlocks.solarPanel));
        recipes.put("waterCell", ItemCells.getCellByName("water"));
        recipes.put("lavaCell", ItemCells.getCellByName("lava"));
        inited = false;
    }


    @Override
    public ItemStack getItem(String name) {
        if(!inited){
            init();
        }
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
