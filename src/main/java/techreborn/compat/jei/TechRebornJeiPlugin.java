package techreborn.compat.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import techreborn.Core;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.api.recipe.machines.ImplosionCompressorRecipe;
import techreborn.client.container.ContainerAlloyFurnace;
import techreborn.client.container.ContainerAlloySmelter;
import techreborn.client.container.ContainerAssemblingMachine;
import techreborn.client.container.ContainerBlastFurnace;
import techreborn.client.container.ContainerCentrifuge;
import techreborn.client.container.ContainerChemicalReactor;
import techreborn.client.container.ContainerExtractor;
import techreborn.client.container.ContainerFusionReactor;
import techreborn.client.container.ContainerGrinder;
import techreborn.client.container.ContainerImplosionCompressor;
import techreborn.client.container.ContainerIndustrialElectrolyzer;
import techreborn.client.container.ContainerIndustrialGrinder;
import techreborn.client.container.ContainerIndustrialSawmill;
import techreborn.client.container.ContainerRollingMachine;
import techreborn.client.container.ContainerVacuumFreezer;
import techreborn.client.gui.GuiAlloyFurnace;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.client.gui.GuiChemicalReactor;
import techreborn.client.gui.GuiExtractor;
import techreborn.client.gui.GuiFusionReactor;
import techreborn.client.gui.GuiGrinder;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.client.gui.GuiIndustrialGrinder;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.client.gui.GuiRollingMachine;
import techreborn.client.gui.GuiVacuumFreezer;
import techreborn.compat.jei.alloySmelter.AlloySmelterRecipeCategory;
import techreborn.compat.jei.alloySmelter.AlloySmelterRecipeHandler;
import techreborn.compat.jei.assemblingMachine.AssemblingMachineRecipeCategory;
import techreborn.compat.jei.assemblingMachine.AssemblingMachineRecipeHandler;
import techreborn.compat.jei.blastFurnace.BlastFurnaceRecipeCategory;
import techreborn.compat.jei.blastFurnace.BlastFurnaceRecipeHandler;
import techreborn.compat.jei.centrifuge.CentrifugeRecipeCategory;
import techreborn.compat.jei.centrifuge.CentrifugeRecipeHandler;
import techreborn.compat.jei.chemicalReactor.ChemicalReactorRecipeCategory;
import techreborn.compat.jei.chemicalReactor.ChemicalReactorRecipeHandler;
import techreborn.compat.jei.extractor.ExtractorRecipeCategory;
import techreborn.compat.jei.extractor.ExtractorRecipeHandler;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeCategory;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeHandler;
import techreborn.compat.jei.grinder.GrinderRecipeCategory;
import techreborn.compat.jei.grinder.GrinderRecipeHandler;
import techreborn.compat.jei.implosionCompressor.ImplosionCompressorRecipeCategory;
import techreborn.compat.jei.implosionCompressor.ImplosionCompressorRecipeHandler;
import techreborn.compat.jei.industrialElectrolyzer.IndustrialElectrolyzerRecipeCategory;
import techreborn.compat.jei.industrialElectrolyzer.IndustrialElectrolyzerRecipeHandler;
import techreborn.compat.jei.industrialGrinder.IndustrialGrinderRecipeCategory;
import techreborn.compat.jei.industrialGrinder.IndustrialGrinderRecipeHandler;
import techreborn.compat.jei.industrialSawmill.IndustrialSawmillRecipeCategory;
import techreborn.compat.jei.industrialSawmill.IndustrialSawmillRecipeHandler;
import techreborn.compat.jei.rollingMachine.RollingMachineRecipeCategory;
import techreborn.compat.jei.rollingMachine.RollingMachineRecipeHandler;
import techreborn.compat.jei.rollingMachine.RollingMachineRecipeMaker;
import techreborn.compat.jei.vacuumFreezer.VacuumFreezerRecipeCategory;
import techreborn.compat.jei.vacuumFreezer.VacuumFreezerRecipeHandler;

@mezz.jei.api.JEIPlugin
public class TechRebornJeiPlugin extends BlankModPlugin {
    @Override
    public void register(@Nonnull IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(
                new AlloySmelterRecipeCategory(guiHelper),
                new AssemblingMachineRecipeCategory(guiHelper),
                new BlastFurnaceRecipeCategory(guiHelper),
                new CentrifugeRecipeCategory(guiHelper),
                new ChemicalReactorRecipeCategory(guiHelper),
                new FusionReactorRecipeCategory(guiHelper),
                new IndustrialGrinderRecipeCategory(guiHelper),
                new ImplosionCompressorRecipeCategory(guiHelper),
                new IndustrialElectrolyzerRecipeCategory(guiHelper),
                new IndustrialSawmillRecipeCategory(guiHelper),
                new RollingMachineRecipeCategory(guiHelper),
                new VacuumFreezerRecipeCategory(guiHelper),
                new GrinderRecipeCategory(guiHelper),
                new ExtractorRecipeCategory(guiHelper)
        );

        registry.addRecipeHandlers(
                new AlloySmelterRecipeHandler(jeiHelpers),
                new AssemblingMachineRecipeHandler(jeiHelpers),
                new BlastFurnaceRecipeHandler(jeiHelpers),
                new CentrifugeRecipeHandler(jeiHelpers),
                new ChemicalReactorRecipeHandler(jeiHelpers),
                new FusionReactorRecipeHandler(),
                new IndustrialGrinderRecipeHandler(jeiHelpers),
                new ImplosionCompressorRecipeHandler(jeiHelpers),
                new IndustrialElectrolyzerRecipeHandler(jeiHelpers),
                new IndustrialSawmillRecipeHandler(jeiHelpers),
                new RollingMachineRecipeHandler(),
                new VacuumFreezerRecipeHandler(jeiHelpers),
                new GrinderRecipeHandler(jeiHelpers),
                new ExtractorRecipeHandler(jeiHelpers)
        );

        registry.addRecipes(RecipeHandler.recipeList);
        registry.addRecipes(FusionReactorRecipeHelper.reactorRecipes);

        try {
            registry.addRecipes(RollingMachineRecipeMaker.getRecipes());
        } catch (RuntimeException e) {
            Core.logHelper.error("Could not register rolling machine recipes. JEI may have changed its internal recipe wrapper locations.");
            e.printStackTrace();
        }

        if (mezz.jei.config.Config.isDebugModeEnabled()) {
            addDebugRecipes(registry);
        }

        registry.addRecipeClickArea(GuiAlloyFurnace.class, 80, 35, 26, 20, RecipeCategoryUids.ALLOY_SMELTER, VanillaRecipeCategoryUid.FUEL);
        registry.addRecipeClickArea(GuiAlloySmelter.class, 80, 35, 26, 20, RecipeCategoryUids.ALLOY_SMELTER);
        registry.addRecipeClickArea(GuiAssemblingMachine.class, 85, 34, 24, 20, RecipeCategoryUids.ASSEMBLING_MACHINE);
        registry.addRecipeClickArea(GuiBlastFurnace.class, 63, 36, 24, 15, RecipeCategoryUids.BLAST_FURNACE);
        registry.addRecipeClickArea(GuiCentrifuge.class, 98, 37, 9, 12, RecipeCategoryUids.CENTRIFUGE);
        registry.addRecipeClickArea(GuiCentrifuge.class, 68, 37, 9, 12, RecipeCategoryUids.CENTRIFUGE);
        registry.addRecipeClickArea(GuiCentrifuge.class, 83, 23, 12, 9, RecipeCategoryUids.CENTRIFUGE);
        registry.addRecipeClickArea(GuiCentrifuge.class, 83, 53, 12, 9, RecipeCategoryUids.CENTRIFUGE);
        registry.addRecipeClickArea(GuiChemicalReactor.class, 73, 39, 32, 12, RecipeCategoryUids.CHEMICAL_REACTOR);
        registry.addRecipeClickArea(GuiFusionReactor.class, 111, 34, 27, 19, RecipeCategoryUids.FUSION_REACTOR);
        registry.addRecipeClickArea(GuiIndustrialGrinder.class, 50, 35, 25, 16, RecipeCategoryUids.INDUSTRIAL_GRINDER);
        registry.addRecipeClickArea(GuiImplosionCompressor.class, 60, 37, 24, 15, RecipeCategoryUids.IMPLOSION_COMPRESSOR);
        registry.addRecipeClickArea(GuiIndustrialElectrolyzer.class, 72, 37, 33, 14, RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER);
        registry.addRecipeClickArea(GuiIndustrialSawmill.class, 55, 36, 24, 16, RecipeCategoryUids.INDUSTRIAL_SAWMILL);
        registry.addRecipeClickArea(GuiRollingMachine.class, 89, 32, 26, 25, RecipeCategoryUids.ROLLING_MACHINE);
        registry.addRecipeClickArea(GuiVacuumFreezer.class, 78, 36, 24, 16, RecipeCategoryUids.VACUUM_FREEZER);
        registry.addRecipeClickArea(GuiGrinder.class, 78, 36, 24, 16, RecipeCategoryUids.GRINDER);
        registry.addRecipeClickArea(GuiExtractor.class, 78, 36, 24, 16, RecipeCategoryUids.EXTRACTOR);


        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloyFurnace.class, RecipeCategoryUids.ALLOY_SMELTER, 0, 2, 4, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloySmelter.class, RecipeCategoryUids.ALLOY_SMELTER, 0, 2, 8, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloyFurnace.class, VanillaRecipeCategoryUid.FUEL, 3, 1, 4, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAssemblingMachine.class, RecipeCategoryUids.ASSEMBLING_MACHINE, 0, 2, 8, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerBlastFurnace.class, RecipeCategoryUids.BLAST_FURNACE, 0, 2, 4, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerCentrifuge.class, RecipeCategoryUids.CENTRIFUGE, 0, 2, 11, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerChemicalReactor.class, RecipeCategoryUids.CHEMICAL_REACTOR, 0, 2, 8, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFusionReactor.class, RecipeCategoryUids.FUSION_REACTOR, 0, 2, 3, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerIndustrialGrinder.class, RecipeCategoryUids.GRINDER, 0, 2, 6, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerImplosionCompressor.class, RecipeCategoryUids.IMPLOSION_COMPRESSOR, 0, 2, 4, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerIndustrialElectrolyzer.class, RecipeCategoryUids.INDUSTRIAL_ELECTROLYZER, 0, 2, 7, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerIndustrialSawmill.class, RecipeCategoryUids.INDUSTRIAL_SAWMILL, 0, 2, 5, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerRollingMachine.class, RecipeCategoryUids.ROLLING_MACHINE, 0, 9, 11, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerVacuumFreezer.class, RecipeCategoryUids.VACUUM_FREEZER, 0, 1, 2, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerGrinder.class, RecipeCategoryUids.GRINDER, 0, 1, 2, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerExtractor.class, RecipeCategoryUids.EXTRACTOR, 0, 1, 2, 36);
    }

    private static void addDebugRecipes(IModRegistry registry) {
        ItemStack diamondBlock = new ItemStack(Blocks.diamond_block);
        ItemStack dirtBlock = new ItemStack(Blocks.dirt);
        List<Object> debugRecipes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int time = (int) Math.round(200 + Math.random() * 100);
            AssemblingMachineRecipe assemblingMachineRecipe = new AssemblingMachineRecipe(diamondBlock, diamondBlock, dirtBlock, time, 120);
            debugRecipes.add(assemblingMachineRecipe);
        }
        for (int i = 0; i < 10; i++) {
            int time = (int) Math.round(200 + Math.random() * 100);
            ImplosionCompressorRecipe recipe = new ImplosionCompressorRecipe(diamondBlock, diamondBlock, dirtBlock, dirtBlock, time, 120);
            debugRecipes.add(recipe);
        }
        registry.addRecipes(debugRecipes);
    }
}
