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

package techreborn.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseBehavior;
import net.minecraft.core.dispenser.DropItemDispenseBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reborncore.common.crafting.RecipeUtils;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.items.CellItem;
import techreborn.recipe.recipes.ScrapBoxRecipe;

import java.util.List;

public final class TRDispenserBehavior {
	public static void init() {
		Registry.register(BuiltInRegistries.DISPENSE_BEHAVIOR_TYPE, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "cell"), CellDispenseBehavior.CODEC);
		Registry.register(BuiltInRegistries.DISPENSE_BEHAVIOR_TYPE, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "scrap_box"), ScrapBoxDispenseBehavior.CODEC);
	}

	public static Item.Properties cell(Item.Properties properties) {
		return properties.component(DataComponents.DISPENSE_BEHAVIOR, Holder.direct(List.of(CellDispenseBehavior.INSTANCE, DropItemDispenseBehavior.INSTANCE)));
	}

	public static Item.Properties scrapBox(Item.Properties properties) {
		if (TechRebornConfig.dispenseScrapboxes.get()) {
			properties.component(DataComponents.DISPENSE_BEHAVIOR, Holder.direct(List.of(ScrapBoxDispenseBehavior.INSTANCE)));
		}
		return properties;
	}

	private static final class CellDispenseBehavior implements DispenseBehavior {
		private static final CellDispenseBehavior INSTANCE = new CellDispenseBehavior();
		private static final MapCodec<CellDispenseBehavior> CODEC = MapCodec.unit(INSTANCE);

		@Override
		public DispenseResult execute(BlockSource source, ItemStack stack) {
			if (!(stack.getItem() instanceof CellItem cell)) {
				return DispenseResult.failure();
			}

			BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
			if (cell.getCellFluid() != Fluids.EMPTY) {
				if (cell.placeFluid(null, source.level(), target, null, stack)) {
					return DispenseBehavior.consumeWithRemainder(source, stack, cell.getEmpty());
				}
				return DispenseResult.stoppingFailure();
			}

			BlockState state = source.level().getBlockState(target);
			if (state.getBlock() instanceof BucketPickup pickup) {
				ItemStack filledBucket = pickup.pickupBlock(null, source.level(), target, state);
				if (filledBucket.getItem() instanceof ItemFluidInfo fluidInfo) {
					Fluid fluid = fluidInfo.getFluid(filledBucket);
					if (fluid instanceof FlowingFluid) {
						ItemStack filledCell = TRContent.Cells.getCellByFluid(fluid).getStack();
						return DispenseBehavior.consumeWithRemainder(source, stack, filledCell);
					}
				}
			}

			return DispenseResult.failure();
		}

		@Override
		public MapCodec<CellDispenseBehavior> codec() {
			return CODEC;
		}
	}

	private static final class ScrapBoxDispenseBehavior implements DispenseBehavior {
		private static final ScrapBoxDispenseBehavior INSTANCE = new ScrapBoxDispenseBehavior();
		private static final MapCodec<ScrapBoxDispenseBehavior> CODEC = MapCodec.unit(INSTANCE);

		@Override
		public DispenseResult execute(BlockSource source, ItemStack stack) {
			List<ScrapBoxRecipe> recipes = RecipeUtils.getRecipes(source.level(), ModRecipes.SCRAPBOX);
			if (recipes.isEmpty()) {
				return DispenseResult.stoppingFailure();
			}
			int index = source.level().getRandom().nextInt(recipes.size());
			ItemStack output = recipes.get(index).outputs().getFirst().create();
			DropItemDispenseBehavior.spawnItem(source, output);
			stack.shrink(1);
			return DispenseResult.success(stack);
		}

		@Override
		public MapCodec<ScrapBoxDispenseBehavior> codec() {
			return CODEC;
		}
	}
}
