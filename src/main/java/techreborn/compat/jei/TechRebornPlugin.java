package techreborn.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import net.minecraftforge.fml.common.Loader;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeCategory;
import techreborn.compat.jei.fusionReactor.FusionReactorRecipeHandler;
import techreborn.lib.ModInfo;

import java.util.ArrayList;
import java.util.List;


@mezz.jei.api.JEIPlugin
public class TechRebornPlugin implements IModPlugin {
    @Override
    public boolean isModLoaded() {
        return Loader.isModLoaded(ModInfo.MOD_ID);
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategories(
                new FusionReactorRecipeCategory()
        );
        registry.addRecipeHandlers(
                new FusionReactorRecipeHandler()
        );
        List<Object> recipes = new ArrayList<>();
        recipes.addAll(FusionReactorRecipeHelper.reactorRecipes);
        registry.addRecipes(recipes);
    }
}
