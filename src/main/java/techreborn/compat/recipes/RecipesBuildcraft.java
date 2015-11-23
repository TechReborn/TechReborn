package techreborn.compat.recipes;

import buildcraft.api.fuels.IFuel;
import buildcraft.core.Version;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import ic2.api.item.IC2Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.fuel.FluidPowerManager;
import reborncore.common.util.CraftingHelper;
import reborncore.common.util.RecipeRemover;
import techreborn.Core;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;

public class RecipesBuildcraft implements ICompatModule {

    public static Block quarryBlock;

    public static void removeRecipes() {
        RecipeRemover.removeAnyRecipe(new ItemStack(
                quarryBlock));
    }

    public static void addRecipies() {
        Item drill = IC2Items.getItem("diamondDrill").getItem();
        ItemStack drillStack = new ItemStack(drill, 1, OreDictionary.WILDCARD_VALUE);
        //Quarry
        CraftingHelper.addShapedOreRecipe(new ItemStack(quarryBlock), new Object[]
                        {
                                "IAI", "GIG", "DED",
                                'I', "gearIron",
                                'G', "gearGold",
                                'D', "gearDiamond",
                                'A', IC2Items.getItem("advancedCircuit"),
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
        try {
            String itemClass = "buildcraft.BuildCraftBuilders";
            if (!Version.getVersion().startsWith("7")) {//Buildcraft 6
                if (Loader.isModLoaded("BuildCraft|Factory")) {
                    itemClass = "buildcraft.BuildCraftFactory";
                }
            } else if (!Version.getVersion().startsWith("7") && !Loader.isModLoaded("BuildCraft|Builders")) { //Buildcraft 7
                Core.logHelper.info("Buildcraft not found");
                return;
            }
            Object obj = Class.forName(itemClass).getField("quarryBlock").get(null);
            if (obj instanceof Block) {
                quarryBlock = (Block) obj;
                Core.logHelper.info("Found Quarry Block from buildcraft at " + itemClass + ":quarryBlock");
            } else {
                Core.logHelper.fatal("Could not retrieve quarry block from Buildcraft! This is a fatal error!");
                return;
            }
        } catch (Exception ex) {
            Core.logHelper.fatal("Could not retrieve quarry block from Buildcraft! This is a fatal error!");
            ex.printStackTrace();
            return;
        }
        removeRecipes();
        addRecipies();
        for (IFuel fuel : buildcraft.energy.fuels.FuelManager.INSTANCE.getFuels()) {
            FluidPowerManager.fluidPowerValues.put(fuel.getFluid(), (double) fuel.getPowerPerCycle() / ConfigTechReborn.euPerRF);
        }
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}
