/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.client.compat.rei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.recipe.recipes.BlastFurnaceRecipe;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MachineRecipeDisplay<R extends RebornRecipe> implements Display {
	public static final DisplaySerializer<MachineRecipeDisplay<?>> SERIALIZER = DisplaySerializer.of(
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("category").forGetter(d -> d.category.getIdentifier().toString()),
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(MachineRecipeDisplay::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(MachineRecipeDisplay::getOutputEntries),
			Identifier.CODEC.optionalFieldOf("location").forGetter(MachineRecipeDisplay::getDisplayLocation),
			Codec.INT.fieldOf("energy").forGetter(MachineRecipeDisplay::getEnergy),
			Codec.INT.fieldOf("heat").forGetter(MachineRecipeDisplay::getHeat),
			Codec.INT.fieldOf("time").forGetter(MachineRecipeDisplay::getTime),
			FluidInstance.CODEC.fieldOf("fluidInstance").forGetter(MachineRecipeDisplay::getFluidInstance)
		).apply(instance, MachineRecipeDisplay::new)),
		PacketCodec.tuple(
			PacketCodecs.STRING,
			d -> d.category.getIdentifier().toString(),
			EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
			MachineRecipeDisplay::getInputEntries,
			EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
			MachineRecipeDisplay::getOutputEntries,
			PacketCodecs.optional(Identifier.PACKET_CODEC),
			MachineRecipeDisplay::getDisplayLocation,
			PacketCodecs.INTEGER,
			MachineRecipeDisplay::getEnergy,
			PacketCodecs.INTEGER,
			MachineRecipeDisplay::getHeat,
			PacketCodecs.INTEGER,
			MachineRecipeDisplay::getTime,
			FluidInstance.PACKET_CODEC,
			MachineRecipeDisplay::getFluidInstance,
			MachineRecipeDisplay::new
		)
	);

	private final CategoryIdentifier<?> category;
	private final List<EntryIngredient> inputs;
	private final List<EntryIngredient> outputs;
	private final Optional<Identifier> location;
	private final int energy;
	private int heat = 0;
	private final int time;
	private FluidInstance fluidInstance = null;

	public MachineRecipeDisplay(
		String category,
		List<EntryIngredient> inputs,
		List<EntryIngredient> outputs,
		Optional<Identifier> location,
		int energy,
		int heat,
		int time,
		FluidInstance fluidInstance
	) {
		this.category = CategoryIdentifier.of(category);
		this.inputs = inputs;
		this.outputs = outputs;
		this.location = location;
		this.energy = energy;
		this.heat = heat;
		this.time = time;
		this.fluidInstance = fluidInstance;
	}

	public MachineRecipeDisplay(RecipeEntry<R> entry) {
		R recipe = entry.value();
		this.category = CategoryIdentifier.of(Objects.requireNonNull(Registries.RECIPE_TYPE.getId(recipe.getType())));
		this.inputs = CollectionUtils.map(recipe.ingredients(), ing -> EntryIngredients.ofItemStacks(ing.getPreviewStacks()));
		this.outputs = recipe.outputs().stream().map(EntryIngredients::of).collect(Collectors.toList());
		this.location = Optional.of(entry.id().getValue());
		this.time = recipe.time();
		this.energy = recipe.power();
		if (recipe instanceof BlastFurnaceRecipe) {
			this.heat = ((BlastFurnaceRecipe) recipe).getHeat();
		}
		if (recipe instanceof RebornFluidRecipe) {
			this.fluidInstance = ((RebornFluidRecipe) recipe).fluid();
			inputs.add(EntryIngredients.of(fluidInstance.fluid(), fluidInstance.getAmount().getRawValue()));
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
	public Optional<Identifier> getDisplayLocation() {
		return location;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return inputs;
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return outputs;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return category;
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}
}
