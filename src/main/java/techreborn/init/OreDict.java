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

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.common.util.OreUtil;
import techreborn.blocks.BlockMachineFrames;
import techreborn.blocks.cable.BlockCable;
import techreborn.items.*;

public class OreDict {

	private static final ImmutableList<String> plateGenIgnores = ImmutableList.of(
		"hot", //Hot ingots
		"mixed_metal", //Mixed metal has own version of plate
		"iridium_alloy" //Iridium alloy is plate itself
	);

	public static void init() {
		OreUtil.registerOre("reBattery", ModItems.RE_BATTERY);

		OreUtil.registerOre("circuitBasic", ItemParts.getPartByName("electronicCircuit"));
		OreUtil.registerOre("circuitAdvanced", ItemParts.getPartByName("advancedCircuit"));
		OreUtil.registerOre("circuitStorage", ItemParts.getPartByName("dataStorageCircuit"));
		OreUtil.registerOre("circuitElite", ItemParts.getPartByName("dataControlCircuit"));
		OreUtil.registerOre("circuitMaster", ItemParts.getPartByName("energyFlowCircuit"));

		OreUtil.registerOre("machineBlockBasic", BlockMachineFrames.getFrameByName("machine", 1));
		OreUtil.registerOre("machineBlockAdvanced", BlockMachineFrames.getFrameByName("advancedMachine", 1));
		OreUtil.registerOre("machineBlockElite", BlockMachineFrames.getFrameByName("highlyAdvancedMachine", 1));

		OreUtil.registerOre("lapotronCrystal", ModItems.LAPOTRONIC_CRYSTAL);
		OreUtil.registerOre("energyCrystal", ModItems.ENERGY_CRYSTAL);

		OreUtil.registerOre("drillBasic", ModItems.STEEL_DRILL);
		OreUtil.registerOre("drillDiamond", ModItems.DIAMOND_DRILL);

		OreUtil.registerOre("industrialTnt", Blocks.TNT);
		OreUtil.registerOre("craftingIndustrialDiamond", Items.DIAMOND);
		OreUtil.registerOre("insulatedGoldCableItem", BlockCable.getCableByName("insulatedgold"));
		OreUtil.registerOre("fertilizer", new ItemStack(Items.DYE, 1, 15));

		//OreUtil.registerOre("uran235", nothing);
		//OreUtil.registerOre("uran238", nothing);
		//OreUtil.registerOre("smallUran235", nothing);

		OreUtil.registerOre("fenceIron", ModBlocks.REFINED_IRON_FENCE);
		OreUtil.registerOre("woodRubber", ModBlocks.RUBBER_LOG);
		OreUtil.registerOre("glassReinforced", ModBlocks.REINFORCED_GLASS);

		OreUtil.registerOre("craftingDiamondGrinder", ItemParts.getPartByName("diamondGrindingHead"));
		OreUtil.registerOre("craftingTungstenGrinder", ItemParts.getPartByName("tungstenGrindingHead"));
		OreUtil.registerOre("craftingSuperconductor", ItemParts.getPartByName("superconductor"));

		OreUtil.registerOre("materialResin", ItemParts.getPartByName("rubberSap"));
		OreUtil.registerOre("materialRubber", ItemParts.getPartByName("rubber"));
		OreUtil.registerOre("itemRubber", ItemParts.getPartByName("rubber"));
		OreUtil.registerOre("pulpWood", ItemDusts.getDustByName("saw_dust"));
		OreUtil.registerOre("dustAsh", ItemDusts.getDustByName("ashes"));

		for (String type : ItemGems.types) {
			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "gem_" + type), ItemGems.getGemByName(type));
			boolean ignoreIt = false;
			for (String ignore : plateGenIgnores)
				if (type.startsWith(ignore))
					ignoreIt = true;
			// if (!ignoreIt)
			//	ItemPlates.registerType(type);
		}

		for (String type : ItemIngots.types) {
			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "ingot_" + type), ItemIngots.getIngotByName(type));
			boolean ignoreIt = false;
			for (String ignore : plateGenIgnores)
				if (type.startsWith(ignore))
					ignoreIt = true;
			// if (!ignoreIt)
			//	ItemPlates.registerType(type);
		}

//		for (String type : ItemPlates.types) {
//			// TODO: fix recipe
//			 OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "plate_" + type), ItemPlates.getPlateByName(type));
//		}
		
		for (String type : ItemDusts.types) {
			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_" + type), ItemDusts.getDustByName(type));
		}

		for (String type : ItemDustsSmall.types) {
			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "dust_small_" + type), ItemDustsSmall.getSmallDustByName(type));
		}

		for (String type : ItemNuggets.types) {
			OreUtil.registerOre(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "nugget_" + type), ItemNuggets.getNuggetByName(type));
		}

	}

}
