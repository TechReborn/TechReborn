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

import ic2.core.block.BlockIC2Fence;
import ic2.core.block.BlockTexGlass;
import ic2.core.block.type.ResourceBlock;
import ic2.core.block.wiring.CableType;
import ic2.core.item.block.ItemCable;
import ic2.core.item.type.CraftingItemType;
import ic2.core.item.type.MiscResourceType;
import ic2.core.item.type.NuclearResourceType;
import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.OreUtil;
import techreborn.Core;
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.items.ItemParts;

/**
 * Created by modmuss50 on 16/07/2016.
 */
public class IC2Dict {

	//TODO IC2
	public static void init() {

		IC2Duplicates.GRINDER.setIc2Stack(BlockName.te.getItemStack(TeBlock.macerator.getName()));
		IC2Duplicates.ELECTRICAL_FURNACE.setIc2Stack(BlockName.te.getItemStack(TeBlock.electric_furnace.getName()));
		IC2Duplicates.IRON_FURNACE.setIc2Stack(BlockName.te.getItemStack(TeBlock.iron_furnace.getName()));
		IC2Duplicates.GENERATOR.setIc2Stack(BlockName.te.getItemStack(TeBlock.generator.getName()));
		IC2Duplicates.EXTRACTOR.setIc2Stack(BlockName.te.getItemStack(TeBlock.extractor.getName()));
		IC2Duplicates.RECYCLER.setIc2Stack(BlockName.te.getItemStack(TeBlock.recycler.getName()));
		IC2Duplicates.COMPRESSOR.setIc2Stack(BlockName.te.getItemStack(TeBlock.compressor.getName()));
		IC2Duplicates.BAT_BOX.setIc2Stack(BlockName.te.getItemStack(TeBlock.batbox.getName()));
		IC2Duplicates.MFE.setIc2Stack(BlockName.te.getItemStack(TeBlock.mfe.getName()));
		IC2Duplicates.MFSU.setIc2Stack(BlockName.te.getItemStack(TeBlock.mfsu.getName()));
		IC2Duplicates.LVT.setIc2Stack(BlockName.te.getItemStack(TeBlock.lv_transformer.getName()));
		IC2Duplicates.MVT.setIc2Stack(BlockName.te.getItemStack(TeBlock.mv_transformer.getName()));
		IC2Duplicates.HVT.setIc2Stack(BlockName.te.getItemStack(TeBlock.hv_transformer.getName()));
		IC2Duplicates.CABLE_COPPER.setIc2Stack(getIC2Cable(CableType.copper, 0));
		IC2Duplicates.CABLE_GOLD.setIc2Stack(getIC2Cable(CableType.gold, 0));
		IC2Duplicates.CABLE_ICOPPER.setIc2Stack(getIC2Cable(CableType.copper, 1));
		IC2Duplicates.CABLE_IGOLD.setIc2Stack(getIC2Cable(CableType.gold, 1));
		IC2Duplicates.CABLE_HV.setIc2Stack(getIC2Cable(CableType.tin, 0));
		IC2Duplicates.CABLE_IHV.setIc2Stack(getIC2Cable(CableType.tin, 1));
		IC2Duplicates.CABLE_IIHV.setIc2Stack(getIC2Cable(CableType.tin, 2));
		IC2Duplicates.CABLE_GLASSFIBER.setIc2Stack(getIC2Cable(CableType.glass, 0));

		IC2Duplicates.MIXED_METAL.setIc2Stack(ItemName.ingot.getItemStack("alloy"));
		IC2Duplicates.CARBON_FIBER.setIc2Stack(ItemName.crafting.getItemStack("carbon_fibre"));
		IC2Duplicates.CARBON_MESH.setIc2Stack(ItemName.crafting.getItemStack("carbon_mesh"));
		IC2Duplicates.REFINED_IRON.setIc2Stack(ItemName.plate.getItemStack("iron"));
		IC2Duplicates.SCRAP.setIc2Stack(ItemName.crafting.getItemStack(CraftingItemType.scrap));
		IC2Duplicates.FREQ_TRANSMITTER.setIc2Stack(ItemName.frequency_transmitter.getItemStack());
		//Rubber - ore dic: itemRubber, hidden from JEI
		//Rubber Sap - only used to make rubber, hidden from JEI
		//Rubber tree blocks, hidden when deduplication is on, and rubber tress are not set to gen, includes tree taps
		//Circuits are hidden in JEI

		RecipeHandler.addRecipe(
			new ExtractorRecipe(ItemName.misc_resource.getItemStack("resin"),
				ItemParts.getPartByName("rubber", 3), 400, 2));

		try {
			CraftingItemType.circuit.getName();

			OreUtil.registerOre("reBattery", ItemName.re_battery.getItemStack());

			OreUtil.registerOre("circuitBasic", ItemName.crafting.getItemStack(CraftingItemType.circuit));
			OreUtil.registerOre("circuitAdvanced", ItemName.crafting.getItemStack(CraftingItemType.advanced_circuit));

			OreUtil.registerOre("machineBlockBasic", BlockName.resource.getItemStack(ResourceBlock.machine));
			OreUtil.registerOre("machineBlockAdvanced", BlockName.resource.getItemStack(ResourceBlock.advanced_machine));

			OreUtil.registerOre("lapotronCrystal", ItemName.lapotron_crystal.getItemStack());
			OreUtil.registerOre("energyCrystal", ItemName.energy_crystal.getItemStack());

			OreUtil.registerOre("drillBasic", ItemName.drill.getItemStack());
			OreUtil.registerOre("drillDiamond", ItemName.diamond_drill.getItemStack());
			OreUtil.registerOre("drillAdvanced", ItemName.iridium_drill.getItemStack());

			ItemStack industrialTnt = BlockName.te.getItemStack(TeBlock.itnt);
			industrialTnt.setItemDamage(1);
			OreUtil.registerOre("industrialTnt", industrialTnt);

			OreUtil.registerOre("craftingIndustrialDiamond", ItemName.crafting.getItemStack(CraftingItemType.industrial_diamond));
			OreUtil.registerOre("fertilizer", ItemName.crafting.getItemStack(CraftingItemType.bio_chaff));
			OreUtil.registerOre("hvTransformer", BlockName.te.getItemStack(TeBlock.hv_transformer));

			OreUtil.registerOre("uran235", ItemName.nuclear.getItemStack(NuclearResourceType.uranium_235));
			OreUtil.registerOre("uran238", ItemName.nuclear.getItemStack(NuclearResourceType.uranium_238));
			OreUtil.registerOre("smallUran238", ItemName.nuclear.getItemStack(NuclearResourceType.small_uranium_238));
			OreUtil.registerOre("smallUran235", ItemName.nuclear.getItemStack(NuclearResourceType.small_uranium_235));

			OreUtil.registerOre("fenceIron", BlockName.fence.getItemStack(BlockIC2Fence.IC2FenceType.iron));
			OreUtil.registerOre("rubberWood", BlockName.rubber_wood.getItemStack());
			OreUtil.registerOre("glassReinforced", BlockName.glass.getItemStack(BlockTexGlass.GlassType.reinforced));

			OreUtil.registerOre("oreIridium", ItemName.misc_resource.getItemStack(MiscResourceType.iridium_ore));

			OreUtil.registerOre("plateIridiumAlloy", ItemName.crafting.getItemStack(CraftingItemType.iridium));

		} catch (NoClassDefFoundError notFound) {
			Core.logHelper.warn(
				"Can't enable integration: IC2 installed but cannot be hooked\n" +
					"Do you use incompatible IC2 version?\n" +
					"Please create issue on github and provide FULL LOG and mod list");
		} catch (Throwable error) {
			Core.logHelper.warn(
				"Exception thrown during IC2 integration init\n" +
					"Do you use incompatible IC2 version?\n" +
					"Please create issue on github and provide FULL LOG and mod list.\n" +
					"Error stack trace: ");
			error.printStackTrace();
		}
	}

	public static ItemStack getIC2Cable(CableType type, int insulation) {
		if (insulation > type.maxInsulation) {
			return null;
		}
		return ItemCable.getCable(type, insulation);
	}

}
