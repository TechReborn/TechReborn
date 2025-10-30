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

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BlockPosPayload {
	BlockPos pos();

	default boolean isWithinDistance(Player player, double distance) {
		return player.blockPosition().closerThan(pos(), distance);
	}

	default boolean canUse(ServerPlayer player, Predicate<AbstractContainerMenu> screenHandlerPredicate) {
		AbstractContainerMenu currentScreenHandler = player.containerMenu;

		if (currentScreenHandler == null) {
			return false;
		}

		if (!screenHandlerPredicate.test(currentScreenHandler)) {
			return false;
		}

		return currentScreenHandler.stillValid(player);
	}

	default <T extends BlockEntity> T getBlockEntity(BlockEntityType<T> type, Player player) {
		if (!isWithinDistance(player, 64)) {
			throw new IllegalStateException("Player cannot use this block entity as its too far away");
		}

		BlockEntity blockEntity = getBlockEntity(player);

		if (type != blockEntity.getType()) {
			throw new IllegalStateException("Block entity is not of the correct type. Expected: " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(type) + " but got: " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType()));
		}

		//noinspection unchecked
		return (T) blockEntity;
	}

	default <T extends BlockEntity> T getBlockEntity(Class<T> baseClass, Player player) {
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

	default BlockEntity getBlockEntity(Player player) {
		if (!isWithinDistance(player, 64)) {
			throw new IllegalStateException("Player cannot use this block entity as its too far away");
		}

		BlockEntity blockEntity = player.level().getBlockEntity(pos());

		if (blockEntity == null) {
			throw new IllegalStateException("Block entity is null");
		}

		return blockEntity;
	}
}
