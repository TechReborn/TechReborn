package techreborn.init;

import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import techreborn.api.recipe.IRecipeCompact;
import techreborn.blocks.BlockMachineFrame;
import techreborn.compat.CompatManager;
import techreborn.items.*;
import techreborn.parts.powerCables.ItemStandaloneCables;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeCompact implements IRecipeCompact
{

	HashMap<String, ItemStack> recipes = new HashMap<>();

	ArrayList<String> missingItems = new ArrayList<>();

	HashMap<String, Ic2ItemLookup> lookupHashMap = new HashMap<>();

	boolean inited = false;

	public void init()
	{
		recipes.put("industrialDiamond", new ItemStack(Items.DIAMOND));
		recipes.put("industrialTnt", new ItemStack(Blocks.TNT));
		recipes.put("copperIngot", ItemIngots.getIngotByName("copper"));
		recipes.put("tinIngot", ItemIngots.getIngotByName("tin"));
		recipes.put("bronzeIngot", ItemIngots.getIngotByName("bronze"));
		recipes.put("leadIngot", ItemIngots.getIngotByName("lead"));
		recipes.put("silverIngot", ItemIngots.getIngotByName("silver"));
		recipes.put("iridiumOre", ItemIngots.getIngotByName("iridium"));
		recipes.put("plateiron", ItemPlates.getPlateByName("iron"));
		recipes.put("iridiumPlate", ItemPlates.getPlateByName("iridium"));
		recipes.put("cell", new ItemStack(ModItems.emptyCell));
		recipes.put("airCell", new ItemStack(ModItems.emptyCell));
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
		recipes.put("waterCell", DynamicCell.getCellWithFluid(FluidRegistry.WATER));
		recipes.put("lavaCell", DynamicCell.getCellWithFluid(FluidRegistry.LAVA));
		recipes.put("pump", new ItemStack(ModBlocks.pump));
//		recipes.put("teleporter", new ItemStack(ModItems.missingRecipe));
		recipes.put("advancedAlloy", ItemIngots.getIngotByName("advancedAlloy"));
		recipes.put("lvTransformer", new ItemStack(ModBlocks.lvt));
		recipes.put("mvTransformer", new ItemStack(ModBlocks.mvt));
		recipes.put("hvTransformer", new ItemStack(ModBlocks.hvt));
		recipes.put("windMill", new ItemStack(ModBlocks.windMill));
		recipes.put("energyCrystal", new ItemStack(ModItems.energyCrystal));
		recipes.put("lapotronCrystal", new ItemStack(ModItems.lapotronCrystal));
		recipes.put("reinforcedGlass", new ItemStack(ModBlocks.reinforcedglass));
		recipes.put("compressor", new ItemStack(ModBlocks.Compressor));
		recipes.put("insulatedGoldCableItem", ItemStandaloneCables.getCableByName("insulatedgold"));
		recipes.put("fertilizer", new ItemStack(Items.DYE));


		lookupHashMap.put("miningDrill", new Ic2ItemLookup("drill"));
		lookupHashMap.put("reBattery", new Ic2ItemLookup("re_battery"));
		lookupHashMap.put("electronicCircuit", new Ic2ItemLookup("crafting", "circuit"));
		lookupHashMap.put("advancedCircuit", new Ic2ItemLookup("crafting", "advanced_circuit"));
		lookupHashMap.put("lapotronCrystal", new Ic2ItemLookup("lapotron_crystal"));
		lookupHashMap.put("lapotronCrystal", new Ic2ItemLookup("lapotron_crystal"));
		lookupHashMap.put("iridiumPlate", new Ic2ItemLookup(ItemPlates.getPlateByName("iridium")));
		lookupHashMap.put("advancedMachine", new Ic2ItemLookup("resource", "advanced_machine"));
		lookupHashMap.put("windMill", new Ic2ItemLookup("te", "wind_generator"));
		lookupHashMap.put("reinforcedGlass", new Ic2ItemLookup("glass", "reinforced"));
		lookupHashMap.put("extractor", new Ic2ItemLookup("te", "extractor"));
		lookupHashMap.put("machine", new Ic2ItemLookup("resource", "machine"));
		lookupHashMap.put("hvTransformer", new Ic2ItemLookup("te", "hv_transformer"));
		lookupHashMap.put("generator", new Ic2ItemLookup("te", "generator"));
		lookupHashMap.put("rubberWood", new Ic2ItemLookup("rubber_wood"));
		lookupHashMap.put("industrialTnt", new Ic2ItemLookup("te", "itnt"));
		lookupHashMap.put("industrialDiamond", new Ic2ItemLookup("crafting", "industrial_diamond"));
		lookupHashMap.put("macerator", new Ic2ItemLookup("te", "macerator"));
		lookupHashMap.put("diamondDrill", new Ic2ItemLookup("diamond_drill"));
		lookupHashMap.put("solarPanel", new Ic2ItemLookup("te", "solar_generator"));
		lookupHashMap.put("insulatedGoldCableItem", new Ic2ItemLookup("cable", "type:gold,insulation:1"));

		inited = false;
	}

	@Override
	public ItemStack getItem(String name)
	{
		if (!inited)
		{
			init();
		}
		if(Loader.isModLoaded("IC2")){
			ItemStack stack = IC2Items.getItem(name);
			if(stack == null){
				if(lookupHashMap.containsKey(name)){
					Ic2ItemLookup lookup = lookupHashMap.get(name);
					if(lookup.getStack() != null){
						return lookup.getStack();
					}
					return IC2Items.getItem(lookup.getName(), lookup.getVariant());
				} else {
					String line = "IC2:" + name;
					if (!missingItems.contains(line))
					{
						missingItems.add(line);
					}
				}
			} else {
				return stack;
			}
		}
		if (!recipes.containsKey(name))
		{
			if (!missingItems.contains(name))
			{
				missingItems.add(name);
			}
			return new ItemStack(ModItems.missingRecipe);
		} else
		{
			return recipes.get(name);
		}
	}

	public void saveMissingItems(File mcDir) throws IOException
	{
		File missingItemsFile = new File(mcDir, "missingItems.txt");
		if (missingItemsFile.exists())
		{
			missingItemsFile.delete();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(missingItemsFile));
		for (String str : missingItems)
		{
			writer.write(str);
			writer.newLine();
		}
		writer.close();
	}

	class Ic2ItemLookup {
		@Nullable
		String name;
		@Nullable
		String variant;
		@Nullable
		ItemStack stack;

		public Ic2ItemLookup(String name, String variant) {
			this.name = name;
			this.variant = variant;
		}

		public Ic2ItemLookup(String name) {
			this.name = name;
		}

		public Ic2ItemLookup(ItemStack stack) {
			this.stack = stack;
		}

		public String getName() {
			return name;
		}

		public String getVariant() {
			return variant;
		}

		@Nullable
		public ItemStack getStack() {
			return stack;
		}
	}

}
