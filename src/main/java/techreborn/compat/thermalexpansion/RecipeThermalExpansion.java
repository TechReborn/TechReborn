package techreborn.compat.thermalexpansion;

import cofh.api.util.ThermalExpansionHelper;
import cofh.thermalfoundation.init.TFFluids;
import cofh.thermalfoundation.item.ItemMaterial;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.compat.ICompatModule;
import techreborn.init.ModItems;
import techreborn.init.recipes.RecipeMethods;
import techreborn.items.ItemDusts;
import techreborn.items.ItemPlates;

/**
 * Created by modmuss50 on 06/07/2017.
 */
public class RecipeThermalExpansion implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		ThermalExpansionHelper.addPulverizerRecipe(2000, new ItemStack(Items.ENDER_PEARL), RecipeMethods.getMaterial("ender_pearl", 1, RecipeMethods.Type.DUST));
		ThermalExpansionHelper.addPulverizerRecipe(3000, new ItemStack(Items.ENDER_EYE), RecipeMethods.getMaterial("ender_eye", 2, RecipeMethods.Type.DUST));
		ThermalExpansionHelper.addPulverizerRecipe(3000, new ItemStack(Items.FLINT), RecipeMethods.getMaterial("flint", 2, RecipeMethods.Type.SMALL_DUST), RecipeMethods.getMaterial("flint", 2, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(2500, new ItemStack(ModItems.CELL), RecipeMethods.getMaterial("tin", 4, RecipeMethods.Type.SMALL_DUST));
		ThermalExpansionHelper.addPulverizerRecipe(2500, new ItemStack(Blocks.END_STONE), RecipeMethods.getMaterial("endstone", 1, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("endstone", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("galena", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("galena", 1, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("sulfur", 1, RecipeMethods.Type.DUST), 50);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("ruby", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("ruby", 2, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("red_garnet", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("sapphire", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("sapphire", 2, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("sphalerite", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("bauxite", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("bauxite", 2, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("aluminum", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("pyrite", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("pyrite", 5, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("iron", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("cinnabar", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("cinnabar", 3, RecipeMethods.Type.DUST), new ItemStack(Items.REDSTONE), 10);
		ThermalExpansionHelper.addPulverizerRecipe(4000, RecipeMethods.getMaterial("sphalerite", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("sphalerite", 4, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("zinc", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(5000, RecipeMethods.getMaterial("tungsten", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("tungsten", 2, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("manganese", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("peridot", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("peridot", 2, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("emerald", 1, RecipeMethods.Type.DUST), 10);
		ThermalExpansionHelper.addPulverizerRecipe(3000, RecipeMethods.getMaterial("sodalite", 1, RecipeMethods.Type.ORE), RecipeMethods.getMaterial("sodalite", 12, RecipeMethods.Type.DUST), RecipeMethods.getMaterial("aluminum", 1, RecipeMethods.Type.DUST), 10);

		for(String plate : ItemPlates.types){
			if(!plate.equals(ModItems.META_PLACEHOLDER) && ItemDusts.hasDust(plate)){
				ThermalExpansionHelper.addPulverizerRecipe(5000, RecipeMethods.getMaterial(plate, 1, RecipeMethods.Type.PLATE), RecipeMethods.getMaterial(plate, 9, RecipeMethods.Type.DUST));
			}
		}

		ThermalExpansionHelper.addSmelterRecipe(4000, new ItemStack(Items.IRON_INGOT, 2), new ItemStack(Blocks.SAND), RecipeMethods.getMaterial("refined_iron", 2, RecipeMethods.Type.INGOT), ItemMaterial.crystalSlag.copy(), 25);

		GeneratorRecipeHelper.registerFluidRecipe(EFluidGenerator.THERMAL, TFFluids.fluidPyrotheum, 80);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
