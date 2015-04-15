package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.config.ConfigTechReborn;
import techreborn.util.CraftingHelper;
import techreborn.util.LogHelper;
import techreborn.util.RecipeRemover;

public class ModRecipes {
    public static ConfigTechReborn config;

    public static void init() {
        removeIc2Recipes();
        addShaplessRecipes();
        addShappedRecipes();
        addSmeltingRecipes();
        addMachineRecipes();
    }

    public static void removeIc2Recipes() {
        if (config.ExpensiveMacerator) ;
        RecipeRemover.removeAnyRecipe(IC2Items.getItem("macerator"));
        if (config.ExpensiveDrill) ;
        RecipeRemover.removeAnyRecipe(IC2Items.getItem("miningDrill"));
        if (config.ExpensiveDiamondDrill) ;
        RecipeRemover.removeAnyRecipe(IC2Items.getItem("diamondDrill"));
        if (config.ExpensiveSolar) ;
        RecipeRemover.removeAnyRecipe(IC2Items.getItem("solarPanel"));

        LogHelper.info("IC2 Recipes Removed");
    }

    public static void addShappedRecipes() {

        //IC2 Recipes
        if (config.ExpensiveMacerator) ;
        CraftingHelper.addShapedOreRecipe(IC2Items.getItem("macerator"),
                new Object[]{"FDF", "DMD", "FCF",
                        'F', Items.flint,
                        'D', Items.diamond,
                        'M', IC2Items.getItem("machine"),
                        'C', IC2Items.getItem("electronicCircuit")});
        if (config.ExpensiveDrill) ;
        CraftingHelper.addShapedOreRecipe(IC2Items.getItem("miningDrill"),
                new Object[]{" S ", "SCS", "SBS",
                        'S', "ingotSteel",
                        'B', IC2Items.getItem("reBattery"),
                        'C', IC2Items.getItem("electronicCircuit")});
        if (config.ExpensiveDiamondDrill) ;
        CraftingHelper.addShapedOreRecipe(IC2Items.getItem("diamondDrill"),
                new Object[]{" D ", "DBD", "TCT",
                        'D', "gemDiamond",
                        'T', "ingotTitanium",
                        'B', IC2Items.getItem("miningDrill"),
                        'C', IC2Items.getItem("advancedCircuit")});
        if (config.ExpensiveSolar) ;
        CraftingHelper.addShapedOreRecipe(IC2Items.getItem("solarPanel"),
                new Object[]{"PPP", "SZS", "CGC",
                        'P', "paneGlass",
                        'S', new ItemStack(ModItems.parts, 1, 1),
                        'Z', IC2Items.getItem("carbonPlate"),
                        'G', IC2Items.getItem("generator"),
                        'C', IC2Items.getItem("electronicCircuit")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.thermalGenerator),
                new Object[]{"III", "IHI", "CGC",
                        'I', "ingotInvar",
                        'H', IC2Items.getItem("reinforcedGlass"),
                        'C', IC2Items.getItem("electronicCircuit"),
                        'G', IC2Items.getItem("geothermalGenerator")});
        //TechReborn Recipes
        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 6),
                new Object[]{"EEE", "EAE", "EEE",
                        'E', "gemEmerald",
                        'A', IC2Items.getItem("electronicCircuit")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 7),
                new Object[]{"AGA", "RPB", "ASA",
                        'A', "ingotAluminium",
                        'G', "dyeGreen",
                        'R', "dyeRed",
                        'P', "paneGlass",
                        'B', "dyeBlue",
                        'S', Items.glowstone_dust,});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 4, 8),
                new Object[]{"DSD", "S S", "DSD",
                        'D', "dustDiamond",
                        'S', "ingotSteel"});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 16, 13),
                new Object[]{"CSC", "SCS", "CSC",
                        'S', "ingotSteel",
                        'C', IC2Items.getItem("electronicCircuit")});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 2, 14),
                new Object[]{"TST", "SBS", "TST",
                        'S', "ingotSteel",
                        'T', "ingotTungsten",
                        'B', "blockSteel"});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 15),
                new Object[]{"AAA", "AMA", "AAA",
                        'A', "ingotAluminium",
                        'M', new ItemStack(ModItems.parts, 1, 13)});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 16),
                new Object[]{"AAA", "AMA", "AAA",
                        'A', "ingotBronze",
                        'M', new ItemStack(ModItems.parts, 1, 13)});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 17),
                new Object[]{"AAA", "AMA", "AAA",
                        'A', "ingotSteel",
                        'M', new ItemStack(ModItems.parts, 1, 13)});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 18),
                new Object[]{"AAA", "AMA", "AAA",
                        'A', "ingotTitanium",
                        'M', new ItemStack(ModItems.parts, 1, 13)});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModItems.parts, 1, 19),
                new Object[]{"AAA", "AMA", "AAA",
                        'A', "ingotBrass",
                        'M', new ItemStack(ModItems.parts, 1, 13)});

        //Storage Blocks
        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 0),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotSilver",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 1),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotAluminium",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 2),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotTitanium",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 3),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "gemSapphire",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 4),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "gemRuby",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 5),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "gemGreenSapphire",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 6),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotChrome",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 7),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotElectrum",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 8),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotTungsten",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 9),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotLead",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 10),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotZinc",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 11),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotBrass",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 12),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotSteel",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 13),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotPlatinum",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 14),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotNickel",});

        CraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.storage, 1, 15),
                new Object[]{"AAA", "AAA", "AAA",
                        'A', "ingotInvar",});

        LogHelper.info("Shapped Recipes Added");
    }

    public static void addShaplessRecipes() {
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 4), "blockSilver");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 5), "blockAluminium");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 6), "blockTitanium");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 1), "blockSapphire");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 0), "blockRuby");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.gems, 9, 2), "blockGreenSapphire");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 7), "blockChrome");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 8), "blockElectrum");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 9), "blockTungsten");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 10), "blockLead");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 11), "blockZinc");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 12), "blockBrass");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 13), "blockSteel");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 14), "blockPlatinum");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 15), "blockNickel");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.ingots, 9, 16), "blockInvar");
        CraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.rockCutter, 1, 27), Items.apple);


        LogHelper.info("Shapless Recipes Added");
    }

    public static void addSmeltingRecipes() {
        GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 27), new ItemStack(Items.iron_ingot), 1F);
        GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 23), new ItemStack(Items.gold_ingot), 1F);
        GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 14), IC2Items.getItem("copperIngot"), 1F);
        GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 51), IC2Items.getItem("tinIngot"), 1F);
        GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 7), IC2Items.getItem("bronzeIngot"), 1F);
        GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 29), IC2Items.getItem("leadIngot"), 1F);
        GameRegistry.addSmelting(new ItemStack(ModItems.dusts, 1, 45), IC2Items.getItem("silverIngot"), 1F);

        LogHelper.info("Smelting Recipes Added");
    }

    public static void addMachineRecipes() {
        TechRebornAPI.registerCentrifugeRecipe(new CentrifugeRecipie(Items.apple, 4, Items.beef, Items.baked_potato, null, null, 120, 4));
        TechRebornAPI.registerCentrifugeRecipe(new CentrifugeRecipie(Items.nether_star, 1, Items.diamond, Items.emerald, Items.bed, Items.cake, 500, 8));
        TechRebornAPI.addRollingMachinceRecipe(new ItemStack(Blocks.furnace, 4), "ccc", "c c", "ccc", 'c', Blocks.cobblestone);
        LogHelper.info("Machine Recipes Added");
    }

}
