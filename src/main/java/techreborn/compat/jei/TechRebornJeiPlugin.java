package techreborn.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.client.container.ContainerFusionReactor;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeCategory;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeHandler;

@mezz.jei.api.JEIPlugin
public class TechRebornJeiPlugin implements IModPlugin {
    private IJeiHelpers jeiHelpers;

    @Override
    public boolean isModLoaded() {
        return true;
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

    }

    @Override
    public void register(IModRegistry registry) {
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(
                new FusionReactorRecipeCategory(guiHelper)
        );

        registry.addRecipeHandlers(
                new FusionReactorRecipeHandler()
        );

        registry.addRecipes(FusionReactorRecipeHelper.reactorRecipes);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(ContainerFusionReactor.class, TechRebornCategoryUids.FUSION_REACTOR, 0, 2, 3, 36);
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

    }
}
