package techreborn.compat.recipes;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.info.IC2Classic;
import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.CraftingHelper;
import reborncore.common.util.OreUtil;
import reborncore.common.util.RecipeRemover;
import techreborn.Core;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.*;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;
import techreborn.init.ModItems;
import techreborn.items.*;

public class RecipesIC2 implements ICompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        removeIc2Recipes();
        addShappedIc2Recipes();
        addTRMaceratorRecipes();
        addTROreWashingRecipes();
        addTRThermalCentrifugeRecipes();
        addMetalFormerRecipes();
        addTRRecipes();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        //Has to be done later, not sure why
        RecipeRemover.removeAnyRecipe(IC2Items.getItem("iridiumPlate"));
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void addTRRecipes() {
        //General
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.manuel), IC2Items.getItem("plateiron"), Items.book);

        CraftingHelper.addShapedOreRecipe(
                ItemParts.getPartByName("machineParts", 16),
                "CSC", "SCS", "CSC",
                'S', "ingotSteel",
                'C', IC2Items.getItem("electronicCircuit"));

        CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("energyFlowCircuit", 4),
                "ATA", "LIL", "ATA",
                'T', "plateTungsten",
                'I', "plateIridium",
                'A', IC2Items.getItem("advancedCircuit"),
                'L', IC2Items.getItem("lapotronCrystal"));

        CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("superconductor", 4),
                "CCC", "TIT", "EEE",
                'E', ItemParts.getPartByName("energyFlowCircuit"),
                'C', ItemParts.getPartByName("heliumCoolantSimple"),
                'T', "ingotTungsten",
                'I', IC2Items.getItem("iridiumPlate"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lapotronicOrb),
                "LLL", "LPL", "LLL",
                'L', IC2Items.getItem("lapotronCrystal"),
                'P', IC2Items.getItem("iridiumPlate"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.lapotronicOrb),
                "LLL", "LPL", "LLL",
                'L', IC2Items.getItem("lapotronCrystal"),
                'P', IC2Items.getItem("iridiumPlate"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.industrialSawmill),
                "PAP",
                "SSS",
                "ACA",
                'P', IC2Items.getItem("pump"),
                'A', IC2Items.getItem("advancedCircuit"),
                'S', ItemParts.getPartByName("diamondSawBlade"),
                'C', IC2Items.getItem("advancedMachine"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ComputerCube),
                "DME", "MAM", "EMD",
                'E', ItemParts.getPartByName("energyFlowCircuit"),
                'D', ItemParts.getPartByName("dataOrb"),
                'M', ItemParts.getPartByName("computerMonitor"),
                'A', IC2Items.getItem("advancedMachine"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MatterFabricator),
                "ETE", "AOA", "ETE",
                'E', ItemParts.getPartByName("energyFlowCircuit"),
                'T', IC2Items.getItem("teleporter"),
                'A', ModBlocks.HighAdvancedMachineBlock,
                'O', ModItems.lapotronicOrb);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                "III", "IHI", "CGC",
                'I', "plateInvar",
                'H', IC2Items.getItem("reinforcedGlass"),
                'C', "circuitBasic",
                'G', new ItemStack(ModBlocks.heatGenerator));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                "AAA", "AHA", "CGC",
                'A', "plateAluminum",
                'H', IC2Items.getItem("reinforcedGlass"),
                'C', "circuitBasic",
                'G', new ItemStack(ModBlocks.heatGenerator));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.heatGenerator),
                "III", "IHI", "CGC",
                'I', "plateIron",
                'H', new ItemStack(Blocks.iron_bars),
                'C', "circuitBasic",
                'G', IC2Items.getItem("geothermalGenerator"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine),
                "IAI", "WGW", "IAI",
                'I', "plateInvar",
                'A', IC2Items.getItem("advancedCircuit"),
                'W', IC2Items.getItem("windMill"),
                'G', IC2Items.getItem("reinforcedGlass"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Gasturbine),
                "IAI", "WGW", "IAI",
                'I', "plateAluminum",
                'A', IC2Items.getItem("advancedCircuit"),
                'W', IC2Items.getItem("windMill"),
                'G', IC2Items.getItem("reinforcedGlass"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                "III", "IHI", "CGC",
                'I', "plateIron",
                'H', IC2Items.getItem("reinforcedGlass"),
                'C', "circuitBasic",
                'G', IC2Items.getItem("generator"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Semifluidgenerator),
                "AAA", "AHA", "CGC",
                'A', "plateAluminum",
                'H', IC2Items.getItem("reinforcedGlass"),
                'C', "circuitBasic",
                'G', IC2Items.getItem("generator"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                "III", "I I", "CGC",
                'I', "plateIron",
                'C', "circuitBasic",
                'G', IC2Items.getItem("generator"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.DieselGenerator),
                "AAA", "A A", "CGC",
                'A', "plateAluminum",
                'C', "circuitBasic",
                'G', IC2Items.getItem("generator"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MagicalAbsorber),
                "CSC", "IBI", "CAC",
                'C', "circuitMaster",
                'S', "craftingSuperconductor",
                'B', Blocks.beacon,
                'A', ModBlocks.Magicenergeyconverter,
                'I', IC2Items.getItem("iridiumPlate"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Magicenergeyconverter),
                "CTC", "PBP", "CLC",
                'C', "circuitAdvanced",
                'P', "platePlatinum",
                'B', Blocks.beacon,
                'L', IC2Items.getItem("lapotronCrystal"),
                'T', IC2Items.getItem("teleporter"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Dragoneggenergysiphoner),
                "CTC", "ISI", "CBC",
                'I', IC2Items.getItem("iridiumPlate"),
                'C', "circuitMaster",
                'B', "batteryUltimate",
                'S', ModBlocks.Supercondensator,
                'T', IC2Items.getItem("teleporter"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.centrifuge),
                "SCS", "BEB", "SCS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'B', IC2Items.getItem("advancedMachine"),
                'E', IC2Items.getItem("extractor"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.IndustrialElectrolyzer),
                "SXS", "CEC", "SMS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'X', IC2Items.getItem("extractor"),
                'E', IC2Items.getItem("electrolyzer"),
                'M', IC2Items.getItem("magnetizer"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.BlastFurnace),
                "CHC", "HBH", "FHF",
                'H', new ItemStack(ModItems.parts, 1, 17),
                'C', "circuitAdvanced",
                'B', IC2Items.getItem("advancedMachine"),
                'F', IC2Items.getItem("inductionFurnace"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Grinder),
                "ECP", "GGG", "CBC",
                'E', ModBlocks.IndustrialElectrolyzer,
                'P', IC2Items.getItem("pump"),
                'C', "circuitAdvanced",
                'B', IC2Items.getItem("advancedMachine"),
                'G', "craftingGrinder");

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ImplosionCompressor),
                "ABA", "CPC", "ABA",
                'A', IC2Items.getItem("advancedAlloy"),
                'C', "circuitAdvanced",
                'B', IC2Items.getItem("advancedMachine"),
                'P', IC2Items.getItem("compressor"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.VacuumFreezer),
                "SPS", "CGC", "SPS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'G', IC2Items.getItem("reinforcedGlass"),
                'P', IC2Items.getItem("pump"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Distillationtower),
                "CMC", "PBP", "EME",
                'E', ModBlocks.IndustrialElectrolyzer,
                'M', "circuitMaster",
                'B', IC2Items.getItem("advancedMachine"),
                'C', ModBlocks.centrifuge,
                'P', IC2Items.getItem("pump"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.AlloyFurnace),
                "III", "F F", "III",
                'I', "plateIron",
                'F', IC2Items.getItem("ironFurnace"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                "IMI", "CPC", "IEI",
                'I', "plateInvar",
                'C', "circuitAdvanced",
                'M', IC2Items.getItem("magnetizer"),
                'P', IC2Items.getItem("compressor"),
                'E', IC2Items.getItem("extractor"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChemicalReactor),
                "AMA", "CPC", "AEA",
                'A', "plateAluminum",
                'C', "circuitAdvanced",
                'M', IC2Items.getItem("magnetizer"),
                'P', IC2Items.getItem("compressor"),
                'E', IC2Items.getItem("extractor"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.RollingMachine),
                "PCP", "MBM", "PCP",
                'P', "craftingPiston",
                'C', "circuitAdvanced",
                'M', IC2Items.getItem("compressor"),
                'B', IC2Items.getItem("machine"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                "ITI", "IBI", "ICI",
                'I', "plateIron",
                'C', "circuitAdvanced",
                'T', "crafterWood",
                'B', IC2Items.getItem("machine"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ElectricCraftingTable),
                "ATA", "ABA", "ACA",
                'A', "plateAluminum",
                'C', "circuitAdvanced",
                'T', "crafterWood",
                'B', IC2Items.getItem("machine"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.ChunkLoader),
                "SCS", "CMC", "SCS",
                'S', "plateSteel",
                'C', "circuitMaster",
                'M', new ItemStack(ModItems.parts, 1, 39));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.Lesu),
                " L ", "CBC", " M ",
                'L', IC2Items.getItem("lvTransformer"),
                'C', "circuitAdvanced",
                'M', IC2Items.getItem("mvTransformer"),
                'B', ModBlocks.LesuStorage);

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.HighAdvancedMachineBlock),
                "CTC", "TBT", "CTC",
                'C', "plateChrome",
                'T', "plateTitanium",
                'B', IC2Items.getItem("advancedMachine"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 0),
                "III", "CBC", "III",
                'I', "plateIron",
                'C', "circuitBasic",
                'B', IC2Items.getItem("machine"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.MachineCasing, 4, 1),
                "SSS", "CBC", "SSS",
                'S', "plateSteel",
                'C', "circuitAdvanced",
                'B', IC2Items.getItem("advancedMachine"));

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
                'T', IC2Items.getItem("teleporter"));

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.PlasmaGenerator),
                "PPP", "PTP", "CGC",
                'P', ItemPlates.getPlateByName("tungstensteel"),
                'T', IC2Items.getItem("hvTransformer"),
                'G', IC2Items.getItem("generator"),
                'C', ItemParts.getPartByName("energyFlowCircuit")
        );


        //Smetling
        GameRegistry.addSmelting(ItemDusts.getDustByName("copper", 1), IC2Items.getItem("copperIngot"), 1F);
        GameRegistry.addSmelting(ItemDusts.getDustByName("tin", 1), IC2Items.getItem("tinIngot"), 1F);
        GameRegistry.addSmelting(ItemDusts.getDustByName("bronze", 1), IC2Items.getItem("bronzeIngot"), 1F);
        GameRegistry.addSmelting(ItemDusts.getDustByName("lead", 1), IC2Items.getItem("leadIngot"), 1F);
        GameRegistry.addSmelting(ItemDusts.getDustByName("silver", 1), IC2Items.getItem("silverIngot"), 1F);

        //Saw mill
        ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(Blocks.planks, 6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(Blocks.planks, 6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 2), IC2Items.getItem("waterCell"), null, new ItemStack(Blocks.planks, 6, 2), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log, 1, 3), IC2Items.getItem("waterCell"), null, new ItemStack(Blocks.planks, 6, 3), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log2, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(Blocks.planks, 6, 4), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(Blocks.log2, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Blocks.planks, 6, 5), pulpStack, IC2Items.getItem("cell"), 200, 30, false));

        //UU
        if (ConfigTechReborn.UUrecipesIridiamOre)
            CraftingHelper.addShapedOreRecipe((IC2Items.getItem("iridiumOre")),
                    "UUU",
                    " U ",
                    "UUU",
                    'U', ModItems.uuMatter);


        //Blast Furnace
        RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemCells.getCellByName("silicon", 2), null, ItemPlates.getPlateByName("silicon"), new ItemStack(IC2Items.getItem("cell").getItem(), 2), 1000, 120, 1500));

        //CentrifugeRecipes

        //Plantball/Bio Chaff
        if (!IC2Classic.isIc2ClassicLoaded()) {
            RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.grass, 16), null, new ItemStack(IC2Items.getItem("biochaff").getItem(), 8), new ItemStack(IC2Items.getItem("plantBall").getItem(), 8), new ItemStack(Items.clay_ball), new ItemStack(Blocks.sand, 8), 2500, 5));
            RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.dirt, 16), null, new ItemStack(IC2Items.getItem("biochaff").getItem(), 4), new ItemStack(IC2Items.getItem("plantBall").getItem(), 4), new ItemStack(Items.clay_ball), new ItemStack(Blocks.sand, 8), 2500, 5));
        }

        //Methane
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.mushroom_stew, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.apple, 32), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.porkchop, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_porkchop, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.bread, 64), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.fish, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_fished, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.beef, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_beef, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.pumpkin, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.speckled_melon, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), new ItemStack(Items.gold_nugget, 6), null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.spider_eye, 32), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.chicken, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cooked_chicken, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.rotten_flesh, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.melon, 64), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cookie, 64), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.cake, 8), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.golden_carrot, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), new ItemStack(Items.gold_nugget, 6), null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.carrot, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.baked_potato, 24), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.potato, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.poisonous_potato, 12), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.nether_wart, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(IC2Items.getItem("terraWart").getItem(), 16), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.brown_mushroom, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.red_mushroom, 1), IC2Items.getItem("cell"), ItemCells.getCellByName("methane", 1), null, null, null, 5000, 5));

        //Rubber Wood Yields
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(IC2Items.getItem("rubberWood").getItem(), 15), new ItemStack(IC2Items.getItem("cell").getItem(), 5), new ItemStack(IC2Items.getItem("resin").getItem(), 8), new ItemStack(IC2Items.getItem("plantBall").getItem(), 6), ItemCells.getCellByName("methane", 1), ItemCells.getCellByName("carbon", 4), 5000, 5));

        //Soul Sand Byproducts
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Blocks.soul_sand, 16), IC2Items.getItem("cell"), ItemCells.getCellByName("oil", 1), ItemDusts.getDustByName("saltpeter", 4), ItemDusts.getDustByName("coal", 1), new ItemStack(Blocks.sand, 10), 2500, 5));

        //Ice
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("ice", 1), null, new ItemStack(Blocks.ice, 1), IC2Items.getItem("cell"), null, null, 40, 5));


        //Dust Byproducts

        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.glowstone_dust, 16), IC2Items.getItem("cell"), new ItemStack(Items.redstone, 8), ItemDusts.getDustByName("gold", 8), ItemCells.getCellByName("helium", 1), null, 25000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("phosphorous", 5), new ItemStack(IC2Items.getItem("cell").getItem(), 3), ItemCells.getCellByName("calcium", 3), null, null, null, 1280, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("ashes", 1), IC2Items.getItem("cell"), ItemCells.getCellByName("carbon"), null, null, null, 80, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(new ItemStack(Items.redstone, 10), new ItemStack(IC2Items.getItem("cell").getItem(), 4), ItemCells.getCellByName("silicon", 1), ItemDusts.getDustByName("pyrite", 3), ItemDusts.getDustByName("ruby", 1), ItemCells.getCellByName("mercury", 3), 6800, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("endstone", 16), new ItemStack(IC2Items.getItem("cell").getItem(), 2), ItemCells.getCellByName("helium3", 1), ItemCells.getCellByName("helium"), ItemDustsSmall.getSmallDustByName("Tungsten", 1), new ItemStack(Blocks.sand, 12), 4800, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("cinnabar", 2), IC2Items.getItem("cell"), ItemCells.getCellByName("mercury", 1), ItemDusts.getDustByName("sulfur", 1), null, null, 80, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemDusts.getDustByName("flint", 1), null, IC2Items.getItem("silicondioxideDust"), null, null, null, 160, 5));


        //Deuterium/Tritium
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("helium", 16), null, ItemCells.getCellByName("deuterium", 1), new ItemStack(IC2Items.getItem("cell").getItem(), 15), null, null, 10000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("deuterium", 4), null, ItemCells.getCellByName("tritium", 1), new ItemStack(IC2Items.getItem("cell").getItem(), 3), null, null, 3000, 5));
        RecipeHandler.addRecipe(new CentrifugeRecipe(ItemCells.getCellByName("hydrogen", 4), null, ItemCells.getCellByName("deuterium", 1), new ItemStack(IC2Items.getItem("cell").getItem(), 3), null, null, 3000, 5));

        //Lava Cell Byproducts
        ItemStack lavaCells = IC2Items.getItem("lavaCell");
        lavaCells.stackSize = 8;
        RecipeHandler.addRecipe(new CentrifugeRecipe(lavaCells, null, ItemNuggets.getNuggetByName("electrum", 4), ItemIngots.getIngotByName("copper", 2), ItemDustsSmall.getSmallDustByName("Tungsten", 1), ItemIngots.getIngotByName("tin", 2), 6000, 5));

        //IndustrialGrinderRecipes
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.coal_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.coal, 1), ItemDustsSmall.getSmallDustByName("Coal", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("iron", 2), ItemDustsSmall.getSmallDustByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("gold", 2), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.iron_ore, 1), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("gold", 2), ItemDusts.getDustByName("copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.gold_ore, 1), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("gold", 3), ItemDustsSmall.getSmallDustByName("Copper", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.diamond_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.diamond, 1), ItemDustsSmall.getSmallDustByName("Diamond", 6), ItemDustsSmall.getSmallDustByName("Coal", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.emerald_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.emerald, 1), ItemDustsSmall.getSmallDustByName("Emerald", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.redstone_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.redstone, 10), ItemDustsSmall.getSmallDustByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.lapis_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.dye, 6, 4), ItemDustsSmall.getSmallDustByName("Lapis", 36), ItemDustsSmall.getSmallDustByName("Lazurite", 8), IC2Items.getItem("cell"), 100, 120));


        //Copper Ore
        if (OreUtil.doesOreExistAndValid("oreCopper")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCopper").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("copper", 2), ItemDusts.getDustByName("gold", 1), ItemDustsSmall.getSmallDustByName("Nickel", 1), IC2Items.getItem("cell"), 100, 120));

                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("copper", 2), ItemDustsSmall.getSmallDustByName("Gold", 1), ItemDusts.getDustByName("nickel", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Copper Ore");
            }
        }

        //Tin Ore
        if (OreUtil.doesOreExistAndValid("oreTin")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTin").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("tin", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("zinc", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Tin Ore");
            }
        }

        //Nickel Ore
        if (OreUtil.doesOreExistAndValid("oreNickel")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreNickel").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("nickel", 3), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Platinum", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("nickel", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("platinum", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Nickel Ore");
            }
        }

        //Zinc Ore
        if (OreUtil.doesOreExistAndValid("oreZinc")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreZinc").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDustsSmall.getSmallDustByName("Tin", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("zinc", 2), ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("iron", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Zinc Ore");
            }
        }

        //Silver Ore
        if (OreUtil.doesOreExistAndValid("oreSilver")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreSilver").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("silver", 2), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("silver", 3), ItemDustsSmall.getSmallDustByName("Lead", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Silver Ore");
            }
        }

        //Lead Ore
        if (OreUtil.doesOreExistAndValid("oreLead")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreLead").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("lead", 2), ItemDustsSmall.getSmallDustByName("Silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("lead", 2), ItemDusts.getDustByName("silver", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Lead Ore");
            }
        }

        //Uranium Ore
        if (OreUtil.doesOreExistAndValid("oreUranium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreUranium").get(0);
                ItemStack uranium238Stack = IC2Items.getItem("Uran238");
                uranium238Stack.stackSize = 8;
                ItemStack uranium235Stack = IC2Items.getItem("smallUran235");
                uranium235Stack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), uranium238Stack, uranium235Stack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, uranium238Stack, uranium235Stack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, uranium238Stack, uranium235Stack, null, new ItemStack(Items.bucket), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Uranium Ore");
            }
        }

        //Pitchblende Ore
        if (OreUtil.doesOreExistAndValid("orePitchblende")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("orePitchblende").get(0);
                ItemStack uranium238Stack = IC2Items.getItem("Uran238");
                uranium238Stack.stackSize = 8;
                ItemStack uranium235Stack = IC2Items.getItem("smallUran235");
                uranium235Stack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, null, new FluidStack(FluidRegistry.WATER, 1000), uranium238Stack, uranium235Stack, null, null, 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, uranium238Stack, uranium235Stack, null, IC2Items.getItem("cell"), 100, 120));
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, new ItemStack(Items.water_bucket), null, uranium238Stack, uranium235Stack, null, new ItemStack(Items.bucket), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Uranium Ore");
            }
        }

        //Aluminum Ore
        if (OreUtil.doesOreExistAndValid("oreAluminum")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreAluminum").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("aluminum", 2), ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDustsSmall.getSmallDustByName("Bauxite", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Lead Ore");
            }
        }

        //Ardite Ore
        if (OreUtil.doesOreExistAndValid("oreArdite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreArdite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("ardite", 2), ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDustsSmall.getSmallDustByName("Ardite", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Ardite Ore");
            }
        }

        //Cobalt Ore
        if (OreUtil.doesOreExistAndValid("oreCobalt")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCobalt").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("cobalt", 2), ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDustsSmall.getSmallDustByName("Cobalt", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Cobalt Ore");
            }
        }

        //Dark Iron Ore
        if (OreUtil.doesOreExistAndValid("oreDarkIron")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreDarkIron").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("darkIron", 2), ItemDustsSmall.getSmallDustByName("DarkIron", 1), ItemDustsSmall.getSmallDustByName("Iron", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Dark Iron Ore");
            }
        }

        //Cadmium Ore
        if (OreUtil.doesOreExistAndValid("oreCadmium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCadmium").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("cadmium", 2), ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDustsSmall.getSmallDustByName("Cadmium", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Cadmium Ore");
            }
        }

        //Indium Ore
        if (OreUtil.doesOreExistAndValid("oreIndium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreIndium").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("indium", 2), ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDustsSmall.getSmallDustByName("Indium", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Indium Ore");
            }
        }

        //Calcite Ore
        if (OreUtil.doesOreExistAndValid("oreCalcite") && OreUtil.doesOreExistAndValid("gemCalcite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCalcite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemCalcite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, ItemDustsSmall.getSmallDustByName("Calcite", 6), null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Calcite Ore");
            }
        }

        //Magnetite Ore
        if (OreUtil.doesOreExistAndValid("oreMagnetite") && OreUtil.doesOreExistAndValid("chunkMagnetite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreMagnetite").get(0);
                ItemStack chunkStack = OreDictionary.getOres("chunkMagnetite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, chunkStack, ItemDustsSmall.getSmallDustByName("Magnetite", 6), null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Magnetite Ore");
            }
        }

        //Graphite Ore
        if (OreUtil.doesOreExistAndValid("oreGraphite") && OreUtil.doesOreExistAndValid("chunkGraphite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreGraphite").get(0);
                ItemStack chunkStack = OreDictionary.getOres("chunkGraphite").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, chunkStack, ItemDustsSmall.getSmallDustByName("Graphite", 6), null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Graphite Ore");
            }
        }

        //Osmium Ore
        if (OreUtil.doesOreExistAndValid("oreOsmium")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreOsmium").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("osmium", 2), ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDustsSmall.getSmallDustByName("Osmium", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Osmium Ore");
            }
        }

        //Teslatite Ore
        if (OreUtil.doesOreExistAndValid("oreTeslatite") && OreUtil.doesOreExistAndValid("dustTeslatite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTeslatite").get(0);
                ItemStack dustStack = OreDictionary.getOres("dustTeslatite").get(0);
                dustStack.stackSize = 10;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, dustStack, ItemDustsSmall.getSmallDustByName("Sodalite", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Teslatite Ore");
            }
        }

        //Sulfur Ore
        if (OreUtil.doesOreExistAndValid("oreSulfur")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreSulfur").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("sulfur", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Sulfur", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Sulfur Ore");
            }
        }

        //Saltpeter Ore
        if (OreUtil.doesOreExistAndValid("oreSaltpeter")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreSaltpeter").get(0);
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("saltpeter", 2), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), ItemDustsSmall.getSmallDustByName("Saltpeter", 1), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Saltpeter Ore");
            }
        }

        //Apatite Ore
        if (OreUtil.doesOreExistAndValid("oreApatite") & OreUtil.doesOreExistAndValid("gemApatite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreApatite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemApatite").get(0);
                gemStack.stackSize = 6;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, gemStack, ItemDustsSmall.getSmallDustByName("Phosphorous", 4), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Apatite Ore");
            }
        }

        //Nether Quartz Ore
        if (OreUtil.doesOreExistAndValid("dustNetherQuartz")) {
            try {
                ItemStack dustStack = OreDictionary.getOres("dustNetherQuartz").get(0);
                dustStack.stackSize = 4;
                RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(Blocks.quartz_ore, 1), IC2Items.getItem("waterCell"), null, new ItemStack(Items.quartz, 2), dustStack, ItemDustsSmall.getSmallDustByName("Netherrack", 2), IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Nether Quartz Ore");
            }
        }

        //Certus Quartz Ore
        if (OreUtil.doesOreExistAndValid("oreCertusQuartz")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreCertusQuartz").get(0);
                ItemStack gemStack = OreDictionary.getOres("crystalCertusQuartz").get(0);
                ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
                dustStack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
            }
        }

        //Charged Certus Quartz Ore
        if (OreUtil.doesOreExistAndValid("oreChargedCertusQuartz")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreChargedCertusQuartz").get(0);
                ItemStack gemStack = OreDictionary.getOres("crystalChargedCertusQuartz").get(0);
                ItemStack dustStack = OreDictionary.getOres("dustCertusQuartz").get(0);
                dustStack.stackSize = 2;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Charged Certus Quartz Ore");
            }
        }

        //Amethyst Ore
        if (OreUtil.doesOreExistAndValid("oreAmethyst") && OreUtil.doesOreExistAndValid("gemAmethyst")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreAmethyst").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemAmethyst").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemAmethyst").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Certus Quartz Ore");
            }
        }

        //Topaz Ore
        if (OreUtil.doesOreExistAndValid("oreTopaz") && OreUtil.doesOreExistAndValid("gemTopaz")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTopaz").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemTopaz").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemTopaz").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Topaz Ore");
            }
        }

        //Tanzanite Ore
        if (OreUtil.doesOreExistAndValid("oreTanzanite") && OreUtil.doesOreExistAndValid("gemTanzanite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreTanzanite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemTanzanite").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemTanzanite").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Tanzanite Ore");
            }
        }

        //Malachite Ore
        if (OreUtil.doesOreExistAndValid("oreMalachite") && OreUtil.doesOreExistAndValid("gemMalachite")) {
            try {
                ItemStack oreStack = OreDictionary.getOres("oreMalachite").get(0);
                ItemStack gemStack = OreDictionary.getOres("gemMalachite").get(0);
                gemStack.stackSize = 2;
                ItemStack dustStack = OreDictionary.getOres("gemMalachite").get(0);
                dustStack.stackSize = 1;
                RecipeHandler.addRecipe(new GrinderRecipe(oreStack, IC2Items.getItem("waterCell"), null, gemStack, dustStack, null, IC2Items.getItem("cell"), 100, 120));
            } catch (Exception e) {
                Core.logHelper.info("Failed to Load Grinder Recipe for Malachite Ore");
            }
        }


        //Implosion Compressor

        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemParts.getPartByName("iridiumAlloyIngot"), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 8), IC2Items.getItem("iridiumPlate"), ItemDusts.getDustByName("darkAshes", 4), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("diamond", 4), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 32), new ItemStack(IC2Items.getItem("industrialDiamond").getItem(), 3), ItemDusts.getDustByName("darkAshes", 16), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(IC2Items.getItem("coalChunk"), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 8), IC2Items.getItem("industrialDiamond"), ItemDusts.getDustByName("darkAshes", 4), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("emerald", 4), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 24), new ItemStack(Items.emerald, 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("sapphire", 4), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 24), ItemGems.getGemByName("sapphire", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("ruby", 4), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 24), ItemGems.getGemByName("ruby", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("yellowGarnet", 4), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 24), ItemGems.getGemByName("yellowGarnet", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("redGarnet", 4), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 24), ItemGems.getGemByName("redGarnet", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));
        RecipeHandler.addRecipe(new ImplosionCompressorRecipe(ItemDusts.getDustByName("peridot", 4), new ItemStack(IC2Items.getItem("industrialTnt").getItem(), 24), ItemGems.getGemByName("peridot", 3), ItemDusts.getDustByName("darkAshes", 12), 20, 30));


        //Grinder


        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 0), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("galena", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("silver", 1), IC2Items.getItem("cell"), 100, 120));

        //Iridium Ore
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDustsSmall.getSmallDustByName("Platinum", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), IC2Items.getItem("waterCell"), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDustsSmall.getSmallDustByName("Platinum", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), new ItemStack(Items.water_bucket), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDustsSmall.getSmallDustByName("Platinum", 2), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), null, new FluidStack(ModFluids.fluidMercury, 1000), IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDusts.getDustByName("platinum", 2), null, 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), ItemCells.getCellByName("mercury", 1), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDusts.getDustByName("platinum", 2), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 1), new ItemStack(ModItems.bucketMercury), null, IC2Items.getItem("iridiumOre"), ItemDustsSmall.getSmallDustByName("Iridium", 6), ItemDusts.getDustByName("platinum", 2), new ItemStack(Items.bucket), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 2), IC2Items.getItem("waterCell"), null, ItemGems.getGemByName("ruby", 1), ItemDustsSmall.getSmallDustByName("Ruby", 6), ItemDustsSmall.getSmallDustByName("Chrome", 2), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 3), IC2Items.getItem("waterCell"), null, ItemGems.getGemByName("sapphire", 1), ItemDustsSmall.getSmallDustByName("Sapphire", 6), ItemDustsSmall.getSmallDustByName("Aluminum", 2), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 4), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("bauxite", 2), ItemDustsSmall.getSmallDustByName("Grossular", 4), ItemDustsSmall.getSmallDustByName("Titanium", 4), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 5), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("pyrite", 2), ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDustsSmall.getSmallDustByName("Phosphorous", 1), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 6), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("cinnabar", 2), ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDustsSmall.getSmallDustByName("Glowstone", 1), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("sphalerite", 2), ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 7), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("sphalerite", 2), ItemDusts.getDustByName("zinc", 1), ItemDustsSmall.getSmallDustByName("YellowGarnet", 1), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDustsSmall.getSmallDustByName("Silver", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 8), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("tungsten", 2), ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("silver", 2), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("platinum", 2), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 9), ItemCells.getCellByName("mercury", 1), null, ItemDusts.getDustByName("platinum", 3), ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 1), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 10), IC2Items.getItem("waterCell"), null, ItemGems.getGemByName("peridot", 1), ItemDustsSmall.getSmallDustByName("Peridot", 6), ItemDustsSmall.getSmallDustByName("Pyrope", 2), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 11), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("sodalite", 12), ItemDustsSmall.getSmallDustByName("Lazurite", 4), ItemDustsSmall.getSmallDustByName("Lapis", 4), IC2Items.getItem("cell"), 100, 120));

        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), IC2Items.getItem("waterCell"), null, ItemDusts.getDustByName("tetrahedrite", 2), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), IC2Items.getItem("cell"), 100, 120));
        RecipeHandler.addRecipe(new GrinderRecipe(new ItemStack(ModBlocks.ore, 1, 12), ItemCells.getCellByName("sodiumPersulfate", 1), null, ItemDusts.getDustByName("tetrahedrite", 3), ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDustsSmall.getSmallDustByName("Zinc", 1), IC2Items.getItem("cell"), 100, 120));


        //Chemical Reactor
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1), ItemDusts.getDustByName("phosphorous", 1), new ItemStack(IC2Items.getItem("fertilizer").getItem(), 3), 100, 30));
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1), null, new ItemStack(IC2Items.getItem("fertilizer").getItem(), 1), 100, 30));
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("sodiumSulfide", 1), IC2Items.getItem("airCell"), ItemCells.getCellByName("sodiumPersulfate", 2), 2000, 30));
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("nitrocarbon", 1), IC2Items.getItem("waterCell"), ItemCells.getCellByName("glyceryl", 2), 580, 30));
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1), ItemDusts.getDustByName("sulfur", 1), new ItemStack(IC2Items.getItem("fertilizer").getItem(), 2), 100, 30));
        ItemStack waterCells = IC2Items.getItem("waterCell").copy();
        waterCells.stackSize = 2;
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("sulfur", 1), waterCells, ItemCells.getCellByName("sulfuricAcid", 3), 1140, 30));
        ItemStack waterCells2 = IC2Items.getItem("waterCell").copy();
        waterCells2.stackSize = 5;
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("hydrogen", 4), IC2Items.getItem("airCell"), waterCells2, 10, 30));
        RecipeHandler.addRecipe(new ChemicalReactorRecipe(ItemCells.getCellByName("nitrogen", 1), IC2Items.getItem("airCell"), ItemCells.getCellByName("nitrogenDioxide", 2), 1240, 30));


//IndustrialElectrolyzer

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemCells.getCellByName("sulfuricAcid", 7),
                null,
                ItemCells.getCellByName("hydrogen", 2),
                ItemDusts.getDustByName("sulfur"),
                new ItemStack(IC2Items.getItem("cell").getItem(), 2, 5),
                new ItemStack(IC2Items.getItem("cell").getItem(), 3, 0),
                400, 90
        ));


        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("ruby", 6),
                IC2Items.getItem("cell"),
                ItemDusts.getDustByName("aluminum", 2),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                ItemDusts.getDustByName("chrome", 1),
                null,
                140, 90
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("sapphire", 5),
                IC2Items.getItem("cell"),
                ItemDusts.getDustByName("aluminum", 2),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                null,
                null,
                100, 60
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemCells.getCellByName("nitrogenDioxide", 3),
                null,
                ItemCells.getCellByName("nitrogen", 1),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                null,
                IC2Items.getItem("cell"),
                160, 60
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemCells.getCellByName("sodiumSulfide", 2),
                null,
                ItemCells.getCellByName("sodium", 1),
                ItemDusts.getDustByName("sulfur", 1),
                null,
                IC2Items.getItem("cell"),
                200, 60
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("greenSapphire", 5),
                IC2Items.getItem("cell"),
                ItemDusts.getDustByName("aluminum", 2),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                null,
                null,
                100, 60
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("emerald", 29),
                new ItemStack(IC2Items.getItem("cell").getItem(), 18, 0),
                ItemCells.getCellByName("berylium", 3),
                ItemDusts.getDustByName("aluminum", 2),
                ItemCells.getCellByName("silicon", 6),
                new ItemStack(IC2Items.getItem("cell").getItem(), 9, 5),
                520, 120
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                new ItemStack(IC2Items.getItem("silicondioxideDust").getItem(), 3, 0),
                new ItemStack(IC2Items.getItem("cell").getItem(), 2, 0),
                ItemCells.getCellByName("silicon", 1),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                null,
                null,
                60, 60
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(new ItemStack(Items.dye, 3, 15),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 0),
                null,
                ItemCells.getCellByName("calcium", 1),
                null,
                null,
                20, 106
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemCells.getCellByName("glyceryl", 20),
                null,
                ItemCells.getCellByName("carbon", 3),
                ItemCells.getCellByName("hydrogen", 5),
                ItemCells.getCellByName("nitrogen", 3),
                new ItemStack(IC2Items.getItem("cell").getItem(), 9, 0),
                800, 90
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("peridot", 9),
                new ItemStack(IC2Items.getItem("cell").getItem(), 4, 0),
                ItemDusts.getDustByName("magnesium", 2),
                ItemDusts.getDustByName("iron"),
                ItemCells.getCellByName("silicon", 2),
                new ItemStack(IC2Items.getItem("cell").getItem(), 2, 5),
                200, 120
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemCells.getCellByName("calciumCarbonate", 5),
                null,
                ItemCells.getCellByName("carbon"),
                ItemCells.getCellByName("calcium"),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                new ItemStack(IC2Items.getItem("cell").getItem(), 2, 0),
                400, 90
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemCells.getCellByName("sodiumPersulfate", 6),
                null,
                ItemCells.getCellByName("sodium"),
                ItemDusts.getDustByName("sulfur"),
                new ItemStack(IC2Items.getItem("cell").getItem(), 2, 5),
                new ItemStack(IC2Items.getItem("cell").getItem(), 3, 0),
                420, 90
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("pyrope", 20),
                new ItemStack(IC2Items.getItem("cell").getItem(), 9, 0),
                ItemDusts.getDustByName("aluminum", 2),
                ItemDusts.getDustByName("magnesium", 3),
                ItemCells.getCellByName("silicon", 3),
                new ItemStack(IC2Items.getItem("cell").getItem(), 6, 5),
                400, 120
        ));

        ItemStack sand = new ItemStack(Blocks.sand);
        sand.stackSize = 16;

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                sand,
                new ItemStack(IC2Items.getItem("cell").getItem(), 2, 0),
                ItemCells.getCellByName("silicon", 1),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                null,
                null,
                1000, 25
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("almandine", 20),
                new ItemStack(IC2Items.getItem("cell").getItem(), 9, 0),
                ItemDusts.getDustByName("aluminum", 2),
                ItemDusts.getDustByName("iron", 3),
                ItemCells.getCellByName("silicon", 3),
                new ItemStack(IC2Items.getItem("cell").getItem(), 6, 5),
                480, 120
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("spessartine", 20),
                new ItemStack(IC2Items.getItem("cell").getItem(), 9, 0),
                ItemDusts.getDustByName("aluminum", 2),
                ItemDusts.getDustByName("manganese", 3),
                ItemCells.getCellByName("silicon", 3),
                new ItemStack(IC2Items.getItem("cell").getItem(), 6, 5),
                480, 120
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("andradite", 20),
                new ItemStack(IC2Items.getItem("cell").getItem(), 12, 0),
                ItemCells.getCellByName("calcium", 3),
                ItemDusts.getDustByName("iron", 2),
                ItemCells.getCellByName("silicon", 3),
                new ItemStack(IC2Items.getItem("cell").getItem(), 6, 5),
                480, 120
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("grossular", 20),
                new ItemStack(IC2Items.getItem("cell").getItem(), 12, 0),
                ItemCells.getCellByName("calcium", 3),
                ItemDusts.getDustByName("aluminum", 2),
                ItemCells.getCellByName("silicon", 3),
                new ItemStack(IC2Items.getItem("cell").getItem(), 6, 5),
                440, 120
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("Uvarovite", 20),
                new ItemStack(IC2Items.getItem("cell").getItem(), 12, 0),
                ItemCells.getCellByName("calcium", 3),
                ItemDusts.getDustByName("chrome", 2),
                ItemCells.getCellByName("silicon", 3),
                new ItemStack(IC2Items.getItem("cell").getItem(), 6, 5),
                480, 120
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                new ItemStack(IC2Items.getItem("cell").getItem(), 6, 10),
                null,
                ItemCells.getCellByName("hydrogen", 4),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 5),
                new ItemStack(IC2Items.getItem("cell").getItem(), 1, 0),
                null,
                100, 30
        ));

        RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                ItemDusts.getDustByName("darkAshes"),
                new ItemStack(IC2Items.getItem("cell").getItem(), 2, 0),
                ItemCells.getCellByName("carbon", 2),
                null,
                null,
                null,
                20, 30
        ));

        if (OreUtil.doesOreExistAndValid("dustSalt")) {
            ItemStack salt = OreDictionary.getOres("dustSalt").get(0);
            salt.stackSize = 2;
            RecipeHandler.addRecipe(new IndustrialElectrolyzerRecipe(
                    salt,
                    new ItemStack(IC2Items.getItem("cell").getItem(), 2, 0),
                    ItemCells.getCellByName("sodium"),
                    ItemCells.getCellByName("chlorine"),
                    null,
                    null,
                    40, 60
            ));
        }

        CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("NaKCoolantSimple"),
                "TST", "PCP", "TST",
                'T', "ingotTin",
                'S', ItemCells.getCellByName("sodium"),
                'P', ItemCells.getCellByName("potassium"),
                'C', IC2Items.getItem("reactorCoolantSimple")
        );
    }

    static void removeIc2Recipes() {
        if (ConfigTechReborn.ExpensiveMacerator) {
            RecipeRemover.removeAnyRecipe(IC2Items.getItem("macerator"));
        }
        if (ConfigTechReborn.ExpensiveDrill) {
            RecipeRemover.removeAnyRecipe(IC2Items.getItem("miningDrill"));
        }
        if (ConfigTechReborn.ExpensiveDiamondDrill) {
            RecipeRemover.removeAnyRecipe(IC2Items.getItem("diamondDrill"));
        }
        if (ConfigTechReborn.ExpensiveSolar) {
            RecipeRemover.removeAnyRecipe(IC2Items.getItem("solarPanel"));
        }
        if (ConfigTechReborn.ExpensiveWatermill) {
            RecipeRemover.removeAnyRecipe(IC2Items.getItem("waterMill"));
        }
        if (ConfigTechReborn.ExpensiveWindmill) {
            RecipeRemover.removeAnyRecipe(IC2Items.getItem("windMill"));
        }

        Core.logHelper.info("IC2 Recipes Removed");
    }

    static void addShappedIc2Recipes() {
        Item drill = IC2Items.getItem("miningDrill").getItem();
        ItemStack drillStack = new ItemStack(drill, 1, OreDictionary.WILDCARD_VALUE);

        if (ConfigTechReborn.ExpensiveMacerator)
            CraftingHelper.addShapedOreRecipe(IC2Items.getItem("macerator"),
                    "FDF", "DMD", "FCF",
                    'F', Items.flint,
                    'D', Items.diamond,
                    'M', IC2Items.getItem("machine"),
                    'C', IC2Items.getItem("electronicCircuit"));

        if (ConfigTechReborn.ExpensiveDrill)
            CraftingHelper.addShapedOreRecipe(IC2Items.getItem("miningDrill"),
                    " S ", "SCS", "SBS",
                    'S', "ingotSteel",
                    'B', IC2Items.getItem("reBattery"),
                    'C', IC2Items.getItem("electronicCircuit"));

        if (ConfigTechReborn.ExpensiveDiamondDrill)
            CraftingHelper.addShapedOreRecipe(IC2Items.getItem("diamondDrill"),
                    " D ", "DBD", "TCT",
                    'D', "gemDiamond",
                    'T', "ingotTitanium",
                    'B', drillStack,
                    'C', IC2Items.getItem("advancedCircuit"));

        if (ConfigTechReborn.ExpensiveSolar)
            CraftingHelper.addShapedOreRecipe(IC2Items.getItem("solarPanel"),
                    "PPP", "SZS", "CGC",
                    'P', "paneGlass",
                    'S', new ItemStack(ModItems.parts, 1, 1),
                    'Z', IC2Items.getItem("carbonPlate"),
                    'G', IC2Items.getItem("generator"),
                    'C', IC2Items.getItem("electronicCircuit"));


        CraftingHelper.addShapedOreRecipe(ItemParts.getPartByName("iridiumAlloyIngot"),
                "IAI", "ADA", "IAI",
                'I', ItemIngots.getIngotByName("iridium"),
                'D', ItemDusts.getDustByName("diamond"),
                'A', IC2Items.getItem("advancedAlloy"));

        Core.logHelper.info("Added Expensive IC2 Recipes");
    }

    static void addTRMaceratorRecipes() {
        //Macerator

        if (OreUtil.doesOreExistAndValid("oreAluminum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreAluminum"), null, ItemCrushedOre.getCrushedOreByName("Aluminum", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreArdite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreArdite"), null, ItemCrushedOre.getCrushedOreByName("Ardite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreBauxite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreBauxite"), null, ItemCrushedOre.getCrushedOreByName("Bauxite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreCadmium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCadmium"), null, ItemCrushedOre.getCrushedOreByName("Cadmium", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreCinnabar")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCinnabar"), null, ItemCrushedOre.getCrushedOreByName("Cinnabar", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreCobalt")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCobalt"), null, ItemCrushedOre.getCrushedOreByName("Cobalt", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreDarkIron")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDarkIron"), null, ItemCrushedOre.getCrushedOreByName("DarkIron", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreIndium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIndium"), null, ItemCrushedOre.getCrushedOreByName("Indium", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreIridium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreIridium"), null, ItemCrushedOre.getCrushedOreByName("Iridium", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreNickel")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreNickel"), null, ItemCrushedOre.getCrushedOreByName("Nickel", 2));
        }
        if (OreUtil.doesOreExistAndValid("orePlatinum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePlatinum"), null, ItemCrushedOre.getCrushedOreByName("Platinum", 2));
        }
        if (OreUtil.doesOreExistAndValid("orePyrite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePyrite"), null, ItemCrushedOre.getCrushedOreByName("Pyrite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreSphalerite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSphalerite"), null, ItemCrushedOre.getCrushedOreByName("Sphalerite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreTetrahedrite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTetrahedrite"), null, ItemCrushedOre.getCrushedOreByName("Tetrahedrite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreTungsten")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTungsten"), null, ItemCrushedOre.getCrushedOreByName("Tungsten", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreGalena")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGalena"), null, ItemCrushedOre.getCrushedOreByName("Galena", 2));
        }


        if (!IC2Classic.isIc2ClassicLoaded() && OreUtil.doesOreExistAndValid("oreRedstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRedstone"), null, new ItemStack(Items.redstone, 10));
        }
        if (OreUtil.doesOreExistAndValid("oreLapis")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLapis"), null, ItemDusts.getDustByName("lapis", 12));
        }
        if (OreUtil.doesOreExistAndValid("oreDiamond")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreDiamond"), null, ItemDusts.getDustByName("diamond", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreEmerald")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreEmerald"), null, ItemDusts.getDustByName("emerald", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreRuby")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreRuby"), null, ItemGems.getGemByName("ruby", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreSapphire")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSapphire"), null, ItemDusts.getDustByName("sapphire", 2));
        }
        if (OreUtil.doesOreExistAndValid("orePeridot")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("orePeridot"), null, ItemDusts.getDustByName("peridot", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreSulfur")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSulfur"), null, ItemDusts.getDustByName("sulfur", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreSaltpeter")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSaltpeter"), null, ItemDusts.getDustByName("saltpeter", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreTeslatite")) {
            ItemStack teslatiteStack = OreDictionary.getOres("dustTeslatite").get(0);
            teslatiteStack.stackSize = 10;
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTeslatite"), null, teslatiteStack);
        }
        if (OreUtil.doesOreExistAndValid("oreMithril")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMithril"), null, ItemDusts.getDustByName("mithril", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreVinteum")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVinteum"), null, ItemDusts.getDustByName("vinteum", 2));
        }
        if (OreUtil.doesOreExistAndValid("limestone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("limestone"), null, ItemDusts.getDustByName("limestone", 2));
        }
        if (OreUtil.doesOreExistAndValid("stoneNetherrack")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneNetherrack"), null, ItemDusts.getDustByName("netherrack", 2));
        }
        if (OreUtil.doesOreExistAndValid("stoneEndstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneEndstone"), null, ItemDusts.getDustByName("endstone", 2));
        }
        if (OreUtil.doesOreExistAndValid("stoneRedrock")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("stoneRedrock"), null, ItemDusts.getDustByName("redrock", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreMagnetite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreMagnetite"), null, ItemDusts.getDustByName("magnetite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreLodestone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreLodestone"), null, ItemDusts.getDustByName("lodestone", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreTellurium")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreTellurium"), null, ItemDusts.getDustByName("tellurium", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreSilicon")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSilicon"), null, ItemDusts.getDustByName("silicon", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreVoidstone")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreVoidstone"), null, ItemDusts.getDustByName("voidstone", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreCalcite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreCalcite"), null, ItemDusts.getDustByName("calcite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreSodalite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreSodalite"), null, ItemDusts.getDustByName("sodalite", 2));
        }
        if (OreUtil.doesOreExistAndValid("oreGraphite")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("oreGraphite"), null, ItemDusts.getDustByName("graphite", 2));
        }
        if (OreUtil.doesOreExistAndValid("blockMarble")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("blockMarble"), null, ItemDusts.getDustByName("marble", 2));
        }
        if (OreUtil.doesOreExistAndValid("blockBasalt")) {
            Recipes.macerator.addRecipe(new RecipeInputOreDict("blockBasalt"), null, ItemDusts.getDustByName("basalt", 2));
        }
        if (OreUtil.doesOreExistAndValid("gemRuby")) {
            Recipes.macerator.getRecipes().put(new RecipeInputOreDict("gemRuby"), new RecipeOutput(new NBTTagCompound(), ItemDusts.getDustByName("ruby")));
        }
    }

    static void addTROreWashingRecipes() {
        //Ore Washing Plant
        NBTTagCompound liquidAmount = new NBTTagCompound();
        liquidAmount.setInteger("amount", 1000);
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedAluminum"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Aluminum", 1), ItemDustsSmall.getSmallDustByName("Aluminum", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedArdite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Ardite", 1), ItemDustsSmall.getSmallDustByName("Ardite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedBauxite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Bauxite", 1), ItemDustsSmall.getSmallDustByName("Bauxite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCadmium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cadmium", 1), ItemDustsSmall.getSmallDustByName("Cadmium", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCinnabar"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cinnabar", 1), ItemDustsSmall.getSmallDustByName("Cinnabar", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedCobalt"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Cobalt", 1), ItemDustsSmall.getSmallDustByName("Cobalt", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedDarkIron"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("DarkIron", 1), ItemDustsSmall.getSmallDustByName("DarkIron", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIndium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Indium", 1), ItemDustsSmall.getSmallDustByName("Indium", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedNickel"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Nickel", 1), ItemDustsSmall.getSmallDustByName("Nickel", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedOsmium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Osmium", 1), ItemDustsSmall.getSmallDustByName("Osmium", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPyrite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Pyrite", 1), ItemDustsSmall.getSmallDustByName("Pyrite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedSphalerite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Sphalerite", 1), ItemDustsSmall.getSmallDustByName("Sphalerite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Tetrahedrite", 1), ItemDustsSmall.getSmallDustByName("Tetrahedrite", 2), IC2Items.getItem("stoneDust"));
        Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedGalena"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Galena", 1), ItemDustsSmall.getSmallDustByName("Galena", 2), IC2Items.getItem("stoneDust"));

        if (!Loader.isModLoaded("aobd")) {
            Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedPlatinum"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Platinum", 1), ItemDustsSmall.getSmallDustByName("Platinum", 2), IC2Items.getItem("stoneDust"));
            Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedIridium"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Iridium", 1), ItemDustsSmall.getSmallDustByName("Iridium", 2), IC2Items.getItem("stoneDust"));
            Recipes.oreWashing.addRecipe(new RecipeInputOreDict("crushedTungsten"), liquidAmount, ItemPurifiedCrushedOre.getPurifiedCrushedOreByName("Tungsten", 1), ItemDustsSmall.getSmallDustByName("Tungsten", 2), IC2Items.getItem("stoneDust"));
        }
    }

    static void addTRThermalCentrifugeRecipes() {
        //Thermal Centrifuge

        //Heat Values
        NBTTagCompound aluminumHeat = new NBTTagCompound();
        aluminumHeat.setInteger("minHeat", 2000);
        NBTTagCompound arditeHeat = new NBTTagCompound();
        arditeHeat.setInteger("minHeat", 3000);
        NBTTagCompound bauxiteHeat = new NBTTagCompound();
        bauxiteHeat.setInteger("minHeat", 2500);
        NBTTagCompound cadmiumHeat = new NBTTagCompound();
        cadmiumHeat.setInteger("minHeat", 1500);
        NBTTagCompound cinnabarHeat = new NBTTagCompound();
        cinnabarHeat.setInteger("minHeat", 1500);
        NBTTagCompound cobaltHeat = new NBTTagCompound();
        cobaltHeat.setInteger("minHeat", 3000);
        NBTTagCompound darkIronHeat = new NBTTagCompound();
        darkIronHeat.setInteger("minHeat", 2500);
        NBTTagCompound indiumHeat = new NBTTagCompound();
        indiumHeat.setInteger("minHeat", 2000);
        NBTTagCompound iridiumHeat = new NBTTagCompound();
        iridiumHeat.setInteger("minHeat", 4000);
        NBTTagCompound nickelHeat = new NBTTagCompound();
        nickelHeat.setInteger("minHeat", 2000);
        NBTTagCompound osmiumHeat = new NBTTagCompound();
        osmiumHeat.setInteger("minHeat", 2000);
        NBTTagCompound platinumHeat = new NBTTagCompound();
        platinumHeat.setInteger("minHeat", 3000);
        NBTTagCompound pyriteHeat = new NBTTagCompound();
        pyriteHeat.setInteger("minHeat", 1500);
        NBTTagCompound sphaleriteHeat = new NBTTagCompound();
        sphaleriteHeat.setInteger("minHeat", 1500);
        NBTTagCompound tetrahedriteHeat = new NBTTagCompound();
        tetrahedriteHeat.setInteger("minHeat", 500);
        NBTTagCompound tungstenHeat = new NBTTagCompound();
        tungstenHeat.setInteger("minHeat", 2000);
        NBTTagCompound galenaHeat = new NBTTagCompound();
        galenaHeat.setInteger("minHeat", 2500);

        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedAluminum"), aluminumHeat, ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDusts.getDustByName("aluminum", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedArdite"), arditeHeat, ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDusts.getDustByName("ardite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedBauxite"), bauxiteHeat, ItemDustsSmall.getSmallDustByName("Aluminum", 1), ItemDusts.getDustByName("bauxite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCadmium"), cadmiumHeat, ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDusts.getDustByName("cadmium", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCinnabar"), cinnabarHeat, ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDusts.getDustByName("cinnabar", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedCobalt"), cobaltHeat, ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDusts.getDustByName("cobalt", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedDarkIron"), darkIronHeat, ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("darkIron", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIndium"), indiumHeat, ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDusts.getDustByName("indium", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedNickel"), nickelHeat, ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("nickel", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedOsmium"), osmiumHeat, ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDusts.getDustByName("osmium", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPyrite"), pyriteHeat, ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("pyrite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedSphalerite"), sphaleriteHeat, ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDusts.getDustByName("sphalerite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTetrahedrite"), tetrahedriteHeat, ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDusts.getDustByName("tetrahedrite", 1), IC2Items.getItem("stoneDust"));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedGalena"), galenaHeat, ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("galena", 1), IC2Items.getItem("stoneDust"));

        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedAluminum"), aluminumHeat, ItemDustsSmall.getSmallDustByName("Bauxite", 1), ItemDusts.getDustByName("aluminum", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedArdite"), arditeHeat, ItemDustsSmall.getSmallDustByName("Ardite", 1), ItemDusts.getDustByName("ardite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedBauxite"), bauxiteHeat, ItemDustsSmall.getSmallDustByName("Aluminum", 1), ItemDusts.getDustByName("bauxite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCadmium"), cadmiumHeat, ItemDustsSmall.getSmallDustByName("Cadmium", 1), ItemDusts.getDustByName("cadmium", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCinnabar"), cinnabarHeat, ItemDustsSmall.getSmallDustByName("Redstone", 1), ItemDusts.getDustByName("cinnabar", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedCobalt"), cobaltHeat, ItemDustsSmall.getSmallDustByName("Cobalt", 1), ItemDusts.getDustByName("cobalt", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedDarkIron"), darkIronHeat, ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("darkIron", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIndium"), indiumHeat, ItemDustsSmall.getSmallDustByName("Indium", 1), ItemDusts.getDustByName("indium", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedNickel"), nickelHeat, ItemDustsSmall.getSmallDustByName("Iron", 1), ItemDusts.getDustByName("nickel", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedOsmium"), osmiumHeat, ItemDustsSmall.getSmallDustByName("Osmium", 1), ItemDusts.getDustByName("osmium", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPyrite"), pyriteHeat, ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("pyrite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedSphalerite"), sphaleriteHeat, ItemDustsSmall.getSmallDustByName("Zinc", 1), ItemDusts.getDustByName("sphalerite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTetrahedrite"), tetrahedriteHeat, ItemDustsSmall.getSmallDustByName("Antimony", 1), ItemDusts.getDustByName("tetrahedrite", 1));
        Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedGalena"), galenaHeat, ItemDustsSmall.getSmallDustByName("Sulfur", 1), ItemDusts.getDustByName("galena", 1));

        if (!Loader.isModLoaded("aobd")) {
            Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedIridium"), iridiumHeat, ItemDustsSmall.getSmallDustByName("Platinum", 1), ItemDusts.getDustByName("iridium", 1), IC2Items.getItem("stoneDust"));
            Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPlatinum"), platinumHeat, ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDusts.getDustByName("platinum", 1), IC2Items.getItem("stoneDust"));
            Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedTungsten"), tungstenHeat, ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("tungsten", 1), IC2Items.getItem("stoneDust"));

            Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedIridium"), iridiumHeat, ItemDustsSmall.getSmallDustByName("Platinum", 1), ItemDusts.getDustByName("iridium", 1));
            Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedPlatinum"), platinumHeat, ItemDustsSmall.getSmallDustByName("Iridium", 1), ItemDusts.getDustByName("platinum", 1));
            Recipes.centrifuge.addRecipe(new RecipeInputOreDict("crushedPurifiedTungsten"), tungstenHeat, ItemDustsSmall.getSmallDustByName("Manganese", 1), ItemDusts.getDustByName("tungsten", 1));
        }
    }

    static void addMetalFormerRecipes() {
        //Metal Former
        NBTTagCompound mode = new NBTTagCompound();
        mode.setInteger("mode", 1);
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotAluminum"), mode, ItemPlates.getPlateByName("aluminum"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBatteryAlloy"), mode, ItemPlates.getPlateByName("batteryAlloy"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBrass"), mode, ItemPlates.getPlateByName("brass"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotChrome"), mode, ItemPlates.getPlateByName("chrome"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotElectrum"), mode, ItemPlates.getPlateByName("electrum"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotInvar"), mode, ItemPlates.getPlateByName("invar"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotIridium"), mode, ItemPlates.getPlateByName("iridium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotMagnalium"), mode, ItemPlates.getPlateByName("magnalium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotNickel"), mode, ItemPlates.getPlateByName("nickel"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotOsmium"), mode, ItemPlates.getPlateByName("osmium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotPlatinum"), mode, ItemPlates.getPlateByName("platinum"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotSilver"), mode, ItemPlates.getPlateByName("silver"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotTitanium"), mode, ItemPlates.getPlateByName("titanium"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotTungsten"), mode, ItemPlates.getPlateByName("tungsten"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotTungstensteel"), mode, ItemPlates.getPlateByName("tungstensteel"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotZinc"), mode, ItemPlates.getPlateByName("zinc"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotRedAlloy"), mode, ItemPlates.getPlateByName("redstone"));
        Recipes.metalformerRolling.addRecipe(new RecipeInputOreDict("ingotBlueAlloy"), mode, ItemPlates.getPlateByName("teslatite"));
    }
}
