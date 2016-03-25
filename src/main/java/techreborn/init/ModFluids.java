package techreborn.init;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.api.fuel.FluidPowerManager;
import techreborn.blocks.fluid.BlockFluidBase;
import techreborn.blocks.fluid.BlockFluidTechReborn;
import techreborn.blocks.fluid.TechRebornFluid;
import techreborn.lib.ModInfo;

public class ModFluids
{

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

	public static void init()
	{
		FluidRegistry.registerFluid(fluidberylium);
		BlockFluidBerylium = new BlockFluidTechReborn(fluidberylium, Material.water, "techreborn.berylium");
		GameRegistry.registerBlock(BlockFluidBerylium,
				ModInfo.MOD_ID + "_" + BlockFluidBerylium.getUnlocalizedName().substring(5));
		fluidberylium.setUnlocalizedName(BlockFluidBerylium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidcalcium);
		BlockFluidCalcium = new BlockFluidTechReborn(fluidcalcium, Material.water, "techreborn.calcium");
		GameRegistry.registerBlock(BlockFluidCalcium,
				ModInfo.MOD_ID + "_" + BlockFluidCalcium.getUnlocalizedName().substring(5));
		fluidcalcium.setUnlocalizedName(BlockFluidCalcium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidcalciumcarbonate);
		BlockFluidCalciumCarbonate = new BlockFluidTechReborn(fluidcalciumcarbonate, Material.water,
				"techreborn.calciumcarbonate");
		GameRegistry.registerBlock(BlockFluidCalciumCarbonate,
				ModInfo.MOD_ID + "_" + BlockFluidCalciumCarbonate.getUnlocalizedName().substring(5));
		fluidcalcium.setUnlocalizedName(BlockFluidCalciumCarbonate.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidChlorite);
		BlockFluidChlorite = new BlockFluidTechReborn(fluidChlorite, Material.water, "techreborn.chlorite");
		GameRegistry.registerBlock(BlockFluidChlorite,
				ModInfo.MOD_ID + "_" + BlockFluidChlorite.getUnlocalizedName().substring(5));
		fluidChlorite.setUnlocalizedName(BlockFluidChlorite.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidDeuterium);
		BlockFluidDeuterium = new BlockFluidTechReborn(fluidDeuterium, Material.water, "techreborn.deuterium");
		GameRegistry.registerBlock(BlockFluidDeuterium,
				ModInfo.MOD_ID + "_" + BlockFluidDeuterium.getUnlocalizedName().substring(5));
		fluidDeuterium.setUnlocalizedName(BlockFluidDeuterium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidGlyceryl);
		BlockFluidGlyceryl = new BlockFluidTechReborn(fluidGlyceryl, Material.water, "techreborn.glyceryl");
		GameRegistry.registerBlock(BlockFluidGlyceryl,
				ModInfo.MOD_ID + "_" + BlockFluidGlyceryl.getUnlocalizedName().substring(5));
		fluidGlyceryl.setUnlocalizedName(BlockFluidGlyceryl.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidHelium);
		BlockFluidHelium = new BlockFluidTechReborn(fluidHelium, Material.water, "techreborn.helium");
		GameRegistry.registerBlock(BlockFluidHelium,
				ModInfo.MOD_ID + "_" + BlockFluidHelium.getUnlocalizedName().substring(5));
		fluidHelium.setUnlocalizedName(BlockFluidHelium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidHelium3);
		BlockFluidHelium3 = new BlockFluidTechReborn(fluidHelium3, Material.water, "techreborn.helium3");
		GameRegistry.registerBlock(BlockFluidHelium3,
				ModInfo.MOD_ID + "_" + BlockFluidHelium3.getUnlocalizedName().substring(5));
		fluidHelium3.setUnlocalizedName(BlockFluidHelium3.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidHeliumplasma);
		BlockFluidHeliumplasma = new BlockFluidTechReborn(fluidHeliumplasma, Material.water, "techreborn.heliumplasma");
		GameRegistry.registerBlock(BlockFluidHeliumplasma,
				ModInfo.MOD_ID + "_" + BlockFluidHeliumplasma.getUnlocalizedName().substring(5));
		fluidHeliumplasma.setUnlocalizedName(fluidHeliumplasma.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidHydrogen);
		BlockFluidHydrogen = new BlockFluidTechReborn(fluidHydrogen, Material.water, "techreborn.hydrogen");
		GameRegistry.registerBlock(BlockFluidHydrogen,
				ModInfo.MOD_ID + "_" + BlockFluidHydrogen.getUnlocalizedName().substring(5));
		fluidHydrogen.setUnlocalizedName(fluidHydrogen.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidLithium);
		BlockFluidLithium = new BlockFluidTechReborn(fluidLithium, Material.water, "techreborn.lithium");
		GameRegistry.registerBlock(BlockFluidLithium,
				ModInfo.MOD_ID + "_" + BlockFluidLithium.getUnlocalizedName().substring(5));
		fluidLithium.setUnlocalizedName(fluidLithium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidMercury);
		BlockFluidMercury = new BlockFluidTechReborn(fluidMercury, Material.water, "techreborn.mercury");
		GameRegistry.registerBlock(BlockFluidMercury,
				ModInfo.MOD_ID + "_" + BlockFluidMercury.getUnlocalizedName().substring(5));
		fluidMercury.setUnlocalizedName(fluidMercury.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidMethane);
		BlockFluidMethane = new BlockFluidTechReborn(fluidMethane, Material.water, "techreborn.methane");
		GameRegistry.registerBlock(BlockFluidMethane,
				ModInfo.MOD_ID + "_" + BlockFluidMethane.getUnlocalizedName().substring(5));
		fluidMethane.setUnlocalizedName(fluidMethane.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidNitrocoalfuel);
		BlockFluidNitrocoalfuel = new BlockFluidTechReborn(fluidNitrocoalfuel, Material.water,
				"techreborn.nitrocoalfuel");
		GameRegistry.registerBlock(BlockFluidNitrocoalfuel,
				ModInfo.MOD_ID + "_" + BlockFluidNitrocoalfuel.getUnlocalizedName().substring(5));
		fluidNitrocoalfuel.setUnlocalizedName(fluidNitrocoalfuel.getUnlocalizedName());
		FluidPowerManager.fluidPowerValues.put(fluidNitrocoalfuel, 48.0);

		FluidRegistry.registerFluid(fluidNitrofuel);
		BlockFluidNitrofuel = new BlockFluidTechReborn(fluidNitrofuel, Material.water, "techreborn.nitrofuel");
		GameRegistry.registerBlock(BlockFluidNitrofuel,
				ModInfo.MOD_ID + "_" + BlockFluidNitrofuel.getUnlocalizedName().substring(5));
		fluidNitrofuel.setUnlocalizedName(fluidNitrofuel.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidNitrogen);
		BlockFluidNitrogen = new BlockFluidTechReborn(fluidNitrogen, Material.water, "techreborn.nitrogen");
		GameRegistry.registerBlock(BlockFluidNitrogen,
				ModInfo.MOD_ID + "_" + BlockFluidNitrogen.getUnlocalizedName().substring(5));
		fluidNitrogen.setUnlocalizedName(fluidNitrogen.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidNitrogendioxide);
		BlockFluidNitrogendioxide = new BlockFluidTechReborn(fluidNitrogendioxide, Material.water,
				"techreborn.nitrogendioxide");
		GameRegistry.registerBlock(BlockFluidNitrogendioxide,
				ModInfo.MOD_ID + "_" + BlockFluidNitrogendioxide.getUnlocalizedName().substring(5));
		fluidNitrogendioxide.setUnlocalizedName(fluidNitrogendioxide.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidPotassium);
		BlockFluidPotassium = new BlockFluidTechReborn(fluidPotassium, Material.water, "techreborn.potassium");
		GameRegistry.registerBlock(BlockFluidPotassium,
				ModInfo.MOD_ID + "_" + BlockFluidPotassium.getUnlocalizedName().substring(5));
		fluidPotassium.setUnlocalizedName(fluidPotassium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidSilicon);
		BlockFluidSilicon = new BlockFluidTechReborn(fluidSilicon, Material.water, "techreborn.silicon");
		GameRegistry.registerBlock(BlockFluidSilicon,
				ModInfo.MOD_ID + "_" + BlockFluidSilicon.getUnlocalizedName().substring(5));
		fluidSilicon.setUnlocalizedName(fluidSilicon.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidSodium);
		BlockFluidSodium = new BlockFluidTechReborn(fluidSodium, Material.water, "techreborn.sodium");
		GameRegistry.registerBlock(BlockFluidSodium,
				ModInfo.MOD_ID + "_" + BlockFluidSodium.getUnlocalizedName().substring(5));
		fluidSodium.setUnlocalizedName(fluidSodium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidSodiumpersulfate);
		BlockFluidSodiumpersulfate = new BlockFluidTechReborn(fluidSodiumpersulfate, Material.water,
				"techreborn.sodiumpersulfate");
		GameRegistry.registerBlock(BlockFluidSodiumpersulfate,
				ModInfo.MOD_ID + "_" + BlockFluidSodiumpersulfate.getUnlocalizedName().substring(5));
		fluidSodiumpersulfate.setUnlocalizedName(fluidSodiumpersulfate.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidTritium);
		BlockFluidTritium = new BlockFluidTechReborn(fluidTritium, Material.water, "techreborn.tritium");
		GameRegistry.registerBlock(BlockFluidTritium,
				ModInfo.MOD_ID + "_" + BlockFluidTritium.getUnlocalizedName().substring(5));
		fluidTritium.setUnlocalizedName(fluidTritium.getUnlocalizedName());

		FluidRegistry.registerFluid(fluidWolframium);
		BlockFluidWolframium = new BlockFluidTechReborn(fluidWolframium, Material.water, "techreborn.wolframium");
		GameRegistry.registerBlock(BlockFluidWolframium,
				ModInfo.MOD_ID + "_" + BlockFluidWolframium.getUnlocalizedName().substring(5));
		fluidWolframium.setUnlocalizedName(fluidWolframium.getUnlocalizedName());

	}
}
