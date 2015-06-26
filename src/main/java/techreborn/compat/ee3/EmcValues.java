package techreborn.compat.ee3;

import com.pahimar.ee3.api.exchange.RecipeRegistryProxy;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.command.TechRebornDevCommand;
import techreborn.compat.ICompatModule;

public class EmcValues implements ICompatModule {

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		for (IBaseRecipeType recipeType : RecipeHandler.recipeList) {
			if (recipeType.getOutputsSize() == 1) {
				RecipeRegistryProxy.addRecipe(recipeType.getOutput(0), recipeType.getInputs());
			}
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandRegen());
		event.registerServerCommand(new CommandReload());
	}


	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event){
		//This should be a fix for the things not saving
		EnergyValueRegistry.getInstance().setShouldRegenNextRestart(false);
	}
}
