package techreborn.compat.jei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.client.container.ContainerAlloyFurnace;
import techreborn.client.container.ContainerAlloySmelter;
import techreborn.client.container.ContainerAssemblingMachine;
import techreborn.client.container.ContainerBlastFurnace;
import techreborn.client.container.ContainerFusionReactor;
import techreborn.compat.jei.alloySmelter.AlloySmelterRecipeCategory;
import techreborn.compat.jei.alloySmelter.AlloySmelterRecipeHandler;
import techreborn.compat.jei.assemblingMachine.AssemblingMachineRecipeCategory;
import techreborn.compat.jei.assemblingMachine.AssemblingMachineRecipeHandler;
import techreborn.compat.jei.blastFurnace.BlastFurnaceRecipeCategory;
import techreborn.compat.jei.blastFurnace.BlastFurnaceRecipeHandler;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeCategory;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeHandler;

@mezz.jei.api.JEIPlugin
public class TechRebornJeiPlugin implements IModPlugin {
    public static IJeiHelpers jeiHelpers;

    @Override
    public boolean isModLoaded() {
        return true;
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
        TechRebornJeiPlugin.jeiHelpers = jeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

    }

    @Override
    public void register(IModRegistry registry) {
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(
                new AlloySmelterRecipeCategory(guiHelper),
                new AssemblingMachineRecipeCategory(guiHelper),
                new BlastFurnaceRecipeCategory(guiHelper),
                new FusionReactorRecipeCategory(guiHelper)
        );

        registry.addRecipeHandlers(
                new AlloySmelterRecipeHandler(),
                new AssemblingMachineRecipeHandler(),
                new BlastFurnaceRecipeHandler(),
                new FusionReactorRecipeHandler()
        );

        registry.addRecipes(RecipeHandler.recipeList);
        registry.addRecipes(FusionReactorRecipeHelper.reactorRecipes);

        if (mezz.jei.config.Config.isDebugModeEnabled()) {
            addDebugRecipes(registry);
        }

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloyFurnace.class, RecipeCategoryUids.ALLOY_SMELTER, 0, 2, 4, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloySmelter.class, RecipeCategoryUids.ALLOY_SMELTER, 0, 2, 8, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAlloyFurnace.class, VanillaRecipeCategoryUid.FUEL, 3, 1, 4, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerAssemblingMachine.class, RecipeCategoryUids.ASSEMBLING_MACHINE, 0, 2, 8, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerBlastFurnace.class, RecipeCategoryUids.BLAST_FURNACE, 0, 2, 4, 36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFusionReactor.class, RecipeCategoryUids.FUSION_REACTOR, 0, 2, 3, 36);
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

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
        registry.addRecipes(debugRecipes);
    }
}
