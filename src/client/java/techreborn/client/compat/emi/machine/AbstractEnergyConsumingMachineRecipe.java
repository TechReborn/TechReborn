package techreborn.client.compat.emi.machine;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.recipe.recipes.BlastFurnaceRecipe;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEnergyConsumingMachineRecipe<R extends RebornRecipe> implements EmiRecipe {
	protected final @Nullable Identifier id;
	protected final List<EmiIngredient> inputs;
	protected final List<EmiStack> outputs;
	private final int energy;
	private int heat = 0;
	private final int time;
	private FluidInstance fluidInstance = null;

	public AbstractEnergyConsumingMachineRecipe(RecipeEntry<R> entry) {
		R recipe = entry.value();
		this.id = entry.id();
		this.inputs = recipe.ingredients().stream().map(ing -> EmiIngredient.of(ing.ingredient(), ing.count()))
			.collect(Collectors.toList());
		this.outputs = recipe.outputs().stream().map(EmiStack::of).toList();
		this.time = recipe.time();
		this.energy = recipe.power();

		if (recipe instanceof BlastFurnaceRecipe) {
			this.heat = ((BlastFurnaceRecipe) recipe).getHeat();
		}

		if (recipe instanceof RebornFluidRecipe) {
			this.fluidInstance = ((RebornFluidRecipe) recipe).fluid();
			inputs.add(EmiStack.of(fluidInstance.fluid(), fluidInstance.getAmount().rawValue()));
		}
	}

	public int getEnergy() {
		return energy;
	}

	public int getHeat() {
		return heat;
	}

	public int getTime() {
		return time;
	}

	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return outputs;
	}

	@Override
	public @Nullable Identifier getId() {
		return id;
	}

	protected EmiIngredient getInput(int index) {
		if (index >= inputs.size()) {
			return EmiStack.EMPTY;
		}

		return inputs.get(index);
	}

	protected EmiStack getOutput(int index) {
		if (index >= outputs.size()) {
			return EmiStack.EMPTY;
		}

		return outputs.get(index);
	}
}
