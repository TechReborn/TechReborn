/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.Core;
import techreborn.blocks.BlockMachineFrame;
import techreborn.items.*;
import techreborn.parts.powerCables.ItemStandaloneCables;

public class OreDict {

	private static final ImmutableList<String> plateGenIgnores = ImmutableList.of(
		"hot", //Hot ingots
		"mixed_metal", //Mixed metal has own version of plate
		"iridium_alloy", //Iridium alloy is plate itself
		ModItems.META_PLACEHOLDER //...
	);

	public static void init() {
		if (Loader.isModLoaded("ic2")) {
			Core.logHelper.info("IC2 installed, enabling integration");
			IC2Dict.init();
		}

		OreDictionary.registerOre("reBattery", ModItems.RE_BATTERY);

		OreDictionary.registerOre("circuitBasic", ItemParts.getPartByName("electronicCircuit"));
		OreDictionary.registerOre("circuitAdvanced", ItemParts.getPartByName("advancedCircuit"));
		OreDictionary.registerOre("circuitStorage", ItemParts.getPartByName("dataStorageCircuit"));
		OreDictionary.registerOre("circuitElite", ItemParts.getPartByName("dataControlCircuit"));
		OreDictionary.registerOre("circuitMaster", ItemParts.getPartByName("energyFlowCircuit"));

		OreDictionary.registerOre("machineBlockBasic", BlockMachineFrame.getFrameByName("machine", 1));
		OreDictionary.registerOre("machineBlockAdvanced", BlockMachineFrame.getFrameByName("advancedMachine", 1));
		OreDictionary.registerOre("machineBlockElite", BlockMachineFrame.getFrameByName("highlyAdvancedMachine", 1));

		OreDictionary.registerOre("lapotronCrystal", ModItems.LAPOTRONIC_CRYSTAL);
		OreDictionary.registerOre("energyCrystal", ModItems.ENERGY_CRYSTAL);

		OreDictionary.registerOre("drillBasic", ModItems.DIAMOND_DRILL);
		OreDictionary.registerOre("drillDiamond", ModItems.DIAMOND_DRILL);

		OreDictionary.registerOre("industrialTnt", Blocks.TNT);
		OreDictionary.registerOre("craftingIndustrialDiamond", Items.DIAMOND);
		OreDictionary.registerOre("insulatedGoldCableItem", ItemStandaloneCables.getCableByName("insulatedgold"));
		OreDictionary.registerOre("fertilizer", new ItemStack(Items.DYE, 1, 15));

		OreDictionary.registerOre("ic2Generator", ModBlocks.SOLID_FUEL_GENEREATOR);
		OreDictionary.registerOre("ic2SolarPanel", ModBlocks.SOLAR_PANEL);
		OreDictionary.registerOre("ic2Macerator", ModBlocks.GRINDER);
		OreDictionary.registerOre("ic2Extractor", ModBlocks.EXTRACTOR);
		OreDictionary.registerOre("ic2Windmill", ModBlocks.WIND_MILL);
		OreDictionary.registerOre("ic2Watermill", ModBlocks.WATER_MILL);

		//OreDictionary.registerOre("uran235", nothing);
		//OreDictionary.registerOre("uran238", nothing);
		//OreDictionary.registerOre("smallUran235", nothing);

		OreDictionary.registerOre("fenceIron", ModBlocks.REFINED_IRON_FENCE);
		//TODO ic2 bug? Disabled as it crashes with this line
		//OreDictionary.registerOre("woodRubber", ModBlocks.RUBBER_LOG);
		OreDictionary.registerOre("glassReinforced", ModBlocks.REINFORCED_GLASS);

		OreDictionary.registerOre("craftingGrinder", ItemParts.getPartByName("diamondGrindingHead"));
		OreDictionary.registerOre("craftingGrinder", ItemParts.getPartByName("tungstenGrindingHead"));
		OreDictionary.registerOre("craftingSuperconductor", ItemParts.getPartByName("superconductor"));
		OreDictionary.registerOre("batteryUltimate", ItemParts.getPartByName("diamondGrindingHead"));

		OreDictionary.registerOre("materialResin", ItemParts.getPartByName("rubberSap"));
		OreDictionary.registerOre("materialRubber", ItemParts.getPartByName("rubber"));
		OreDictionary.registerOre("itemRubber", ItemParts.getPartByName("rubber"));
		OreDictionary.registerOre("pulpWood", ItemDusts.getDustByName("saw_dust"));

		for (String type : ItemGems.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "gem_" + type), ItemGems.getGemByName(type));
			boolean ignoreIt = false;
			for (String ignore : plateGenIgnores)
				if (type.startsWith(ignore))
					ignoreIt = true;
			if (!ignoreIt)
				ItemPlates.registerType(type);
		}

		for (String type : ItemIngots.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "ingot_" + type), ItemIngots.getIngotByName(type));
			boolean ignoreIt = false;
			for (String ignore : plateGenIgnores)
				if (type.startsWith(ignore))
					ignoreIt = true;
			if (!ignoreIt)
				ItemPlates.registerType(type);
		}

		for (String type : ItemPlates.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "plate_" + type), ItemPlates.getPlateByName(type));
		}

		for (String type : ItemDusts.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_" + type), ItemDusts.getDustByName(type));
		}

		for (String type : ItemDustsSmall.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_tiny_" + type), ItemDustsSmall.getSmallDustByName(type));
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_small_" + type), ItemDustsSmall.getSmallDustByName(type));
		}

		for (String type : ItemNuggets.types) {
			if (type.equals(ModItems.META_PLACEHOLDER))
				continue; //Aware of placeholders!
			OreDictionary.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "nugget_" + type), ItemNuggets.getNuggetByName(type));
		}

	}

}
