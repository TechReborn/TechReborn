package techreborn.compat.libcd;

import io.github.cottonmc.libcd.tweaker.RecipeParser;
import io.github.cottonmc.libcd.tweaker.RecipeTweaker;
import io.github.cottonmc.libcd.tweaker.Tweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.TechReborn;
import techreborn.init.ModRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TRTweaker {
    public static final TRTweaker INSTANCE = new TRTweaker();
    private RecipeTweaker tweaker = RecipeTweaker.INSTANCE;

    public void add(RebornRecipeType type, Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            List<Ingredient> ingredients = new ArrayList<>();
            for (Object input : inputs) {
                //TODO: write a TR-specifc ingredient parser, so we don't have to do the wrapped-ingredient hack thing
                ingredients.add(RecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new TweakedRebornRecipe(type, id, ingredients, Arrays.asList(outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn " + type.getName().getPath().replace('_', ' ') + " recipe - " + e.getMessage());
        }
    }

    public void addAlloySmelter(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.ALLOY_SMELTER, inputs, outputs, power, time);
    }

    public void addAssemblingMachine(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.ASSEMBLING_MACHINE, inputs, outputs, power, time);
    }

    public void addBlastFurnace(Object[] inputs, ItemStack[] outputs, int power, int time, int heat) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            List<Ingredient> ingredients = new ArrayList<>();
            for (Object input : inputs) {
                ingredients.add(RecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new TweakedBlastFurnaceRecipe(ModRecipes.BLAST_FURNACE, id, ingredients, Arrays.asList(outputs), power, time, heat));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn blast furnace recipe - " + e.getMessage());
        }
    }

    public void addCentrifuge(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.CENTRIFUGE, inputs, outputs, power, time);
    }

    public void addChemicalReactor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.CHEMICAL_REACTOR, inputs, outputs, power, time);
    }

    public void addCompressor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.COMPRESSOR, inputs, outputs, power, time);
    }

    public void addDistillationTower(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.DISTILLATION_TOWER, inputs, outputs, power, time);
    }

    public void addExtractor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.EXTRACTOR, inputs, outputs, power, time);
    }

    public void addGrinder(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.GRINDER, inputs, outputs, power, time);
    }

    public void addImplosionCompressor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.IMPLOSION_COMPRESSOR, inputs, outputs, power, time);
    }

    public void addElectrolyzer(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.INDUSTRIAL_ELECTROLYZER, inputs, outputs, power, time);
    }

    public void addIndustrialGrinder(Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            List<Ingredient> ingredients = new ArrayList<>();
            for (Object input : inputs) {
                ingredients.add(RecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new TweakedIndustrialGrinderRecipe(ModRecipes.INDUSTRIAL_GRINDER, id, ingredients, Arrays.asList(outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn industrial grinder recipe - " + e.getMessage());
        }
    }

    public void addIndustrialSawmill(Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            List<Ingredient> ingredients = new ArrayList<>();
            for (Object input : inputs) {
                ingredients.add(RecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new TweakedIndustrialSawmillRecipe(ModRecipes.INDUSTRIAL_SAWMILL, id, ingredients, Arrays.asList(outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn industrial sawmill recipe - " + e.getMessage());
        }
    }

    public void addRecycler(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.RECYCLER, inputs, outputs, power, time);
    }

    public void addScrapbox(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.SCRAPBOX, inputs, outputs, power, time);
    }

    public void addVacuumFreezer(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.VACUUM_FREEZER, inputs, outputs, power, time);
    }

    public void addFluidReplicator(Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            List<Ingredient> ingredients = new ArrayList<>();
            for (Object input : inputs) {
                ingredients.add(RecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new TweakedIndustrialSawmillRecipe(ModRecipes.FLUID_REPLICATOR, id, ingredients, Arrays.asList(outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn fluid replicator recipe - " + e.getMessage());
        }
    }

    public void addFusionReactor(Object[] inputs, ItemStack[] outputs, int power, int time, int startE, int minSize) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            List<Ingredient> ingredients = new ArrayList<>();
            for (Object input : inputs) {
                ingredients.add(RecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new TweakedFusionReactorRecipe(ModRecipes.FUSION_REACTOR, id, ingredients, Arrays.asList(outputs), power, time, startE, minSize));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn fusion reactor recipe - " + e.getMessage());
        }
    }

    //TODO: rolling machine, in all the ways you can do shaped recipes

    public void addSolidCanningMachine(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.SOLID_CANNING_MACHINE, inputs, outputs, power, time);
    }

    public void addWireMill(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.WIRE_MILL, inputs, outputs, power, time);
    }

    public static void init() {
        Tweaker.addAssistant("TRTweaker", TRTweaker.INSTANCE);
    }
}
