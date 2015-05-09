package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import techreborn.blocks.fluid.BlockFluidCalciumCarbonate;
import techreborn.blocks.fluid.BlockFluidBerylium;
import techreborn.blocks.fluid.BlockFluidChlorite;
import techreborn.blocks.fluid.BlockFluidDeuterium;
import techreborn.blocks.fluid.BlockFluidGlyceryl;
import techreborn.blocks.fluid.BlockFluidHelium;
import techreborn.blocks.fluid.BlockFluidHelium3;
import techreborn.blocks.fluid.BlockFluidHydrogen;
import techreborn.blocks.fluid.BlockFluidLithium;
import techreborn.blocks.fluid.BlockFluidMercury;
import techreborn.blocks.fluid.BlockFluidMethane;
import techreborn.blocks.fluid.BlockFluidNitrocoalfuel;
import techreborn.blocks.fluid.BlockFluidNitrofuel;
import techreborn.blocks.fluid.BlockFluidNitrogen;
import techreborn.blocks.fluid.BlockFluidNitrogendioxide;
import techreborn.blocks.fluid.BlockFluidPotassium;
import techreborn.blocks.fluid.BlockFluidSilicon;
import techreborn.blocks.fluid.BlockFluidSodium;
import techreborn.blocks.fluid.BlockFluidSodiumpersulfate;
import techreborn.blocks.fluid.BlockFluidTritium;
import techreborn.blocks.fluid.BlockFluidWolframium;
import techreborn.blocks.fluid.BlockFluidHeliumplasma;
import techreborn.lib.ModInfo;
import techreborn.util.BucketHandler;
import techreborn.blocks.fluid.BlockFluidCalcium;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
	
	public static Fluid fluidberylium = new Fluid("fluidberylium");
	public static Block BlockFluidBerylium;
	
	public static Fluid fluidcalcium = new Fluid("fluidcalcium");
	public static Block BlockFluidCalcium;
	
	public static Fluid fluidcalciumcarbonate = new Fluid("fluidcalciumcarbonate");
	public static Block BlockFluidCalciumCarbonate;
	
	public static Fluid fluidChlorite = new Fluid("fluidchlorite");
	public static Block BlockFluidChlorite;
	
	public static Fluid fluidDeuterium = new Fluid("fluiddeuterium");
	public static Block BlockFluidDeuterium;
	
	public static Fluid fluidGlyceryl = new Fluid("fluidglyceryl");
	public static Block BlockFluidGlyceryl;
	
	public static Fluid fluidHelium = new Fluid("fluidhelium");
	public static Block BlockFluidHelium;
	
	public static Fluid fluidHelium3 = new Fluid("fluidhelium3");
	public static Block BlockFluidHelium3;
	
	public static Fluid fluidHeliumplasma = new Fluid("fluidheliumplasma");
	public static Block BlockFluidHeliumplasma;
	
	public static Fluid fluidHydrogen = new Fluid("fluidhydrogen");
	public static Block BlockFluidHydrogen;
	
	public static Fluid fluidLithium = new Fluid("fluidlithium");
	public static Block BlockFluidLithium;
	
	public static Fluid fluidMercury = new Fluid("fluidmercury");
	public static Block BlockFluidMercury;
	
	public static Fluid fluidMethane = new Fluid("fluidmethane");
	public static Block BlockFluidMethane;
	
	public static Fluid fluidNitrocoalfuel = new Fluid("fluidnitrocoalfuel");
	public static Block BlockFluidNitrocoalfuel;
	
	public static Fluid fluidNitrofuel = new Fluid("fluidnitrofuel");
	public static Block BlockFluidNitrofuel;
	
	public static Fluid fluidNitrogen = new Fluid("fluidnitrogen");
	public static Block BlockFluidNitrogen;
	
	public static Fluid fluidNitrogendioxide = new Fluid("fluidnitrogendioxide");
	public static Block BlockFluidNitrogendioxide;
	
	public static Fluid fluidPotassium = new Fluid("fluidpotassium");
	public static Block BlockFluidPotassium;
	
	public static Fluid fluidSilicon = new Fluid("fluidsilicon");
	public static Block BlockFluidSilicon;
	
	public static Fluid fluidSodium = new Fluid("fluidsodium");
	public static Block BlockFluidSodium;
	
	public static Fluid fluidSodiumpersulfate = new Fluid("fluidsodiumpersulfate");
	public static Block BlockFluidSodiumpersulfate;
	
	public static Fluid fluidTritium = new Fluid("fluidtritium");
	public static Block BlockFluidTritium;
	
	public static Fluid fluidWolframium = new Fluid("fluidwolframium");
	public static Block BlockFluidWolframium;
	
	public static void init()
	{
		FluidRegistry.registerFluid(fluidberylium);
		BlockFluidBerylium = new BlockFluidBerylium(fluidberylium, Material.water);
		GameRegistry.registerBlock(BlockFluidBerylium, ModInfo.MOD_ID + "_" + BlockFluidBerylium.getUnlocalizedName().substring(5));
		fluidberylium.setUnlocalizedName(BlockFluidBerylium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidcalcium);
		BlockFluidCalcium = new BlockFluidCalcium(fluidcalcium, Material.water);
		GameRegistry.registerBlock(BlockFluidCalcium, ModInfo.MOD_ID + "_" + BlockFluidCalcium.getUnlocalizedName().substring(5));
		fluidcalcium.setUnlocalizedName(BlockFluidCalcium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidcalciumcarbonate);
		BlockFluidCalciumCarbonate = new BlockFluidCalciumCarbonate(fluidcalciumcarbonate, Material.water);
		GameRegistry.registerBlock(BlockFluidCalciumCarbonate, ModInfo.MOD_ID + "_" + BlockFluidCalciumCarbonate.getUnlocalizedName().substring(5));
		fluidcalcium.setUnlocalizedName(BlockFluidCalciumCarbonate.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidChlorite);
		BlockFluidChlorite = new BlockFluidChlorite(fluidChlorite, Material.water);
		GameRegistry.registerBlock(BlockFluidChlorite, ModInfo.MOD_ID + "_" + BlockFluidChlorite.getUnlocalizedName().substring(5));
		fluidChlorite.setUnlocalizedName(BlockFluidChlorite.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidDeuterium);
		BlockFluidDeuterium = new BlockFluidDeuterium(fluidDeuterium, Material.water);
		GameRegistry.registerBlock(BlockFluidDeuterium, ModInfo.MOD_ID + "_" + BlockFluidDeuterium.getUnlocalizedName().substring(5));
		fluidDeuterium.setUnlocalizedName(BlockFluidDeuterium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidGlyceryl);
		BlockFluidGlyceryl = new BlockFluidGlyceryl(fluidGlyceryl, Material.water);
		GameRegistry.registerBlock(BlockFluidGlyceryl, ModInfo.MOD_ID + "_" + BlockFluidGlyceryl.getUnlocalizedName().substring(5));
		fluidGlyceryl.setUnlocalizedName(BlockFluidGlyceryl.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidHelium);
		BlockFluidHelium = new BlockFluidHelium(fluidHelium, Material.water);
		GameRegistry.registerBlock(BlockFluidHelium, ModInfo.MOD_ID + "_" + BlockFluidHelium.getUnlocalizedName().substring(5));
		fluidHelium.setUnlocalizedName(BlockFluidHelium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidHelium3);
		BlockFluidHelium3 = new BlockFluidHelium3(fluidHelium3, Material.water);
		GameRegistry.registerBlock(BlockFluidHelium3, ModInfo.MOD_ID + "_" + BlockFluidHelium3.getUnlocalizedName().substring(5));
		fluidHelium3.setUnlocalizedName(BlockFluidHelium3.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidHeliumplasma);
		BlockFluidHeliumplasma = new BlockFluidHeliumplasma(fluidHeliumplasma, Material.water);
		GameRegistry.registerBlock(BlockFluidHeliumplasma, ModInfo.MOD_ID + "_" + BlockFluidHeliumplasma.getUnlocalizedName().substring(5));
		fluidHeliumplasma.setUnlocalizedName(fluidHeliumplasma.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidHydrogen);
		BlockFluidHydrogen = new BlockFluidHydrogen(fluidHydrogen, Material.water);
		GameRegistry.registerBlock(BlockFluidHydrogen, ModInfo.MOD_ID + "_" + BlockFluidHydrogen.getUnlocalizedName().substring(5));
		fluidHydrogen.setUnlocalizedName(fluidHydrogen.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidLithium);
		BlockFluidLithium = new BlockFluidLithium(fluidLithium, Material.water);
		GameRegistry.registerBlock(BlockFluidLithium, ModInfo.MOD_ID + "_" + BlockFluidLithium.getUnlocalizedName().substring(5));
		fluidLithium.setUnlocalizedName(fluidLithium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidMercury);
		BlockFluidMercury = new BlockFluidMercury(fluidMercury, Material.water);
		GameRegistry.registerBlock(BlockFluidMercury, ModInfo.MOD_ID + "_" + BlockFluidMercury.getUnlocalizedName().substring(5));
		fluidMercury.setUnlocalizedName(fluidMercury.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidMethane);
		BlockFluidMethane = new BlockFluidMethane(fluidMethane, Material.water);
		GameRegistry.registerBlock(BlockFluidMethane, ModInfo.MOD_ID + "_" + BlockFluidMethane.getUnlocalizedName().substring(5));
		fluidMethane.setUnlocalizedName(fluidMethane.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidNitrocoalfuel);
		BlockFluidNitrocoalfuel = new BlockFluidNitrocoalfuel(fluidNitrocoalfuel, Material.water);
		GameRegistry.registerBlock(BlockFluidNitrocoalfuel, ModInfo.MOD_ID + "_" + BlockFluidNitrocoalfuel.getUnlocalizedName().substring(5));
		fluidNitrocoalfuel.setUnlocalizedName(fluidNitrocoalfuel.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidNitrofuel);
		BlockFluidNitrofuel = new BlockFluidNitrofuel(fluidNitrofuel, Material.water);
		GameRegistry.registerBlock(BlockFluidNitrofuel, ModInfo.MOD_ID + "_" + BlockFluidNitrofuel.getUnlocalizedName().substring(5));
		fluidNitrofuel.setUnlocalizedName(fluidNitrofuel.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidNitrogen);
		BlockFluidNitrogen = new BlockFluidNitrogen(fluidNitrogen, Material.water);
		GameRegistry.registerBlock(BlockFluidNitrogen, ModInfo.MOD_ID + "_" + BlockFluidNitrogen.getUnlocalizedName().substring(5));
		fluidNitrogen.setUnlocalizedName(fluidNitrogen.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidNitrogendioxide);
		BlockFluidNitrogendioxide = new BlockFluidNitrogendioxide(fluidNitrogendioxide, Material.water);
		GameRegistry.registerBlock(BlockFluidNitrogendioxide, ModInfo.MOD_ID + "_" + BlockFluidNitrogendioxide.getUnlocalizedName().substring(5));
		fluidNitrogendioxide.setUnlocalizedName(fluidNitrogendioxide.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidPotassium);
		BlockFluidPotassium = new BlockFluidPotassium(fluidPotassium, Material.water);
		GameRegistry.registerBlock(BlockFluidPotassium, ModInfo.MOD_ID + "_" + BlockFluidPotassium.getUnlocalizedName().substring(5));
		fluidPotassium.setUnlocalizedName(fluidPotassium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidSilicon);
		BlockFluidSilicon = new BlockFluidSilicon(fluidSilicon, Material.water);
		GameRegistry.registerBlock(BlockFluidSilicon, ModInfo.MOD_ID + "_" + BlockFluidSilicon.getUnlocalizedName().substring(5));
		fluidSilicon.setUnlocalizedName(fluidSilicon.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidSodium);
		BlockFluidSodium = new BlockFluidSodium(fluidSodium, Material.water);
		GameRegistry.registerBlock(BlockFluidSodium, ModInfo.MOD_ID + "_" + BlockFluidSodium.getUnlocalizedName().substring(5));
		fluidSodium.setUnlocalizedName(fluidSodium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidSodiumpersulfate);
		BlockFluidSodiumpersulfate = new BlockFluidSodiumpersulfate(fluidSodiumpersulfate, Material.water);
		GameRegistry.registerBlock(BlockFluidSodiumpersulfate, ModInfo.MOD_ID + "_" + BlockFluidSodiumpersulfate.getUnlocalizedName().substring(5));
		fluidSodiumpersulfate.setUnlocalizedName(fluidSodiumpersulfate.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidTritium);
		BlockFluidTritium = new BlockFluidTritium(fluidTritium, Material.water);
		GameRegistry.registerBlock(BlockFluidTritium, ModInfo.MOD_ID + "_" + BlockFluidTritium.getUnlocalizedName().substring(5));
		fluidTritium.setUnlocalizedName(fluidTritium.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidWolframium);
		BlockFluidWolframium = new BlockFluidWolframium(fluidWolframium, Material.water);
		GameRegistry.registerBlock(BlockFluidWolframium, ModInfo.MOD_ID + "_" + BlockFluidWolframium.getUnlocalizedName().substring(5));
		fluidWolframium.setUnlocalizedName(fluidWolframium.getUnlocalizedName());


	}
}
