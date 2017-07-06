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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import reborncore.RebornRegistry;
import techreborn.blocks.fluid.BlockFluidBase;
import techreborn.blocks.fluid.BlockFluidTechReborn;
import techreborn.blocks.fluid.TechRebornFluid;
import techreborn.lib.ModInfo;

public class ModFluids {

	public static Fluid BERYLLIUM = new TechRebornFluid("fluidberylium");
	public static BlockFluidBase BLOCK_BERYLLIUM;

	public static Fluid CALCIUM = new TechRebornFluid("fluidcalcium");
	public static BlockFluidBase BLOCK_CALCIUM;

	public static Fluid CALCIUM_CARBONATE = new TechRebornFluid("fluidcalciumcarbonate");
	public static BlockFluidBase BLOCK_CALCIUM_CARBONATE;

	public static Fluid CHLORITE = new TechRebornFluid("fluidchlorite");
	public static BlockFluidBase BLOCK_CHLORITE;

	public static Fluid DEUTERIUM = new TechRebornFluid("fluiddeuterium");
	public static BlockFluidBase BLOCK_DEUTERIUM;

	public static Fluid GLYCERYL = new TechRebornFluid("fluidglyceryl");
	public static BlockFluidBase BLOCK_GLYCERYL;

	public static Fluid HELIUM = new TechRebornFluid("fluidhelium");
	public static BlockFluidBase BLOCK_HELIUM;

	public static Fluid HELIUM_3 = new TechRebornFluid("fluidhelium3");
	public static BlockFluidBase BLOCK_HELIUM_3;

	public static Fluid HELIUMPLASMA = new TechRebornFluid("fluidheliumplasma");
	public static BlockFluidBase BLOCK_HELIUMPLASMA;

	public static Fluid HYDROGEN = new TechRebornFluid("fluidhydrogen");
	public static BlockFluidBase BLOCK_HYDROGEN;

	public static Fluid LITHIUM = new TechRebornFluid("fluidlithium");
	public static BlockFluidBase BLOCK_LITHIUM;

	public static Fluid MERCURY = new TechRebornFluid("fluidmercury");
	public static BlockFluidBase BLOCK_MERCURY;

	public static Fluid METHANE = new TechRebornFluid("fluidmethane");
	public static BlockFluidBase BLOCK_METHANE;

	public static Fluid NITROCOAL_FUEL = new TechRebornFluid("fluidnitrocoalfuel");
	public static BlockFluidBase BLOCK_NITROCOAL_FUEL;

	public static Fluid NITROFUEL = new TechRebornFluid("fluidnitrofuel");
	public static BlockFluidBase BLOCK_NITROFUEL;

	public static Fluid NITROGEN = new TechRebornFluid("fluidnitrogen");
	public static BlockFluidBase BLOCK_NITROGEN;

	public static Fluid NITROGENDIOXIDE = new TechRebornFluid("fluidnitrogendioxide");
	public static BlockFluidBase BLOCK_NITROGENDIOXIDE;

	public static Fluid POTASSIUM = new TechRebornFluid("fluidpotassium");
	public static BlockFluidBase BLOCK_POTASSIUM;

	public static Fluid SILICON = new TechRebornFluid("fluidsilicon");
	public static BlockFluidBase BLOCK_SILICON;

	public static Fluid SODIUM = new TechRebornFluid("fluidsodium");
	public static BlockFluidBase BLOCK_SODIUM;

	public static Fluid SODIUMPERSULFATE = new TechRebornFluid("fluidsodiumpersulfate");
	public static BlockFluidBase BLOCK_SODIUMPERSULFATE;

	public static Fluid TRITIUM = new TechRebornFluid("fluidtritium");
	public static BlockFluidBase BLOCK_TRITIUM;

	public static Fluid WOLFRAMIUM = new TechRebornFluid("fluidwolframium");
	public static BlockFluidBase BLOCK_WOLFRAMIUM;

	public static Fluid CARBON = new TechRebornFluid("fluidcarbon");
	public static BlockFluidBase BLOCK_CARBON;

	public static Fluid CARBON_FIBER = new TechRebornFluid("fluidcarbonfiber");
	public static BlockFluidBase BLOCK_CARBON_FIBER;

	public static Fluid NITRO_CARBON = new TechRebornFluid("fluidnitrocarbon");
	public static BlockFluidBase BLOCK_NITRO_CARBON;

	public static Fluid SULFUR = new TechRebornFluid("fluidSulfur");
	public static BlockFluidBase BLOCK_SULFUR;

	public static Fluid SODIUM_SULFIDE = new TechRebornFluid("fluidsodiumSulfide");
	public static BlockFluidBase BLOCK_SODIUM_SULFIDE;

	public static Fluid DIESEL = new TechRebornFluid("fluiddiesel");
	public static BlockFluidBase BLOCK_DIESEL;

	public static Fluid NITRO_DIESEL = new TechRebornFluid("fluidnitrodiesel");
	public static BlockFluidBase BLOCK_NITRO_DIESEL;

	public static Fluid OIL = new TechRebornFluid("fluidoil");
	public static BlockFluidBase BLOCK_OIL;

	public static Fluid SULFURIC_ACID = new TechRebornFluid("fluidsulfuricacid");
	public static BlockFluidBase BLOCK_SULFURIC_ACID;

	public static Fluid COMPRESSED_AIR = new TechRebornFluid("fluidcompressedair");
	public static BlockFluidBase BLOCK_COMPRESSED_AIR;

	public static Fluid ELECTROLYZED_WATER = new TechRebornFluid("fluidelectrolyzedwater");
	public static BlockFluidBase BLOCK_ELECTROLYZED_WATER;

	public static void init() {
		FluidRegistry.registerFluid(BERYLLIUM);
		BLOCK_BERYLLIUM = new BlockFluidTechReborn(BERYLLIUM, Material.WATER, "techreborn.berylium");
		registerBlock(BLOCK_BERYLLIUM,
			ModInfo.MOD_ID + "_" + BLOCK_BERYLLIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(CALCIUM);
		BLOCK_CALCIUM = new BlockFluidTechReborn(CALCIUM, Material.WATER, "techreborn.calcium");
		registerBlock(BLOCK_CALCIUM,
			ModInfo.MOD_ID + "_" + BLOCK_CALCIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(CALCIUM_CARBONATE);
		BLOCK_CALCIUM_CARBONATE = new BlockFluidTechReborn(CALCIUM_CARBONATE, Material.WATER,
			"techreborn.calciumcarbonate");
		registerBlock(BLOCK_CALCIUM_CARBONATE,
			ModInfo.MOD_ID + "_" + BLOCK_CALCIUM_CARBONATE.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(CHLORITE);
		BLOCK_CHLORITE = new BlockFluidTechReborn(CHLORITE, Material.WATER, "techreborn.chlorite");
		registerBlock(BLOCK_CHLORITE,
			ModInfo.MOD_ID + "_" + BLOCK_CHLORITE.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(DEUTERIUM);
		BLOCK_DEUTERIUM = new BlockFluidTechReborn(DEUTERIUM, Material.WATER, "techreborn.deuterium");
		registerBlock(BLOCK_DEUTERIUM,
			ModInfo.MOD_ID + "_" + BLOCK_DEUTERIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(GLYCERYL);
		BLOCK_GLYCERYL = new BlockFluidTechReborn(GLYCERYL, Material.WATER, "techreborn.glyceryl");
		registerBlock(BLOCK_GLYCERYL,
			ModInfo.MOD_ID + "_" + BLOCK_GLYCERYL.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(HELIUM);
		BLOCK_HELIUM = new BlockFluidTechReborn(HELIUM, Material.WATER, "techreborn.helium");
		registerBlock(BLOCK_HELIUM,
			ModInfo.MOD_ID + "_" + BLOCK_HELIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(HELIUM_3);
		BLOCK_HELIUM_3 = new BlockFluidTechReborn(HELIUM_3, Material.WATER, "techreborn.helium3");
		registerBlock(BLOCK_HELIUM_3,
			ModInfo.MOD_ID + "_" + BLOCK_HELIUM_3.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(HELIUMPLASMA);
		BLOCK_HELIUMPLASMA = new BlockFluidTechReborn(HELIUMPLASMA, Material.WATER, "techreborn.heliumplasma");
		registerBlock(BLOCK_HELIUMPLASMA,
			ModInfo.MOD_ID + "_" + BLOCK_HELIUMPLASMA.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(HYDROGEN);
		BLOCK_HYDROGEN = new BlockFluidTechReborn(HYDROGEN, Material.WATER, "techreborn.hydrogen");
		registerBlock(BLOCK_HYDROGEN,
			ModInfo.MOD_ID + "_" + BLOCK_HYDROGEN.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(LITHIUM);
		BLOCK_LITHIUM = new BlockFluidTechReborn(LITHIUM, Material.WATER, "techreborn.lithium");
		registerBlock(BLOCK_LITHIUM,
			ModInfo.MOD_ID + "_" + BLOCK_LITHIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(MERCURY);
		BLOCK_MERCURY = new BlockFluidTechReborn(MERCURY, Material.WATER, "techreborn.mercury");
		registerBlock(BLOCK_MERCURY,
			ModInfo.MOD_ID + "_" + BLOCK_MERCURY.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(METHANE);
		BLOCK_METHANE = new BlockFluidTechReborn(METHANE, Material.WATER, "techreborn.methane");
		registerBlock(BLOCK_METHANE,
			ModInfo.MOD_ID + "_" + BLOCK_METHANE.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(NITROCOAL_FUEL);
		BLOCK_NITROCOAL_FUEL = new BlockFluidTechReborn(NITROCOAL_FUEL, Material.WATER,
			"techreborn.nitrocoalfuel");
		registerBlock(BLOCK_NITROCOAL_FUEL,
			ModInfo.MOD_ID + "_" + BLOCK_NITROCOAL_FUEL.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(NITROFUEL);
		BLOCK_NITROFUEL = new BlockFluidTechReborn(NITROFUEL, Material.WATER, "techreborn.nitrofuel");
		registerBlock(BLOCK_NITROFUEL,
			ModInfo.MOD_ID + "_" + BLOCK_NITROFUEL.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(NITROGEN);
		BLOCK_NITROGEN = new BlockFluidTechReborn(NITROGEN, Material.WATER, "techreborn.nitrogen");
		registerBlock(BLOCK_NITROGEN,
			ModInfo.MOD_ID + "_" + BLOCK_NITROGEN.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(NITROGENDIOXIDE);
		BLOCK_NITROGENDIOXIDE = new BlockFluidTechReborn(NITROGENDIOXIDE, Material.WATER,
			"techreborn.nitrogendioxide");
		registerBlock(BLOCK_NITROGENDIOXIDE,
			ModInfo.MOD_ID + "_" + BLOCK_NITROGENDIOXIDE.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(POTASSIUM);
		BLOCK_POTASSIUM = new BlockFluidTechReborn(POTASSIUM, Material.WATER, "techreborn.potassium");
		registerBlock(BLOCK_POTASSIUM,
			ModInfo.MOD_ID + "_" + BLOCK_POTASSIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(SILICON);
		BLOCK_SILICON = new BlockFluidTechReborn(SILICON, Material.WATER, "techreborn.silicon");
		registerBlock(BLOCK_SILICON,
			ModInfo.MOD_ID + "_" + BLOCK_SILICON.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(SODIUM);
		BLOCK_SODIUM = new BlockFluidTechReborn(SODIUM, Material.WATER, "techreborn.sodium");
		registerBlock(BLOCK_SODIUM,
			ModInfo.MOD_ID + "_" + BLOCK_SODIUM.getUnlocalizedName().substring(5));


		FluidRegistry.registerFluid(SODIUMPERSULFATE);
		BLOCK_SODIUMPERSULFATE = new BlockFluidTechReborn(SODIUMPERSULFATE, Material.WATER,
			"techreborn.sodiumpersulfate");
		registerBlock(BLOCK_SODIUMPERSULFATE,
			ModInfo.MOD_ID + "_" + BLOCK_SODIUMPERSULFATE.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(TRITIUM);
		BLOCK_TRITIUM = new BlockFluidTechReborn(TRITIUM, Material.WATER, "techreborn.tritium");
		registerBlock(BLOCK_TRITIUM,
			ModInfo.MOD_ID + "_" + BLOCK_TRITIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(WOLFRAMIUM);
		BLOCK_WOLFRAMIUM = new BlockFluidTechReborn(WOLFRAMIUM, Material.WATER, "techreborn.wolframium");
		registerBlock(BLOCK_WOLFRAMIUM,
			ModInfo.MOD_ID + "_" + BLOCK_WOLFRAMIUM.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(CARBON);
		BLOCK_CARBON = new BlockFluidTechReborn(CARBON, Material.WATER, "techreborn.carbon");
		registerBlock(BLOCK_CARBON,
			ModInfo.MOD_ID + "_" + BLOCK_CARBON.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(CARBON_FIBER);
		BLOCK_CARBON_FIBER = new BlockFluidTechReborn(CARBON_FIBER, Material.WATER, "techreborn.carbonfiber");
		registerBlock(BLOCK_CARBON_FIBER,
			ModInfo.MOD_ID + "_" + BLOCK_CARBON_FIBER.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(NITRO_CARBON);
		BLOCK_NITRO_CARBON = new BlockFluidTechReborn(NITRO_CARBON, Material.WATER, "techreborn.nitrocarbon");
		registerBlock(BLOCK_NITRO_CARBON,
			ModInfo.MOD_ID + "_" + BLOCK_NITRO_CARBON.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(SULFUR);
		BLOCK_SULFUR = new BlockFluidTechReborn(SULFUR, Material.WATER, "techreborn.sulfur");
		registerBlock(BLOCK_SULFUR,
			ModInfo.MOD_ID + "_" + BLOCK_SULFUR.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(SODIUM_SULFIDE);
		BLOCK_SODIUM_SULFIDE = new BlockFluidTechReborn(SODIUM_SULFIDE, Material.WATER, "techreborn.sodiumsulfide");
		registerBlock(BLOCK_SODIUM_SULFIDE,
			ModInfo.MOD_ID + "_" + BLOCK_SODIUM_SULFIDE.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(DIESEL);
		BLOCK_DIESEL = new BlockFluidTechReborn(DIESEL, Material.WATER, "techreborn.diesel");
		registerBlock(BLOCK_DIESEL,
			ModInfo.MOD_ID + "_" + BLOCK_DIESEL.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(NITRO_DIESEL);
		BLOCK_NITRO_DIESEL = new BlockFluidTechReborn(NITRO_DIESEL, Material.WATER, "techreborn.nitrodiesel");
		registerBlock(BLOCK_NITRO_DIESEL,
			ModInfo.MOD_ID + "_" + BLOCK_NITRO_DIESEL.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(OIL);
		BLOCK_OIL = new BlockFluidTechReborn(OIL, Material.WATER, "techreborn.oil");
		registerBlock(BLOCK_OIL,
			ModInfo.MOD_ID + "_" + BLOCK_OIL.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(SULFURIC_ACID);
		BLOCK_SULFURIC_ACID = new BlockFluidTechReborn(SULFURIC_ACID, Material.WATER, "techreborn.sulfuricacid");
		registerBlock(BLOCK_SULFURIC_ACID,
			ModInfo.MOD_ID + "_" + BLOCK_SULFURIC_ACID.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(COMPRESSED_AIR);
		BLOCK_COMPRESSED_AIR = new BlockFluidTechReborn(COMPRESSED_AIR, Material.WATER, "techreborn.compressedair");
		registerBlock(BLOCK_COMPRESSED_AIR,
			ModInfo.MOD_ID + "_" + BLOCK_COMPRESSED_AIR.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(ELECTROLYZED_WATER);
		BLOCK_ELECTROLYZED_WATER = new BlockFluidTechReborn(ELECTROLYZED_WATER, Material.WATER, "techreborn.electrolyzedwater");
		registerBlock(BLOCK_ELECTROLYZED_WATER,
			ModInfo.MOD_ID + "_" + BLOCK_ELECTROLYZED_WATER.getUnlocalizedName().substring(5));
	}

	public static void registerBlock(Block block, String name) {
		RebornRegistry.registerBlock(block, name);
	}
}
