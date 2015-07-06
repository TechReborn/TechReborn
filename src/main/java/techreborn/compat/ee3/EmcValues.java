package techreborn.compat.ee3;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.exchange.RecipeRegistryProxy;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.OreStack;
import com.pahimar.ee3.exchange.WrappedStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputFluidContainer;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import ic2.core.AdvRecipe;
import ic2.core.AdvShapelessRecipe;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.compat.ICompatModule;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EmcValues implements ICompatModule {

	public static void addIC2Handlers() {
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.macerator.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.compressor.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.extractor.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.metalformerCutting.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.metalformerExtruding.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.metalformerRolling.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.oreWashing.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.centrifuge.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.blockcutter.getRecipes().entrySet())
			sendRecipeEntry(entry);
		for (Map.Entry<IRecipeInput, RecipeOutput> entry : Recipes.blastfurance.getRecipes().entrySet())
			sendRecipeEntry(entry);


		for (Object recipeObject : CraftingManager.getInstance().getRecipeList()) {
			if (recipeObject instanceof AdvRecipe || recipeObject instanceof AdvShapelessRecipe) {
				IRecipe recipe = (IRecipe) recipeObject;
				if (recipe.getRecipeOutput() != null) {
					List<Object> recipeInputs = getRecipeInputs(recipe);
					if (recipeInputs != null && !recipeInputs.isEmpty()) {
						RecipeRegistryProxy.addRecipe(recipe.getRecipeOutput(), recipeInputs);
					}
				}
			}
		}
	}

	private static void sendRecipeEntry(Map.Entry<IRecipeInput, RecipeOutput> entry) {
		List<ItemStack> recipeStackOutputs = entry.getValue().items;
		if (recipeStackOutputs.size() == 1) {
			ItemStack recipeOutput = recipeStackOutputs.get(0);
			if (recipeOutput != null) {
				recipeOutput = recipeOutput.copy();
				recipeOutput.setTagCompound(entry.getValue().metadata);

				for (ItemStack recipeInput : entry.getKey().getInputs()) {
					if (recipeInput != null) {
						recipeInput = recipeInput.copy();
						recipeInput.stackSize = entry.getKey().getAmount();

						RecipeRegistryProxy.addRecipe(recipeOutput, Arrays.asList(recipeInput));
					}
				}
			}
		}
	}

	private static List<Object> getRecipeInputs(IRecipe recipe) {
		List<Object> recipeInputs = new ArrayList<Object>();

		if (recipe instanceof AdvRecipe) {
			for (Object object : ((AdvRecipe) recipe).input) {
				addInputToList(recipeInputs, object);
			}
		} else if (recipe instanceof AdvShapelessRecipe) {
			for (Object object : ((AdvShapelessRecipe) recipe).input) {
				addInputToList(recipeInputs, object);
			}
		}

		return recipeInputs;
	}

	public static void addInputToList(List<Object> recipeInputs, Object object) {
		if (object instanceof ItemStack) {
			ItemStack itemStack = ((ItemStack) object).copy();
			recipeInputs.add(itemStack);
		} else if (object instanceof String) {
			OreStack stack = new OreStack((String) object);
			recipeInputs.add(stack);
		} else if (object instanceof IRecipeInput) {
			if (object instanceof RecipeInputItemStack)
				recipeInputs.add(((RecipeInputItemStack) object).input);
			else if (object instanceof RecipeInputOreDict)
				recipeInputs.add(new OreStack(((RecipeInputOreDict) object).input));
			else if (object instanceof RecipeInputFluidContainer)
				recipeInputs.add(new FluidStack(((RecipeInputFluidContainer) object).fluid, ((RecipeInputFluidContainer) object).amount));
		}
	}

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
		if(!Loader.isModLoaded("EE3Compatibility")){
			MinecraftForge.EVENT_BUS.register(this);
			addOre("ingotCopper", 128);
			addOre("ingotSilver", 1024);
			addOre("ingotTin", 256);
			addOre("ingotLead", 256);
			addOre("dustSteel", 512);
			addOre("ingotRefinedIron", 512);
			addOre("dustCoal", 32);
			addOre("dustDiamond", 8192);
			addOre("dustSulfur", 32);
			addOre("dustLead", 256);
			addOre("ingotBronze", 256);
			addOre("ingotElectrum", 2052);
			addOre("dustLapis", 864);
			addOre("dustSilver", 1024);
			addOre("dustTin", 256);
		}

		if(!Loader.isModLoaded("EE3Compatibility")){
			addStack(IC2Items.getItem("rubber"), 32);
			addStack(IC2Items.getItem("carbonPlate"), 256);
			addStack(Ic2Items.energyCrystal, 32896 / 9);
			addStack(Ic2Items.chargingEnergyCrystal, 32896 / 9);
			addStack(IC2Items.getItem("refinedIronIngot"), 512);
			addStack(Ic2Items.plateadviron, 512);
			addStack(Ic2Items.reBattery, 608);
			addStack(Ic2Items.chargedReBattery, 608);
		}

		addStack(ItemPlates.getPlateByName("steel"), 512);
		addStack(ItemParts.getPartByName("lazuriteChunk"), 7776);
		if(!Loader.isModLoaded("EE3Compatibility")){
			addIC2Handlers();
		}
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandRegen());
		event.registerServerCommand(new CommandReload());
	}

	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event) {
		//This should be a fix for the things not saving
		EnergyValueRegistry.getInstance().setShouldRegenNextRestart(false);
	}

	private void addOre(String name, float value) {
		WrappedStack stack = WrappedStack.wrap(new OreStack(name));
		EnergyValue energyValue = new EnergyValue(value);

		EnergyValueRegistryProxy.addPreAssignedEnergyValue(stack, energyValue);
	}


	private void addStack(ItemStack itemStack, float value) {
		WrappedStack stack = WrappedStack.wrap(itemStack);
		EnergyValue energyValue = new EnergyValue(value);

		EnergyValueRegistryProxy.addPreAssignedEnergyValue(stack, energyValue);
	}


}
