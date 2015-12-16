package techreborn.compat.recipes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.CraftingHelper;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.blocks.BlockOre;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.*;

public class RecipesStandalone implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        addShappedIc2Recipes();
        addTRRecipes();

        FusionReactorRecipeHelper.registerRecipe(new FusionReactorRecipe(ItemCells.getCellByName("wolframium"), ItemCells.getCellByName("lithium"), BlockOre.getOreByName("Iridium"), 90000000, -2048, 1024));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void addTRRecipes() {
        //General
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.manuel), "plateIron", Items.book);

        CraftingHelper.addShapedOreRecipe(
                ItemParts.getPartByName("machineParts", 16),
                "CSC", "SCS", "CSC",
                'S', "ingotSteel",
                'C', ItemParts.getPartByName("basicCircuitBoard"));

        CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("energyFlowCircuit", 4),
                "ATA", "LIL", "ATA",
                'T', "plateTungsten",
                'I', "plateIridium",
                'A', ItemParts.getPartByName("advancedCircuitBoard"),
                'L', new ItemStack(Items.emerald));

        CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("superconductor", 4),
                "CCC", "TIT", "EEE",
                'E', ItemParts.getPartByName("energyFlowCircuit"),
                'C', ItemParts.getPartByName("heliumCoolantSimple"),
                'T', "ingotTungsten",
                'I', "plateIridium");

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lapotronicOrb),
                "LLL", "LPL", "LLL",
                'L', new ItemStack(Items.emerald),
                'P', "plateIridium");

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.industrialSawmill),
                "PAP",
                "SSS",
                "ACA",
                'P', new ItemStack(Blocks.hopper),
                'A', ItemParts.getPartByName("advancedCircuitBoard"),
                'S', ItemParts.getPartByName("diamondSawBlade"),
                'C', new ItemStack(ModBlocks.MachineCasing, 1, 2));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ComputerCube),
                "DME", "MAM", "EMD",
                'E', ItemParts.getPartByName("energyFlowCircuit"),
                'D', ItemParts.getPartByName("dataOrb"),
                'M', ItemParts.getPartByName("computerMonitor"),
                'A', new ItemStack(ModBlocks.MachineCasing, 1, 2));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MatterFabricator),
                "ETE", "AOA", "ETE",
                'E', ItemParts.getPartByName("energyFlowCircuit"),
                'T', new ItemStack(Items.enchanted_book),
                'A', ModBlocks.HighAdvancedMachineBlock,
                'O', ModItems.lapotronicOrb);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                "III", "IHI", "CGC",
                'I', "plateInvar",
                'H', new ItemStack(Blocks.glass),
                'C', "circuitBasic",
                'G', new ItemStack(ModBlocks.heatGenerator));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                "AAA", "AHA", "CGC",
                'A', "plateAluminum",
                'H', new ItemStack(Blocks.glass),
                'C', "circuitBasic",
                'G', new ItemStack(ModBlocks.heatGenerator));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.heatGenerator),
                "III", "IHI", "CGC",
                'I', "plateIron",
                'H', new ItemStack(Blocks.iron_bars),
                'C', "circuitBasic",
                'G', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine),
                "IAI", "WGW", "IAI",
                'I', "plateInvar",
                'A', ItemParts.getPartByName("advancedCircuitBoard"),
                'W', new ItemStack(Blocks.iron_bars),
                'G', new ItemStack(Blocks.glass));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine),
                "IAI", "WGW", "IAI",
                'I', "plateAluminum",
                'A', ItemParts.getPartByName("advancedCircuitBoard"),
                'W', new ItemStack(Blocks.iron_bars),
                'G', new ItemStack(Blocks.glass));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                "III", "IHI", "CGC",
                'I', "plateIron",
                'H', new ItemStack(Blocks.glass),
                'C', "circuitBasic",
                'G', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                "AAA", "AHA", "CGC",
                'A', "plateAluminum",
                'H', new ItemStack(Blocks.glass),
                'C', "circuitBasic",
                'G', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                "III", "I I", "CGC",
                'I', "plateIron",
                'C', "circuitBasic",
                'G', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                "AAA", "A A", "CGC",
                'A', "plateAluminum",
                'C', "circuitBasic",
                'G', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MagicalAbsorber),
                "CSC", "IBI", "CAC",
                'C', "circuitMaster",
                'S', "craftingSuperconductor",
                'B', Blocks.beacon,
                'A', ModBlocks.Magicenergeyconverter,
                'I', "plateIridium");

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Magicenergeyconverter),
                "CTC", "PBP", "CLC",
                'C', "circuitAdvanced",
                'P', "platePlatinum",
                'B', Blocks.beacon,
                'L', new ItemStack(Items.emerald),
                'T', new ItemStack(Items.ender_eye));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Dragoneggenergysiphoner),
                "CTC", "ISI", "CBC",
                'I', "plateIridium",
                'C', "circuitMaster",
                'B', "batteryUltimate",
                'S', ModBlocks.Supercondensator,
                'T', new ItemStack(Items.ender_eye));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.centrifuge),
                "SCS", "BEB", "SCS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'B', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'E', new ItemStack(ModBlocks.AlloyFurnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IndustrialElectrolyzer),
                "SXS", "CEC", "SMS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'X', new ItemStack(ModBlocks.AlloyFurnace),
                'E', new ItemStack(Blocks.enchanting_table),
                'M', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BlastFurnace),
                "CHC", "HBH", "FHF",
                'H', new ItemStack(ModItems.parts, 1, 17),
                'C', "circuitAdvanced",
                'B', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'F', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Grinder),
                "ECP", "GGG", "CBC",
                'E', ModBlocks.IndustrialElectrolyzer,
                'P', new ItemStack(Blocks.iron_bars),
                'C', "circuitAdvanced",
                'B', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'G', "craftingGrinder");

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ImplosionCompressor),
                "ABA", "CPC", "ABA",
                'A', new ItemStack(Items.gold_ingot),
                'C', "circuitAdvanced",
                'B', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'P', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.VacuumFreezer),
                "SPS", "CGC", "SPS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'G', new ItemStack(Blocks.glass),
                'P', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Distillationtower),
                "CMC", "PBP", "EME",
                'E', ModBlocks.IndustrialElectrolyzer,
                'M', "circuitMaster",
                'B', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'C', ModBlocks.centrifuge,
                'P', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloyFurnace),
                "III", "F F", "III",
                'I', "plateIron",
                'F', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                "IMI", "CPC", "IEI",
                'I', "plateInvar",
                'C', "circuitAdvanced",
                'M', new ItemStack(ModBlocks.AlloyFurnace),
                'P', new ItemStack(ModBlocks.AlloyFurnace),
                'E', new ItemStack(ModBlocks.AlloyFurnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                "AMA", "CPC", "AEA",
                'A', "plateAluminum",
                'C', "circuitAdvanced",
                'M', new ItemStack(ModBlocks.AlloyFurnace),
                'P', new ItemStack(ModBlocks.AlloyFurnace),
                'E', new ItemStack(ModBlocks.AlloyFurnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.RollingMachine),
                "PCP", "MBM", "PCP",
                'P', "craftingPiston",
                'C', "circuitAdvanced",
                'M', new ItemStack(ModBlocks.AlloyFurnace),
                'B', new ItemStack(ModBlocks.AlloyFurnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                "ITI", "IBI", "ICI",
                'I', "plateIron",
                'C', "circuitAdvanced",
                'T', "crafterWood",
                'B', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                "ATA", "ABA", "ACA",
                'A', "plateAluminum",
                'C', "circuitAdvanced",
                'T', "crafterWood",
                'B', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChunkLoader),
                "SCS", "CMC", "SCS",
                'S', "plateSteel",
                'C', "circuitMaster",
                'M', new ItemStack(ModItems.parts, 1, 39));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Lesu),
                " L ", "CBC", " M ",
                'L', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'C', "circuitAdvanced",
                'M', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'B', ModBlocks.LesuStorage);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.HighAdvancedMachineBlock),
                "CTC", "TBT", "CTC",
                'C', "plateChrome",
                'T', "plateTitanium",
                'B', new ItemStack(ModBlocks.MachineCasing, 1, 2));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 0),
                "III", "CBC", "III",
                'I', "plateIron",
                'C', "circuitBasic",
                'B', new ItemStack(Blocks.furnace));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 1),
                "SSS", "CBC", "SSS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'B', new ItemStack(ModBlocks.MachineCasing, 1, 2));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 2),
                "HHH", "CBC", "HHH",
                'H', "plateChrome",
                'C', "circuitElite",
                'B', ModBlocks.HighAdvancedMachineBlock);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.quantumChest),
                "DCD", "ATA", "DQD",
                'D', ItemParts.getPartByName("dataOrb"),
                'C', ItemParts.getPartByName("computerMonitor"),
                'A', ModBlocks.HighAdvancedMachineBlock,
                'Q', ModBlocks.digitalChest,
                'T', new ItemStack(Items.ender_eye));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.PlasmaGenerator),
                "PPP", "PTP", "CGC",
                'P', ItemPlates.getPlateByName("tungstensteel"),
                'T', new ItemStack(ModBlocks.MachineCasing, 1, 2),
                'G', new ItemStack(Blocks.furnace),
                'C', ItemParts.getPartByName("energyFlowCircuit")
        );


        //UU
        if (ConfigTechReborn.UUrecipesIridiamOre)
            CraftingHelper.addShapedOreRecipe(ItemIngots.getIngotByName("iridium"),
                    "UUU",
                    " U ",
                    "UUU",
                    'U', ModItems.uuMatter);


        //Blast Furnace
        RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemCells.getCellByName("silicon", 2), null, ItemPlates.getPlateByName("silicon"), ItemCells.getCellByName("empty"), 1000, 120, 1500));

        //CentrifugeRecipes

    }

    static void addShappedIc2Recipes() {
        CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("iridiumAlloyIngot"),
                "IAI", "ADA", "IAI",
                'I', ItemIngots.getIngotByName("iridium"),
                'D', ItemDusts.getDustByName("diamond"),
                'A', new ItemStack(Items.diamond));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lithiumBatpack, 1, OreDictionary.WILDCARD_VALUE),
                "BCB", "BPB", "B B",
                'B', new ItemStack(ModItems.lithiumBattery),
                'P', "plateAluminum",
                'C', ItemParts.getPartByName("advancedCircuitBoard"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lithiumBattery, 1, OreDictionary.WILDCARD_VALUE),
                " C ", "PFP", "PFP",
                'F', ItemCells.getCellByName("lithium"),
                'P', "plateAluminum",
                'C', new ItemStack(Items.golden_chestplate));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lapotronpack, 1, OreDictionary.WILDCARD_VALUE),
                "FOF", "SPS", "FIF",
                'F', ItemParts.getPartByName("energyFlowCircuit"),
                'O', new ItemStack(ModItems.lapotronicOrb),
                'S', ItemParts.getPartByName("superConductor"),
                'I', "ingotIridium",
                'P', new ItemStack(ModItems.lapotronpack));
    }

}
