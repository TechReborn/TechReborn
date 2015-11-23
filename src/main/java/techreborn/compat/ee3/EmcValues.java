package techreborn.compat.ee3;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.api.exchange.RecipeRegistryProxy;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.OreStack;
import com.pahimar.ee3.exchange.WrappedStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import techreborn.api.recipe.IBaseRecipeType;
import techreborn.api.recipe.RecipeHandler;
import techreborn.compat.ICompatModule;
import techreborn.items.ItemParts;
import techreborn.items.ItemPlates;

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
        if (!Loader.isModLoaded("EE3Compatibility")) {
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

        addStack(ItemPlates.getPlateByName("steel"), 512);
        addStack(ItemParts.getPartByName("lazuriteChunk"), 7776);
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
