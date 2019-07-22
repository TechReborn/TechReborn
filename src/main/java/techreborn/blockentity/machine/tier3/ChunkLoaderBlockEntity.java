/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.blockentity.machine.tier3;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.RebornInventory;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;

@RebornRegister(TechReborn.MOD_ID)
public class ChunkLoaderBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "chunk_loader", key = "ChunkLoaderMaxInput", comment = "Chunk Loader Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "chunk_loader", key = "ChunkLoaderMaxEnergy", comment = "Chunk Loader Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;
	//  @ConfigRegistry(config = "machines", category = "chunk_loader", key = "ChunkLoaderWrenchDropRate", comment = "Chunk Loader Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public RebornInventory<ChunkLoaderBlockEntity> inventory = new RebornInventory<>(1, "ChunkLoaderBlockEntity", 64, this).withConfiguredAccess();

	public boolean isRunning;
	public int tickTime;

	public ChunkLoaderBlockEntity() {
		super(TRBlockEntities.CHUNK_LOADER );
	}

	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.CHUNK_LOADER.getStack();
	}

	public boolean isComplete() {
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final Direction direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final Direction direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	@Override
	public RebornInventory<ChunkLoaderBlockEntity> getInventory() {
		return this.inventory;
	}

	@Override
	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("chunkloader").player(player.inventory).inventory(8,84).hotbar(8,142).addInventory()
			.create(this, syncID);
	}
}
