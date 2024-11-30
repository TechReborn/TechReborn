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

package techreborn.client.compat.rei.fluidreplicator;

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
import reborncore.common.fluid.container.FluidInstance;
import techreborn.recipe.recipes.FluidReplicatorRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author drcrazy
 */
public class FluidReplicatorRecipeDisplay implements Display {
	public static final DisplaySerializer<FluidReplicatorRecipeDisplay> SERIALIZER = DisplaySerializer.of(
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("category").forGetter(d -> d.category.getIdentifier().toString()),
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(FluidReplicatorRecipeDisplay::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(FluidReplicatorRecipeDisplay::getOutputEntries),
			Identifier.CODEC.optionalFieldOf("location").forGetter(FluidReplicatorRecipeDisplay::getDisplayLocation),
			FluidInstance.CODEC.fieldOf("fluidInstance").forGetter(FluidReplicatorRecipeDisplay::getFluidInstance),
			Codec.INT.fieldOf("energy").forGetter(FluidReplicatorRecipeDisplay::getEnergy),
			Codec.INT.fieldOf("time").forGetter(FluidReplicatorRecipeDisplay::getTime)
		).apply(instance, FluidReplicatorRecipeDisplay::new)),
		PacketCodec.tuple(
			PacketCodecs.STRING,
			d -> d.category.getIdentifier().toString(),
			EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
			FluidReplicatorRecipeDisplay::getInputEntries,
			EntryIngredient.streamCodec().collect(PacketCodecs.toList()),
			FluidReplicatorRecipeDisplay::getOutputEntries,
			PacketCodecs.optional(Identifier.PACKET_CODEC),
			FluidReplicatorRecipeDisplay::getDisplayLocation,
			FluidInstance.PACKET_CODEC,
			FluidReplicatorRecipeDisplay::getFluidInstance,
			PacketCodecs.INTEGER,
			FluidReplicatorRecipeDisplay::getEnergy,
			PacketCodecs.INTEGER,
			FluidReplicatorRecipeDisplay::getTime,
			FluidReplicatorRecipeDisplay::new
		)
	);

	private final CategoryIdentifier<?> category;
	private final Optional<Identifier> location;
	private final List<EntryIngredient> inputs;
	private final List<EntryIngredient> outputs;
	private final FluidInstance fluidInstance;
	private final int energy;
	private final int time;

	public FluidReplicatorRecipeDisplay(
		String category,
		List<EntryIngredient> inputs,
		List<EntryIngredient> outputs,
		Optional<Identifier> location,
		FluidInstance fluidInstance,
		int energy,
		int time
	) {
		this.category = CategoryIdentifier.of(category);
		this.inputs = inputs;
		this.outputs = outputs;
		this.location = location;
		this.fluidInstance = fluidInstance;
		this.energy = energy;
		this.time = time;
	}

	public FluidReplicatorRecipeDisplay(RecipeEntry<FluidReplicatorRecipe> entry) {
		FluidReplicatorRecipe recipe = entry.value();
		this.category = CategoryIdentifier.of(Objects.requireNonNull(Registries.RECIPE_TYPE.getId(recipe.getType())));
		this.location = Optional.of(entry.id().getValue());
		this.inputs = CollectionUtils.map(recipe.ingredients(), ing -> EntryIngredients.ofItemStacks(ing.getPreviewStacks()));
		this.fluidInstance = recipe.fluid();
		this.outputs = fluidInstance == null ? Collections.emptyList() : Collections.singletonList(EntryIngredients.of(fluidInstance.fluid(), fluidInstance.getAmount().getRawValue()));
		this.energy = recipe.power();
		this.time = recipe.time();
	}

	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}

	public int getEnergy() {
		return energy;
	}

	public int getTime() {
		return time;
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
	public Optional<Identifier> getDisplayLocation() {
		return location;
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}
}
