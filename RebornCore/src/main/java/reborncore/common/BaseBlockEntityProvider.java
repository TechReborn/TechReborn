/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public abstract class BaseBlockEntityProvider extends Block implements BlockEntityProvider {
	protected BaseBlockEntityProvider(Settings builder) {
		super(builder);
	}

	public Optional<ItemStack> getDropWithContents(World world, BlockPos pos, ItemStack stack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity == null) {
			return Optional.empty();
		}
		ItemStack newStack = stack.copy();
		NbtCompound blockEntityData = blockEntity.writeNbt(new NbtCompound());
		stripLocationData(blockEntityData);
		if (!newStack.hasTag()) {
			newStack.setTag(new NbtCompound());
		}
		newStack.getTag().put("blockEntity_data", blockEntityData);
		return Optional.of(newStack);
	}

	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasTag() && stack.getTag().contains("blockEntity_data")) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			NbtCompound nbt = stack.getTag().getCompound("blockEntity_data");
			injectLocationData(nbt, pos);
			blockEntity.readNbt(nbt);
			blockEntity.markDirty();
		}
	}

	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (world1, pos, state1, blockEntity) -> {
			if (blockEntity instanceof BlockEntityTicker) {
				((BlockEntityTicker) blockEntity).tick(world1, pos, state1, blockEntity);
			}
		};
	}

	private void stripLocationData(NbtCompound compound) {
		compound.remove("x");
		compound.remove("y");
		compound.remove("z");
	}

	private void injectLocationData(NbtCompound compound, BlockPos pos) {
		compound.putInt("x", pos.getX());
		compound.putInt("y", pos.getY());
		compound.putInt("z", pos.getZ());
	}

	public void getDrops(BlockState state, DefaultedList<ItemStack> drops, World world, BlockPos pos, int fortune){
		
	}
}
