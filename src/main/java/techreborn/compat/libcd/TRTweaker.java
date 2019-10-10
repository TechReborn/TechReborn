package techreborn.compat.libcd;

import io.github.cottonmc.libcd.tweaker.RecipeParser;
import io.github.cottonmc.libcd.tweaker.RecipeTweaker;
import io.github.cottonmc.libcd.tweaker.Tweaker;
import io.github.cottonmc.libcd.tweaker.TweakerUtils;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.TechReborn;
import techreborn.api.generator.EFluidGenerator;
import techreborn.api.generator.FluidGeneratorRecipe;
import techreborn.api.generator.GeneratorRecipeHelper;
import techreborn.api.recipe.recipes.*;
import techreborn.init.ModRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class TRTweaker implements Tweaker {
    public static final TRTweaker INSTANCE = new TRTweaker();
    private RecipeTweaker tweaker = RecipeTweaker.INSTANCE;

    List<FluidGeneratorRecipe> added = new ArrayList<>();
    List<FluidGeneratorRecipe> toAdd = new ArrayList<>();

    @Override
    public void prepareReload(ResourceManager resourceManager) {
        for (FluidGeneratorRecipe recipe : added) {
            GeneratorRecipeHelper.removeFluidRecipe(recipe.getGeneratorType(), recipe.getFluid());
        }
        added.clear();
    }

    @Override
    public void applyReload(ResourceManager resourceManager, Executor executor) {
        for (FluidGeneratorRecipe recipe : toAdd) {
            if (GeneratorRecipeHelper.getFluidRecipesForGenerator(recipe.getGeneratorType()).addRecipe(recipe)) {
                added.add(recipe);
            } else {
                TechReborn.LOGGER.error("Could not add recipe to TechReborn generator " + recipe.getGeneratorType().getRecipeID()
                        + ": a recipe for fluid " + Registry.FLUID.getId(recipe.getFluid()) + " already exists");
            }
        }
    }

    @Override
    public String getApplyMessage() {
        int recipeCount = added.size();
        return recipeCount + " TechReborn fluid generator " + (recipeCount == 1? "recipe" : "recipes");
    }

    /**
     * Register a generic TechReborn recipe.
     * @param type The type of RebornRecipe to add.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void add(RebornRecipeType type, Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new RebornRecipe(type, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn " + type.getName().getPath().replace('_', ' ') + " recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to smelt in an alloy smelter.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to smelt for.
     */
    public void addAlloySmelter(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.ALLOY_SMELTER, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to assemble in an assembling machine.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addAssemblingMachine(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.ASSEMBLING_MACHINE, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to smelt in a TechReborn blast furnace.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to smelt for.
     * @param heat How hot the blast furnace needs to be.
     */
    public void addBlastFurnace(Object[] inputs, ItemStack[] outputs, int power, int time, int heat) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new BlastFurnaceRecipe(ModRecipes.BLAST_FURNACE, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time, heat));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn blast furnace recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in a centrifuge.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addCentrifuge(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.CENTRIFUGE, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in a chemical reactor.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addChemicalReactor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.CHEMICAL_REACTOR, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to compress in a compressor.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addCompressor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.COMPRESSOR, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to distil in a distillation tower.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addDistillationTower(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.DISTILLATION_TOWER, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to extract in an extractor.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addExtractor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.EXTRACTOR, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in a grinder.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addGrinder(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.GRINDER, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in an implosion compressor.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addImplosionCompressor(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.IMPLOSION_COMPRESSOR, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in an electrolyzer.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addElectrolyzer(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.INDUSTRIAL_ELECTROLYZER, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in an industrial grinder.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addIndustrialGrinder(Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new IndustrialGrinderRecipe(ModRecipes.INDUSTRIAL_GRINDER, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn industrial grinder recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in an industrial grinder.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     * @param fluid The fluid required for the operation, including an amount.
     */
    public void addIndustrialGrinder(Object[] inputs, ItemStack[] outputs, int power, int time, String fluid) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            FluidInstance fluidInst = TRRecipeParser.parseFluid(fluid);
            tweaker.addRecipe(new IndustrialGrinderRecipe(ModRecipes.INDUSTRIAL_GRINDER, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time, fluidInst));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn industrial grinder recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in an industria sawmilll.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addIndustrialSawmill(Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new IndustrialSawmillRecipe(ModRecipes.INDUSTRIAL_SAWMILL, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn industrial sawmill recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in an industrial sawmill.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     * @param fluid The fluid required for this operation, including an amount.
     */
    public void addIndustrialSawmill(Object[] inputs, ItemStack[] outputs, int power, int time, String fluid) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            FluidInstance fluidInst = TRRecipeParser.parseFluid(fluid);
            tweaker.addRecipe(new IndustrialSawmillRecipe(ModRecipes.INDUSTRIAL_SAWMILL, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time, fluidInst));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn industrial sawmill recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in a recycler.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addRecycler(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.RECYCLER, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to get from a scrapbox.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addScrapbox(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.SCRAPBOX, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in a vacuum freezer.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addVacuumFreezer(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.VACUUM_FREEZER, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in a fluid replicator.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addFluidReplicator(Object[] inputs, ItemStack[] outputs, int power, int time) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new FluidReplicatorRecipe(ModRecipes.FLUID_REPLICATOR, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn fluid replicator recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in a fluid replicator.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     * @param fluid The fluid required for this operation, including an amount.
     */
    public void addFluidReplicator(Object[] inputs, ItemStack[] outputs, int power, int time, String fluid) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            FluidInstance fluidInst = TRRecipeParser.parseFluid(fluid);
            tweaker.addRecipe(new FluidReplicatorRecipe(ModRecipes.FLUID_REPLICATOR, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time, fluidInst));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn fluid replicator recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in a fusion reactor.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addFusionReactor(Object[] inputs, ItemStack[] outputs, int power, int time, int startE, int minSize) {
        try {
            Identifier id = tweaker.getRecipeId(outputs[0]);
            DefaultedList<RebornIngredient> ingredients = DefaultedList.of();
            for (Object input : inputs) {
                ingredients.add(TRRecipeParser.processIngredient(input));
            }
            tweaker.addRecipe(new FusionReactorRecipe(ModRecipes.FUSION_REACTOR, id, ingredients, DefaultedList.copyOf(ItemStack.EMPTY, outputs), power, time, startE, minSize));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn fusion reactor recipe - " + e.getMessage());
        }
    }

    /**
     * Register a rolling machine recipe from a 2D array of inputs, like a standard CraftTweaker recipe.
     * @param inputs the 2D array (array of arrays) of inputs to use.
     * @param output The output of the recipe.
     */
    public void addRollingMachine(Object[][] inputs, ItemStack output) {
        try {
            Object[] processed = RecipeParser.processGrid(inputs);
            int width = inputs[0].length;
            int height = inputs.length;
            addRollingMachine(processed, output, width, height);
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn rolling machine recipe - " + e.getMessage());
        }
    }

    /**
     * Register a rolling machine recipe from a 1D array of inputs.
     * @param inputs The input item or tag ids required in order: left to right, top to bottom.
     * @param output The output of the recipe.
     * @param width How many rows the recipe needs.
     * @param height How many columns the recipe needs.
     */
    public void addRollingMachine(Object[] inputs, ItemStack output, int width, int height){
        Identifier recipeId = tweaker.getRecipeId(output);
        try {
            DefaultedList<Ingredient> ingredients = DefaultedList.of();
            for (int i = 0; i < Math.min(inputs.length, width * height); i++) {
                Object id = inputs[i];
                if (id.equals("")) continue;
                ingredients.add(i, RecipeParser.processIngredient(id));
            }
            tweaker.addRecipe(new RollingMachineRecipe(ModRecipes.ROLLING_MACHINE, recipeId, new ShapedRecipe(recipeId, "", width, height, ingredients, output)));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn rolling machine recipe - " + e.getMessage());
        }
    }

    /**
     * Register a rolling machine recipe from a pattern and dictionary.
     * @param pattern A crafting pattern like one you'd find in a vanilla recipe JSON.
     * @param dictionary A map of single characters to item or tag ids.
     * @param output The output of the recipe.
     */
    public void addRollingMachine(String[] pattern, Map<String, Object> dictionary, ItemStack output) {
        Identifier recipeId = tweaker.getRecipeId(output);
        try {
            pattern = RecipeParser.processPattern(pattern);
            Map<String, Ingredient> map = RecipeParser.processDictionary(dictionary);
            int x = pattern[0].length();
            int y = pattern.length;
            DefaultedList<Ingredient> ingredients = RecipeParser.getIngredients(pattern, map, x, y);
            tweaker.addRecipe(new RollingMachineRecipe(ModRecipes.ROLLING_MACHINE, recipeId, new ShapedRecipe(recipeId, "", x, y, ingredients, output)));
        } catch (Exception e) {
            TechReborn.LOGGER.error("Error parsing TechReborn rolling machine recipe - " + e.getMessage());
        }
    }

    /**
     * Register a recipe to process in a solid canning machine.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addSolidCanningMachine(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.SOLID_CANNING_MACHINE, inputs, outputs, power, time);
    }

    /**
     * Register a recipe to process in a wire mill.
     * @param inputs The input ingredients for the recipe.
     * @param outputs The outputs of the recipe.
     * @param power How much power is needed for the recipe.
     * @param time How many ticks (1/20 of a second) to process for.
     */
    public void addWireMill(Object[] inputs, ItemStack[] outputs, int power, int time) {
        add(ModRecipes.WIRE_MILL, inputs, outputs, power, time);
    }

    /**
     * Register a fluid to process in a fluid generator.
     * @param generator The type of generator: thermal, gas, diesel, semifluid, or plasma.
     * @param fluid The fluid to add a recipe for.
     * @param euPerMB How much EU should be generated per millibucket of fluid.
     */
    public void addFluidGenerator(String generator, String fluid, int euPerMB) {
        EFluidGenerator type;
        switch(generator.toLowerCase()) {
            case "thermal":
            case "techreborn.thermalgenerator":
                type = EFluidGenerator.THERMAL;
                break;
            case "gas":
            case "techreborn.gasgenerator":
                type = EFluidGenerator.GAS;
                break;
            case "diesel":
            case "techreborn.dieselgenerator":
                type = EFluidGenerator.DIESEL;
                break;
            case "semifluid":
            case "techreborn.semifluidgenerator":
                type = EFluidGenerator.SEMIFLUID;
                break;
            case "plasma":
            case "techreborn.plasmagenerator":
                type = EFluidGenerator.PLASMA;
                break;
            default:
                TechReborn.LOGGER.error("Error parsing TechReborn fluid generator recipe - could not find generator: " + generator);
                return;
        }
        Fluid parsedFluid = TweakerUtils.INSTANCE.getFluid(fluid);
        if (parsedFluid == Fluids.EMPTY) {
            TechReborn.LOGGER.error("Error parsing TechReborn fluid generator recipe - could not find fluid: " + fluid);
            return;
        }
        toAdd.add(new FluidGeneratorRecipe(parsedFluid, euPerMB, type));
    }

    public static void init() {
        Tweaker.addTweaker("TRTweaker", TRTweaker.INSTANCE);
    }
}
