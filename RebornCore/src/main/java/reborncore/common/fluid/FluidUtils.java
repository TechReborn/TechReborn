/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TeamReborn
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

package reborncore.common.fluid;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FluidUtils {

	@NotNull
	public static Fluid fluidFromBlock(Block block) {
		if (block instanceof FluidBlock fluidBlock) {
			return fluidBlock.fluid;
		}

		return Fluids.EMPTY;
	}

	public static List<Fluid> getAllFluids() {
		return Registries.FLUID.stream().collect(Collectors.toList());
	}

	public static boolean drainContainers(Tank tank, Inventory inventory, int inputSlot, int outputSlot) {
		return drainContainers(tank, inventory, inputSlot, outputSlot, false);
	}

	public static boolean drainContainers(Tank tank, Inventory inventory, int inputSlot, int outputSlot, boolean voidFluid) {
		Storage<FluidVariant> itemStorage = getItemFluidStorage(inventory, inputSlot, outputSlot);

		if (voidFluid) {
			// Just extract as much as we can
			try (Transaction tx = Transaction.openOuter()) {
				boolean didSomething = false;
				for (var view : itemStorage) {
					if (view.isResourceBlank()) continue;

					didSomething = didSomething | view.extract(view.getResource(), Long.MAX_VALUE, tx) > 0;
				}
				tx.commit();
				return didSomething;
			}
		} else {
			return StorageUtil.move(itemStorage, tank, fv -> true, Long.MAX_VALUE, null) > 0;
		}
	}

	public static boolean fillContainers(Tank source, Inventory inventory, int inputSlot, int outputSlot) {
		return StorageUtil.move(
				source,
				getItemFluidStorage(inventory, inputSlot, outputSlot),
				fv -> true,
				Long.MAX_VALUE,
				null
		) > 0;
	}

	private static Storage<FluidVariant> getItemFluidStorage(Inventory inventory, int inputSlot, int outputSlot) {
		var invWrapper = InventoryStorage.of(inventory, null);
		var input = invWrapper.getSlot(inputSlot);
		var output = invWrapper.getSlot(outputSlot);
		var context = new ContainerItemContext() {
			@Override
			public SingleSlotStorage<ItemVariant> getMainSlot() {
				return input;
			}

			@Override
			public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
				return output.insert(itemVariant, maxAmount, transactionContext);
			}

			@Override
			public long insert(ItemVariant itemVariant, long maxAmount, TransactionContext transaction) {
				// Don't allow insertion in the input slot
				return insertOverflow(itemVariant, maxAmount, transaction);
			}

			@Override
			public List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
				return List.of();
			}
		};
		var storage = context.find(FluidStorage.ITEM);
		return storage != null ? storage : Storage.empty();
	}

	public static boolean fluidEquals(@NotNull Fluid fluid, @NotNull Fluid fluid1) {
		return fluid == fluid1;
	}

	public static boolean isContainer(ItemStack stack) {
		return ContainerItemContext.withInitial(stack).find(FluidStorage.ITEM) != null;
	}

	public static boolean isContainerEmpty(ItemStack stack) {
		var fluidStorage = ContainerItemContext.withInitial(stack).find(FluidStorage.ITEM);
		if (fluidStorage == null) return false;

		// Use current transaction in case this check is nested in a transfer operation.
		try (var tx = Transaction.openNested(Transaction.getCurrentUnsafe())) {
			for (var view : fluidStorage) {
				if (!view.isResourceBlank() && view.getAmount() > 0) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean containsMatchingFluid(ItemStack stack, Predicate<Fluid> predicate) {
		var fluidStorage = ContainerItemContext.withInitial(stack).find(FluidStorage.ITEM);
		if (fluidStorage == null) return false;

		// Use current transaction in case this check is nested in a transfer operation.
		try (var tx = Transaction.openNested(Transaction.getCurrentUnsafe())) {
			for (var view : fluidStorage) {
				if (!view.isResourceBlank() && view.getAmount() > 0 && predicate.test(view.getResource().getFluid())) {
					return true;
				}
			}
		}

		return false;
	}

	@Deprecated
	public static boolean interactWithFluidHandler(PlayerEntity playerIn, Hand hand, Tank tank) {
		// TODO
		return false;
	}

	public static String getFluidName(@NotNull FluidInstance fluidInstance) {
		// TODO: use FluidVariantRendering
		return getFluidName(fluidInstance.getFluid());
	}

	public static String getFluidName(@NotNull Fluid fluid) {
		return Text.translatable(fluid.getDefaultState().getBlockState().getBlock().getTranslationKey()).getString();
	}
}
