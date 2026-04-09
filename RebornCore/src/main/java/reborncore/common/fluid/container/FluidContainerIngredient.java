/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TeamReborn
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

package reborncore.common.fluid.container;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.material.Fluid;

import java.util.stream.Stream;

public class FluidContainerIngredient implements CustomIngredient {
	public static final CustomIngredientSerializer<FluidContainerIngredient> SERIALIZER = new Serializer();

	private static final long DROPLETS_PER_MB = FluidConstants.BUCKET / 1000;

	private final Holder<Fluid> fluid;
	private final long amountMb;

	public FluidContainerIngredient(Holder<Fluid> fluid, long amountMb) {
		this.fluid = fluid;
		this.amountMb = amountMb;
	}

	public Holder<Fluid> getFluid() {
		return fluid;
	}

	public long getAmountMb() {
		return amountMb;
	}

	private long getAmountDroplets() {
		return amountMb * DROPLETS_PER_MB;
	}

	/**
	 * Tests whether the given stack can provide the required fluid amount.
	 * Uses a simulated extraction to ensure the container actually supports
	 * extracting the exact amount (e.g., cells only allow full-bucket extraction,
	 * so a recipe requiring 500 Mb will not accept a 1000 Mb cell).
	 */
	@Override
	public boolean test(ItemStack stack) {
		if (stack.isEmpty()) return false;
		return canExtractFluid(ContainerItemContext.withConstant(stack));
	}

	@Override
	public Stream<Holder<Item>> items() {
		return BuiltInRegistries.ITEM.stream()
			.filter(item -> canExtractFluid(ContainerItemContext.withConstant(new ItemStack(item))))
			.map(Item::builtInRegistryHolder);
	}

	private boolean canExtractFluid(ContainerItemContext context) {
		Storage<FluidVariant> fluidStorage = context.find(FluidStorage.ITEM);
		if (fluidStorage == null) return false;

		FluidVariant target = FluidVariant.of(fluid.value());
		long required = getAmountDroplets();

		// Simulate extraction to verify the container actually supports it
		try (Transaction tx = Transaction.openOuter()) {
			long extracted = fluidStorage.extract(target, required, tx);
			// Transaction is never committed, so nothing is actually modified
			return extracted >= required;
		}
	}

	@Override
	public boolean requiresTesting() {
		return true;
	}

	@Override
	public CustomIngredientSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	/**
	 * Computes the crafting remainder for an input item after extracting the required fluid.
	 * For items implementing {@link ItemFluidInfo}, returns the empty container.
	 * For other items, returns the input as-is (preserving the container).
	 */
	public ItemStack computeRemainder(ItemStack input) {
		if (input.getItem() instanceof ItemFluidInfo fluidInfo) {
			return fluidInfo.getEmpty();
		}
		// For unknown containers, preserve the item
		return input.copy();
	}

	/**
	 * Modifies the crafting remainders for a recipe that contains {@link FluidContainerIngredient}s.
	 * This ensures fluid containers are preserved (returned as empty or with reduced fluid)
	 * rather than being consumed.
	 */
	public static void modifyRemainders(CraftingRecipe recipe, CraftingInput input, NonNullList<ItemStack> remainders) {
		PlacementInfo info = recipe.placementInfo();
		if (info == null || info.ingredients() == null) return;

		for (int slot = 0; slot < input.size(); slot++) {
			ItemStack inputStack = input.getItem(slot);
			if (inputStack.isEmpty()) continue;

			// Check if any FluidContainerIngredient in the recipe matches this input
			for (Ingredient ingredient : info.ingredients()) {
				var custom = ingredient.getCustomIngredient();
				if (custom instanceof FluidContainerIngredient fluidIngredient && fluidIngredient.test(inputStack)) {
					remainders.set(slot, fluidIngredient.computeRemainder(inputStack));
					break;
				}
			}
		}
	}

	private static class Serializer implements CustomIngredientSerializer<FluidContainerIngredient> {
		private static final Identifier ID = Identifier.fromNamespaceAndPath("reborncore", "fluid_container");

		private static final MapCodec<FluidContainerIngredient> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
				BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(FluidContainerIngredient::getFluid),
				com.mojang.serialization.Codec.LONG.optionalFieldOf("amount", 1000L).forGetter(FluidContainerIngredient::getAmountMb)
			).apply(instance, FluidContainerIngredient::new)
		);

		private static final StreamCodec<RegistryFriendlyByteBuf, FluidContainerIngredient> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.holderRegistry(Registries.FLUID), FluidContainerIngredient::getFluid,
			ByteBufCodecs.VAR_LONG, FluidContainerIngredient::getAmountMb,
			FluidContainerIngredient::new
		);

		@Override
		public Identifier getIdentifier() {
			return ID;
		}

		@Override
		public MapCodec<FluidContainerIngredient> getCodec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, FluidContainerIngredient> getStreamCodec() {
			return STREAM_CODEC;
		}
	}
}
