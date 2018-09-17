/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.common.util.OreUtil;
import techreborn.blocks.cable.BlockCable;

public class OreDict {

	private static final ImmutableList<String> plateGenIgnores = ImmutableList.of(
		"hot", //Hot ingots
		"mixed_metal", //Mixed metal has own version of plate
		"iridium_alloy" //Iridium alloy is plate itself
	);

	/**
	 * Register blocks and items
	 */
	public static void init() {
		// Blocks
		OreUtil.registerOre("fenceIron", ModBlocks.REFINED_IRON_FENCE);
		OreUtil.registerOre("woodRubber", ModBlocks.RUBBER_LOG);
		OreUtil.registerOre("glassReinforced", ModBlocks.REINFORCED_GLASS);
		OreUtil.registerOre("treeSapling", ModBlocks.RUBBER_SAPLING);
		OreUtil.registerOre("saplingRubber", ModBlocks.RUBBER_SAPLING);

		//		OreUtil.registerOre("logWood", new ItemStack(RUBBER_LOG, 1, OreDictionary.WILDCARD_VALUE));
		//		OreUtil.registerOre("logRubber", new ItemStack(RUBBER_LOG, 1, OreDictionary.WILDCARD_VALUE));
		//		OreUtil.registerOre("plankWood", new ItemStack(RUBBER_PLANKS, 1, OreDictionary.WILDCARD_VALUE));
		//		OreUtil.registerOre("slabWood", new ItemStack(RUBBER_LOG_SLAB_HALF, 1, OreDictionary.WILDCARD_VALUE));
		//		OreUtil.registerOre("stairWood", new ItemStack(RUBBER_LOG_STAIR, 1, OreDictionary.WILDCARD_VALUE));
		//		OreUtil.registerOre("plankRubber", new ItemStack(RUBBER_PLANKS, 1, OreDictionary.WILDCARD_VALUE));
		//		OreUtil.registerOre("treeLeaves", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));
		//		OreUtil.registerOre("leavesRubber", new ItemStack(RUBBER_LEAVES, 1, OreDictionary.WILDCARD_VALUE));

		// Parts
		OreUtil.registerOre("circuitBasic", TRContent.Parts.ELECTRONIC_CIRCUIT.getStack());
		OreUtil.registerOre("circuitAdvanced", TRContent.Parts.ADVANCED_CIRCUIT.getStack());
		OreUtil.registerOre("circuitElite", TRContent.Parts.INDUSTRIAL_CIRCUIT.getStack());
		OreUtil.registerOre("circuitStorage", TRContent.Parts.DATA_STORAGE_CHIP.getStack());
		OreUtil.registerOre("circuitMaster", TRContent.Parts.ENERGY_FLOW_CHIP.getStack());
		OreUtil.registerOre("craftingDiamondGrinder", TRContent.Parts.DIAMOND_GRINDING_HEAD.getStack());
		OreUtil.registerOre("craftingTungstenGrinder", TRContent.Parts.TUNGSTEN_GRINDING_HEAD.getStack());
		OreUtil.registerOre("craftingSuperconductor", TRContent.Parts.SUPERCONDUCTOR.getStack());
		OreUtil.registerOre("materialResin", TRContent.Parts.SAP.getStack());
		OreUtil.registerOre("materialRubber", TRContent.Parts.RUBBER.getStack());
		OreUtil.registerOre("itemRubber", TRContent.Parts.RUBBER.getStack());

		// Frames
		OreUtil.registerOre("machineBasic", new ItemStack(ModBlocks.MACHINE_BLOCK_BASIC));
		OreUtil.registerOre("machineBlockBasic", new ItemStack(ModBlocks.MACHINE_BLOCK_BASIC));
		OreUtil.registerOre("machineBlockAdvanced", new ItemStack(ModBlocks.MACHINE_BLOCK_ADVANCED));
		OreUtil.registerOre("machineBlockElite", new ItemStack(ModBlocks.MACHINE_BLOCK_ELITE));

		// Tools&Armor
		OreUtil.registerOre("reBattery", TRItems.RED_CELL_BATTERY);
		OreUtil.registerOre("lapotronCrystal", TRItems.LAPOTRON_CRYSTAL);
		OreUtil.registerOre("energyCrystal", TRItems.ENERGY_CRYSTAL);
		OreUtil.registerOre("drillBasic", TRItems.BASIC_DRILL);
		OreUtil.registerOre("drillDiamond", TRItems.ADVANCED_DRILL);

		// Misc
		OreUtil.registerOre("industrialTnt", Blocks.TNT);
		OreUtil.registerOre("craftingPiston", Blocks.PISTON);
		OreUtil.registerOre("craftingPiston", Blocks.STICKY_PISTON);
		OreUtil.registerOre("crafterWood", Blocks.CRAFTING_TABLE);
		OreUtil.registerOre("craftingIndustrialDiamond", Items.DIAMOND);
		OreUtil.registerOre("fertilizer", new ItemStack(Items.DYE, 1, 15));
		OreUtil.registerOre("insulatedGoldCableItem", BlockCable.getCableByName("insulatedgold"));
		OreUtil.registerOre("pulpWood", TRContent.Dusts.SAW.getStack());

		//OreUtil.registerOre("uran235", nothing);
		//OreUtil.registerOre("uran238", nothing);
		//OreUtil.registerOre("smallUran235", nothing);

		//		for (String ore : BlockOre.ores) {
		//		OreUtil.registerOre("ore" + StringUtils.toFirstCapital(ore), BlockOre.getOreByName(ore));
		//	}

		//	OreUtil.registerOre("blockSilver", BlockStorage.getStorageBlockByName("silver"));
		//	OreUtil.registerOre("blockAluminum", BlockStorage.getStorageBlockByName("aluminum"));
		//	OreUtil.registerOre("blockAluminium", BlockStorage.getStorageBlockByName("aluminum"));
		//	OreUtil.registerOre("blockTitanium", BlockStorage.getStorageBlockByName("titanium"));
		//	OreUtil.registerOre("blockChrome", BlockStorage.getStorageBlockByName("chrome"));
		//	OreUtil.registerOre("blockSteel", BlockStorage.getStorageBlockByName("steel"));
		//	OreUtil.registerOre("blockBrass", BlockStorage.getStorageBlockByName("brass"));
		//	OreUtil.registerOre("blockLead", BlockStorage.getStorageBlockByName("lead"));
		//	OreUtil.registerOre("blockElectrum", BlockStorage.getStorageBlockByName("electrum"));
		//	OreUtil.registerOre("blockZinc", BlockStorage.getStorageBlockByName("zinc"));
		//	OreUtil.registerOre("blockPlatinum", BlockStorage.getStorageBlockByName("platinum"));
		//	OreUtil.registerOre("blockTungsten", BlockStorage.getStorageBlockByName("tungsten"));
		//	OreUtil.registerOre("blockNickel", BlockStorage.getStorageBlockByName("nickel"));
		//	OreUtil.registerOre("blockInvar", BlockStorage.getStorageBlockByName("invar"));
		//	OreUtil.registerOre("blockIridium", BlockStorage.getStorageBlockByName("iridium"));
		//	OreUtil.registerOre("blockBronze", BlockStorage.getStorageBlockByName("bronze"));
		//	OreUtil.registerOre("blockCopper", BlockStorage2.getStorageBlockByName("copper", 1));
		//	OreUtil.registerOre("blockTin", BlockStorage2.getStorageBlockByName("tin", 1));
		//	OreUtil.registerOre("blockTungstensteel", BlockStorage2.getStorageBlockByName("tungstensteel", 1));
		//	OreUtil.registerOre("blockRuby", BlockStorage2.getStorageBlockByName("ruby", 1));
		//	OreUtil.registerOre("blockSapphire", BlockStorage2.getStorageBlockByName("sapphire", 1));
		//	OreUtil.registerOre("blockPeridot", BlockStorage2.getStorageBlockByName("peridot", 1));
		//	OreUtil.registerOre("blockYellowGarnet", BlockStorage2.getStorageBlockByName("yellowGarnet", 1));
		//	OreUtil.registerOre("blockRedGarnet", BlockStorage2.getStorageBlockByName("redGarnet", 1));

		//		TODO: fix recipe
		//		for (String type : ItemGems.types) {
		//			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "gem_" + type), ItemGems.getGemByName(type));
		//			boolean ignoreIt = false;
		//			for (String ignore : plateGenIgnores)
		//				if (type.startsWith(ignore))
		//					ignoreIt = true;
		//			 if (!ignoreIt)
		//				ItemPlates.registerType(type);
		//		}

		//		for (String type : ItemIngots.types) {
		//			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "ingot_" + type), ItemIngots.getIngotByName(type));
		//			boolean ignoreIt = false;
		//			for (String ignore : plateGenIgnores)
		//				if (type.startsWith(ignore))
		//					ignoreIt = true;
		//			 if (!ignoreIt)
		//				ItemPlates.registerType(type);
		//		}

		//		for (String type : ItemPlates.types) {
		//			 OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "plate_" + type), ItemPlates.getPlateByName(type));
		//		}

		//		for (String type : ItemDusts.types) {
		//			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_" + type), ItemDusts.getDustByName(type));
		//		}
		//
		//		for (String type : ItemDustsSmall.types) {
		//			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_small_" + type), ItemDustsSmall.getSmallDustByName(type));
		//		}

		//		for (String type : ItemNuggets.types) {
		//			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "nugget_" + type), ItemNuggets.getNuggetByName(type));
		//		}

	}

}
