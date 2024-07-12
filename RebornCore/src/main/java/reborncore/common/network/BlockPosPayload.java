/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TeamReborn
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

package reborncore.common.network;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public interface BlockPosPayload {
	BlockPos pos();

	default boolean isWithinDistance(PlayerEntity player, double distance) {
		return player.getBlockPos().isWithinDistance(pos(), distance);
	}

	default boolean canUse(ServerPlayerEntity player, Predicate<ScreenHandler> screenHandlerPredicate) {
		ScreenHandler currentScreenHandler = player.currentScreenHandler;

		if (currentScreenHandler == null) {
			return false;
		}

		if (!screenHandlerPredicate.test(currentScreenHandler)) {
			return false;
		}

		return currentScreenHandler.canUse(player);
	}

	default <T extends BlockEntity> T getBlockEntity(BlockEntityType<T> type, PlayerEntity player) {
		if (!isWithinDistance(player, 64)) {
			throw new IllegalStateException("Player cannot use this block entity as its too far away");
		}

		BlockEntity blockEntity = getBlockEntity(player);

		if (type != blockEntity.getType()) {
			throw new IllegalStateException("Block entity is not of the correct type. Expected: " + Registries.BLOCK_ENTITY_TYPE.getId(type) + " but got: " + Registries.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()));
		}

		//noinspection unchecked
		return (T) blockEntity;
	}

	default <T extends BlockEntity> T getBlockEntity(Class<T> baseClass, PlayerEntity player) {
		if (!isWithinDistance(player, 64)) {
			throw new IllegalStateException("Player cannot use this block entity as its too far away");
		}

		BlockEntity blockEntity = getBlockEntity(player);

		if (!baseClass.isInstance(blockEntity)) {
			throw new IllegalStateException("Block entity is not of the correct class");
		}

		//noinspection unchecked
		return (T) blockEntity;
	}

	default BlockEntity getBlockEntity(PlayerEntity player) {
		if (!isWithinDistance(player, 64)) {
			throw new IllegalStateException("Player cannot use this block entity as its too far away");
		}

		BlockEntity blockEntity = player.getWorld().getBlockEntity(pos());

		if (blockEntity == null) {
			throw new IllegalStateException("Block entity is null");
		}

		return blockEntity;
	}
}
