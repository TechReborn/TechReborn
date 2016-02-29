package techreborn.compat.recipes;

import buildcraft.BuildCraftBuilders;
import buildcraft.api.fuels.IFuel;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.fuel.FluidPowerManager;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.CraftingHelper;
import reborncore.common.util.RecipeRemover;
import techreborn.Core;
import techreborn.api.TechRebornAPI;
import techreborn.compat.ICompatModule;

public class RecipesBuildcraft implements ICompatModule {

    public static Block quarryBlock;

    public static void removeRecipes() {
        RecipeRemover.removeAnyRecipe(new ItemStack(
                quarryBlock));
    }

    public static void addRecipies() {
        Item drill = TechRebornAPI.recipeCompact.getItem("diamondDrill").getItem();
        ItemStack drillStack = new ItemStack(drill, 1, OreDictionary.WILDCARD_VALUE);
        //Quarry
        CraftingHelper.addShapedOreRecipe(new ItemStack(quarryBlock), new Object[]
                        {
                                "IAI", "GIG", "DED",
                                'I', "gearIron",
                                'G', "gearGold",
                                'D', "gearDiamond",
                                'A', TechRebornAPI.recipeCompact.getItem("advancedCircuit"),
                                'E', drillStack
                        }
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        Core.logHelper.info("Trying to change the quarry recipe");
        quarryBlock = BuildCraftBuilders.quarryBlock;
        removeRecipes();
        addRecipies();
        for (IFuel fuel : buildcraft.energy.fuels.FuelManager.INSTANCE.getFuels()) {
            FluidPowerManager.fluidPowerValues.put(fuel.getFluid(), (double) fuel.getPowerPerCycle() / PowerSystem.euPerRF);
        }
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}
