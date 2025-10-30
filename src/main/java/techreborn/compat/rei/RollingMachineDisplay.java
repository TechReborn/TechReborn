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

package techreborn.compat.rei;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.displays.crafting.CraftingDisplay;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import reborncore.common.crafting.RebornRecipe;
import techreborn.init.ModRecipes;
import techreborn.recipe.recipes.RollingMachineRecipe;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RollingMachineDisplay implements CraftingDisplay {
	public static final DisplaySerializer<RollingMachineDisplay> SERIALIZER = DisplaySerializer.of(
		RecordCodecBuilder.mapCodec(instance -> instance.group(
			EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(RollingMachineDisplay::getInputEntries),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(RollingMachineDisplay::getOutputEntries),
			ResourceLocation.CODEC.optionalFieldOf("location").forGetter(RollingMachineDisplay::getDisplayLocation),
			Codec.INT.fieldOf("width").forGetter(RollingMachineDisplay::getWidth),
			Codec.INT.fieldOf("height").forGetter(RollingMachineDisplay::getHeight),
			Codec.INT.fieldOf("energy").forGetter(RollingMachineDisplay::getEnergy),
			Codec.INT.fieldOf("time").forGetter(RollingMachineDisplay::getTime)
		).apply(instance, RollingMachineDisplay::new)),
		StreamCodec.composite(
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), RollingMachineDisplay::getInputEntries,
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), RollingMachineDisplay::getOutputEntries,
			ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), RollingMachineDisplay::getDisplayLocation,
			ByteBufCodecs.INT, RollingMachineDisplay::getWidth,
			ByteBufCodecs.INT, RollingMachineDisplay::getHeight,
			ByteBufCodecs.INT, RollingMachineDisplay::getEnergy,
			ByteBufCodecs.INT, RollingMachineDisplay::getTime,
			RollingMachineDisplay::new
		)
	);

	private final List<EntryIngredient> inputs;
	private final List<EntryIngredient> outputs;
	private final Optional<ResourceLocation> location;
	private final int width;
	private final int height;
	private final int energy;
	private final int time;

	public RollingMachineDisplay(
		List<EntryIngredient> inputs,
		List<EntryIngredient> outputs,
		Optional<ResourceLocation> location,
		int width,
		int height,
		int energy,
		int time
	) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.location = location;
		this.width = width;
		this.height = height;
		this.energy = energy;
		this.time = time;
	}

	public RollingMachineDisplay(RecipeHolder<RebornRecipe> entry) {
		RollingMachineRecipe recipe = (RollingMachineRecipe) entry.value();
		this.energy = recipe.power();
		this.time = recipe.time();
		ShapedRecipe shapedRecipe = recipe.getShapedRecipe();
		this.inputs = CollectionUtils.map(
			shapedRecipe.getIngredients(),
			opt -> opt.map(EntryIngredients::ofIngredient).orElse(EntryIngredient.empty())
		);
		this.outputs = List.of(EntryIngredients.of(shapedRecipe.assemble(null, null)));
		this.location = Optional.of(entry.id().location());
		this.width = shapedRecipe.getWidth();
		this.height = shapedRecipe.getHeight();
	}

	public int getEnergy() {
		return energy;
	}

	public int getTime() {
		return time;
	}

	@Override
	public boolean isShapeless() {
		return false;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Optional<ResourceLocation> getDisplayLocation() {
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
		return CategoryIdentifier.of(Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getKey(ModRecipes.ROLLING_MACHINE)));
	}

	@Override
	public DisplaySerializer<? extends Display> getSerializer() {
		return SERIALIZER;
	}
}
