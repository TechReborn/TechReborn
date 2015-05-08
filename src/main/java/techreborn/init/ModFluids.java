package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import techreborn.blocks.fluid.BlockCalciumCarbonate;
import techreborn.blocks.fluid.BlockFluidBerylium;
import techreborn.blocks.fluid.BlockFluidChlorite;
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
		BlockFluidCalciumCarbonate = new BlockCalciumCarbonate(fluidcalciumcarbonate, Material.water);
		GameRegistry.registerBlock(BlockFluidCalciumCarbonate, ModInfo.MOD_ID + "_" + BlockFluidCalciumCarbonate.getUnlocalizedName().substring(5));
		fluidcalcium.setUnlocalizedName(BlockFluidCalciumCarbonate.getUnlocalizedName());
		
		FluidRegistry.registerFluid(fluidChlorite);
		BlockFluidChlorite = new BlockFluidChlorite(fluidChlorite, Material.water);
		GameRegistry.registerBlock(BlockFluidChlorite, ModInfo.MOD_ID + "_" + BlockFluidChlorite.getUnlocalizedName().substring(5));
		fluidChlorite.setUnlocalizedName(BlockFluidChlorite.getUnlocalizedName());
		
		//TODO
		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidBerylium, ModItems.cells);
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	}
}
