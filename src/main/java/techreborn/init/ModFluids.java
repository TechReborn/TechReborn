package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.api.fuel.FluidPowerManager;
import techreborn.blocks.fluid.BlockFluidBase;
import techreborn.blocks.fluid.BlockFluidTechReborn;
import techreborn.blocks.fluid.TechRebornFluid;
import techreborn.lib.ModInfo;

public class ModFluids {

	public static Fluid fluidberylium = new TechRebornFluid("fluidberylium");
	public static BlockFluidBase BlockFluidBerylium;

	public static Fluid fluidcalcium = new TechRebornFluid("fluidcalcium");
	public static BlockFluidBase BlockFluidCalcium;

	public static Fluid fluidcalciumcarbonate = new TechRebornFluid("fluidcalciumcarbonate");
	public static BlockFluidBase BlockFluidCalciumCarbonate;

	public static Fluid fluidChlorite = new TechRebornFluid("fluidchlorite");
	public static BlockFluidBase BlockFluidChlorite;

	public static Fluid fluidDeuterium = new TechRebornFluid("fluiddeuterium");
	public static BlockFluidBase BlockFluidDeuterium;

	public static Fluid fluidGlyceryl = new TechRebornFluid("fluidglyceryl");
	public static BlockFluidBase BlockFluidGlyceryl;

	public static Fluid fluidHelium = new TechRebornFluid("fluidhelium");
	public static BlockFluidBase BlockFluidHelium;

	public static Fluid fluidHelium3 = new TechRebornFluid("fluidhelium3");
	public static BlockFluidBase BlockFluidHelium3;

	public static Fluid fluidHeliumplasma = new TechRebornFluid("fluidheliumplasma");
	public static BlockFluidBase BlockFluidHeliumplasma;

	public static Fluid fluidHydrogen = new TechRebornFluid("fluidhydrogen");
	public static BlockFluidBase BlockFluidHydrogen;

	public static Fluid fluidLithium = new TechRebornFluid("fluidlithium");
	public static BlockFluidBase BlockFluidLithium;

	public static Fluid fluidMercury = new TechRebornFluid("fluidmercury");
	public static BlockFluidBase BlockFluidMercury;

	public static Fluid fluidMethane = new TechRebornFluid("fluidmethane");
	public static BlockFluidBase BlockFluidMethane;

	public static Fluid fluidNitrocoalfuel = new TechRebornFluid("fluidnitrocoalfuel");
	public static BlockFluidBase BlockFluidNitrocoalfuel;

	public static Fluid fluidNitrofuel = new TechRebornFluid("fluidnitrofuel");
	public static BlockFluidBase BlockFluidNitrofuel;

	public static Fluid fluidNitrogen = new TechRebornFluid("fluidnitrogen");
	public static BlockFluidBase BlockFluidNitrogen;

	public static Fluid fluidNitrogendioxide = new TechRebornFluid("fluidnitrogendioxide");
	public static BlockFluidBase BlockFluidNitrogendioxide;

	public static Fluid fluidPotassium = new TechRebornFluid("fluidpotassium");
	public static BlockFluidBase BlockFluidPotassium;

	public static Fluid fluidSilicon = new TechRebornFluid("fluidsilicon");
	public static BlockFluidBase BlockFluidSilicon;

	public static Fluid fluidSodium = new TechRebornFluid("fluidsodium");
	public static BlockFluidBase BlockFluidSodium;

	public static Fluid fluidSodiumpersulfate = new TechRebornFluid("fluidsodiumpersulfate");
	public static BlockFluidBase BlockFluidSodiumpersulfate;

	public static Fluid fluidTritium = new TechRebornFluid("fluidtritium");
	public static BlockFluidBase BlockFluidTritium;

	public static Fluid fluidWolframium = new TechRebornFluid("fluidwolframium");
	public static BlockFluidBase BlockFluidWolframium;

	public static Fluid fluidCarbon = new TechRebornFluid("fluidcarbon");
	public static BlockFluidBase BlockFluidCarbon;

	public static Fluid fluidCarbonFiber = new TechRebornFluid("fluidcarbonfiber");
	public static BlockFluidBase BlockFluidCarbonFiber;

	public static Fluid fluidNitroCarbon = new TechRebornFluid("fluidnitrocarbon");
	public static BlockFluidBase BlockFluidNitroCarbon;

	public static Fluid fluidSulfur = new TechRebornFluid("fluidSulfur");
	public static BlockFluidBase BlockFluidSulfur;

	public static Fluid fluidSodiumSulfide = new TechRebornFluid("fluidsodiumSulfide");
	public static BlockFluidBase BlockFluidSodiumSulfide;

	public static Fluid fluidDiesel = new TechRebornFluid("fluiddiesel");
	public static BlockFluidBase BlockFluidDiesel;

	public static Fluid fluidNitroDiesel = new TechRebornFluid("fluidnitrodiesel");
	public static BlockFluidBase BlockFluidNitroDiesel;

	public static Fluid fluidOil = new TechRebornFluid("fluidoil");
	public static BlockFluidBase BlockFluidOil;

	public static Fluid fluidSulfuricAcid = new TechRebornFluid("fluidsulfuricacid");
	public static BlockFluidBase BlockFluidSulfuricAcid;

	public static void init() {
		FluidRegistry.registerFluid(fluidberylium);
		BlockFluidBerylium = new BlockFluidTechReborn(fluidberylium, Material.WATER, "techreborn.berylium");
		registerBlock(BlockFluidBerylium,
			ModInfo.MOD_ID + "_" + BlockFluidBerylium.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidcalcium);
		BlockFluidCalcium = new BlockFluidTechReborn(fluidcalcium, Material.WATER, "techreborn.calcium");
		registerBlock(BlockFluidCalcium,
			ModInfo.MOD_ID + "_" + BlockFluidCalcium.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidcalciumcarbonate);
		BlockFluidCalciumCarbonate = new BlockFluidTechReborn(fluidcalciumcarbonate, Material.WATER,
			"techreborn.calciumcarbonate");
		registerBlock(BlockFluidCalciumCarbonate,
			ModInfo.MOD_ID + "_" + BlockFluidCalciumCarbonate.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidChlorite);
		BlockFluidChlorite = new BlockFluidTechReborn(fluidChlorite, Material.WATER, "techreborn.chlorite");
		registerBlock(BlockFluidChlorite,
			ModInfo.MOD_ID + "_" + BlockFluidChlorite.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidDeuterium);
		BlockFluidDeuterium = new BlockFluidTechReborn(fluidDeuterium, Material.WATER, "techreborn.deuterium");
		registerBlock(BlockFluidDeuterium,
			ModInfo.MOD_ID + "_" + BlockFluidDeuterium.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidGlyceryl);
		BlockFluidGlyceryl = new BlockFluidTechReborn(fluidGlyceryl, Material.WATER, "techreborn.glyceryl");
		registerBlock(BlockFluidGlyceryl,
			ModInfo.MOD_ID + "_" + BlockFluidGlyceryl.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidHelium);
		BlockFluidHelium = new BlockFluidTechReborn(fluidHelium, Material.WATER, "techreborn.helium");
		registerBlock(BlockFluidHelium,
			ModInfo.MOD_ID + "_" + BlockFluidHelium.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidHelium3);
		BlockFluidHelium3 = new BlockFluidTechReborn(fluidHelium3, Material.WATER, "techreborn.helium3");
		registerBlock(BlockFluidHelium3,
			ModInfo.MOD_ID + "_" + BlockFluidHelium3.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidHeliumplasma);
		BlockFluidHeliumplasma = new BlockFluidTechReborn(fluidHeliumplasma, Material.WATER, "techreborn.heliumplasma");
		registerBlock(BlockFluidHeliumplasma,
			ModInfo.MOD_ID + "_" + BlockFluidHeliumplasma.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidHydrogen);
		BlockFluidHydrogen = new BlockFluidTechReborn(fluidHydrogen, Material.WATER, "techreborn.hydrogen");
		registerBlock(BlockFluidHydrogen,
			ModInfo.MOD_ID + "_" + BlockFluidHydrogen.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidLithium);
		BlockFluidLithium = new BlockFluidTechReborn(fluidLithium, Material.WATER, "techreborn.lithium");
		registerBlock(BlockFluidLithium,
			ModInfo.MOD_ID + "_" + BlockFluidLithium.getUnlocalizedName().substring(5));
		FluidPowerManager.fluidPowerValues.put(fluidLithium, 24.0);

		FluidRegistry.registerFluid(fluidMercury);
		BlockFluidMercury = new BlockFluidTechReborn(fluidMercury, Material.WATER, "techreborn.mercury");
		registerBlock(BlockFluidMercury,
			ModInfo.MOD_ID + "_" + BlockFluidMercury.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidMethane);
		BlockFluidMethane = new BlockFluidTechReborn(fluidMethane, Material.WATER, "techreborn.methane");
		registerBlock(BlockFluidMethane,
			ModInfo.MOD_ID + "_" + BlockFluidMethane.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidNitrocoalfuel);
		BlockFluidNitrocoalfuel = new BlockFluidTechReborn(fluidNitrocoalfuel, Material.WATER,
			"techreborn.nitrocoalfuel");
		registerBlock(BlockFluidNitrocoalfuel,
			ModInfo.MOD_ID + "_" + BlockFluidNitrocoalfuel.getUnlocalizedName().substring(5));
		FluidPowerManager.fluidPowerValues.put(fluidNitrocoalfuel, 48.0);

		FluidRegistry.registerFluid(fluidNitrofuel);
		BlockFluidNitrofuel = new BlockFluidTechReborn(fluidNitrofuel, Material.WATER, "techreborn.nitrofuel");
		registerBlock(BlockFluidNitrofuel,
			ModInfo.MOD_ID + "_" + BlockFluidNitrofuel.getUnlocalizedName().substring(5));
		FluidPowerManager.fluidPowerValues.put(fluidNitrofuel, 42.0);

		FluidRegistry.registerFluid(fluidNitrogen);
		BlockFluidNitrogen = new BlockFluidTechReborn(fluidNitrogen, Material.WATER, "techreborn.nitrogen");
		registerBlock(BlockFluidNitrogen,
			ModInfo.MOD_ID + "_" + BlockFluidNitrogen.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidNitrogendioxide);
		BlockFluidNitrogendioxide = new BlockFluidTechReborn(fluidNitrogendioxide, Material.WATER,
			"techreborn.nitrogendioxide");
		registerBlock(BlockFluidNitrogendioxide,
			ModInfo.MOD_ID + "_" + BlockFluidNitrogendioxide.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidPotassium);
		BlockFluidPotassium = new BlockFluidTechReborn(fluidPotassium, Material.WATER, "techreborn.potassium");
		registerBlock(BlockFluidPotassium,
			ModInfo.MOD_ID + "_" + BlockFluidPotassium.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidSilicon);
		BlockFluidSilicon = new BlockFluidTechReborn(fluidSilicon, Material.WATER, "techreborn.silicon");
		registerBlock(BlockFluidSilicon,
			ModInfo.MOD_ID + "_" + BlockFluidSilicon.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidSodium);
		BlockFluidSodium = new BlockFluidTechReborn(fluidSodium, Material.WATER, "techreborn.sodium");
		registerBlock(BlockFluidSodium,
			ModInfo.MOD_ID + "_" + BlockFluidSodium.getUnlocalizedName().substring(5));
		FluidPowerManager.fluidPowerValues.put(fluidNitrofuel, 22.0);

		FluidRegistry.registerFluid(fluidSodiumpersulfate);
		BlockFluidSodiumpersulfate = new BlockFluidTechReborn(fluidSodiumpersulfate, Material.WATER,
			"techreborn.sodiumpersulfate");
		registerBlock(BlockFluidSodiumpersulfate,
			ModInfo.MOD_ID + "_" + BlockFluidSodiumpersulfate.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidTritium);
		BlockFluidTritium = new BlockFluidTechReborn(fluidTritium, Material.WATER, "techreborn.tritium");
		registerBlock(BlockFluidTritium,
			ModInfo.MOD_ID + "_" + BlockFluidTritium.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidWolframium);
		BlockFluidWolframium = new BlockFluidTechReborn(fluidWolframium, Material.WATER, "techreborn.wolframium");
		registerBlock(BlockFluidWolframium,
			ModInfo.MOD_ID + "_" + BlockFluidWolframium.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidCarbon);
		BlockFluidCarbon = new BlockFluidTechReborn(fluidCarbon, Material.WATER, "techreborn.carbon");
		registerBlock(BlockFluidCarbon,
			ModInfo.MOD_ID + "_" + BlockFluidCarbon.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidCarbonFiber);
		BlockFluidCarbonFiber = new BlockFluidTechReborn(fluidCarbonFiber, Material.WATER, "techreborn.carbonfiber");
		registerBlock(BlockFluidCarbonFiber,
			ModInfo.MOD_ID + "_" + BlockFluidCarbonFiber.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidNitroCarbon);
		BlockFluidNitroCarbon = new BlockFluidTechReborn(fluidNitroCarbon, Material.WATER, "techreborn.nitrocarbon");
		registerBlock(BlockFluidNitroCarbon,
			ModInfo.MOD_ID + "_" + BlockFluidNitroCarbon.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidSulfur);
		BlockFluidSulfur = new BlockFluidTechReborn(fluidSulfur, Material.WATER, "techreborn.sulfur");
		registerBlock(BlockFluidSulfur,
			ModInfo.MOD_ID + "_" + BlockFluidSulfur.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidSodiumSulfide);
		BlockFluidSodiumSulfide = new BlockFluidTechReborn(fluidSodiumSulfide, Material.WATER, "techreborn.sodiumsulfide");
		registerBlock(BlockFluidSodiumSulfide,
			ModInfo.MOD_ID + "_" + BlockFluidSodiumSulfide.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidDiesel);
		BlockFluidDiesel = new BlockFluidTechReborn(fluidDiesel, Material.WATER, "techreborn.diesel");
		registerBlock(BlockFluidDiesel,
			ModInfo.MOD_ID + "_" + BlockFluidDiesel.getUnlocalizedName().substring(5));

		FluidRegistry.registerFluid(fluidNitroDiesel);
		BlockFluidNitroDiesel = new BlockFluidTechReborn(fluidNitroDiesel, Material.WATER, "techreborn.nitrodiesel");
		registerBlock(BlockFluidNitroDiesel,
			ModInfo.MOD_ID + "_" + BlockFluidNitroDiesel.getUnlocalizedName().substring(5));
		FluidPowerManager.fluidPowerValues.put(fluidNitroDiesel, 36.0);

		FluidRegistry.registerFluid(fluidOil);
		BlockFluidOil = new BlockFluidTechReborn(fluidOil, Material.WATER, "techreborn.oil");
		registerBlock(BlockFluidOil,
			ModInfo.MOD_ID + "_" + BlockFluidOil.getUnlocalizedName().substring(5));
		FluidPowerManager.fluidPowerValues.put(fluidOil, 16.0);

		FluidRegistry.registerFluid(fluidSulfuricAcid);
		BlockFluidSulfuricAcid = new BlockFluidTechReborn(fluidSulfuricAcid, Material.WATER, "techreborn.sulfuricacid");
		registerBlock(BlockFluidSulfuricAcid,
			ModInfo.MOD_ID + "_" + BlockFluidSulfuricAcid.getUnlocalizedName().substring(5));
	}

	public static void registerBlock(Block block, String name) {
		block.setRegistryName(name);
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block), block.getRegistryName());
	}
}
