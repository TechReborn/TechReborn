package techreborn.compat.buildcraft;

import buildcraft.builders.BCBuildersBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import reborncore.common.util.CraftingHelper;
import reborncore.common.util.RecipeRemover;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

/**
 * Created by Mark on 02/06/2017.
 */
public class BuildcraftBuildersCompat implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		if(ConfigTechReborn.ExpensiveQuarry){
			RecipeRemover.removeAnyRecipe(new ItemStack(BCBuildersBlocks.quarry));

			CraftingHelper.addShapedOreRecipe(new ItemStack(BCBuildersBlocks.quarry),
				"IAI", "GIG", "DED",
				'I', "gearIron",
				'G', "gearGold",
				'D', "gearDiamond",
				'A', "circuitAdvanced",
				'E', new ItemStack(ModItems.DIAMOND_DRILL));
		}
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
