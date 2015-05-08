package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import techreborn.blocks.fluid.BlockFluidBerylium;
import techreborn.lib.ModInfo;
import techreborn.util.BucketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluids {
	
	public static Fluid fluidberylium = new Fluid("fluidberylium");
	public static Block BlockFluidBerylium;
	
	public static void init()
	{
		FluidRegistry.registerFluid(fluidberylium);
		BlockFluidBerylium = new BlockFluidBerylium(fluidberylium, Material.water);
		GameRegistry.registerBlock(BlockFluidBerylium, ModInfo.MOD_ID + "_" + BlockFluidBerylium.getUnlocalizedName().substring(5));
		fluidberylium.setUnlocalizedName(BlockFluidBerylium.getUnlocalizedName());
		

		BucketHandler.INSTANCE.buckets.put(ModFluids.BlockFluidBerylium, ModItems.cells);
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
	}
}
