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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.api.tile.ItemHandlerProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.TechReborn;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;

@RebornRegister(TechReborn.MOD_ID)
public class TileChunkLoader extends TilePowerAcceptor implements IToolDrop, ItemHandlerProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "chunk_loader", key = "ChunkLoaderMaxInput", comment = "Chunk Loader Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "chunk_loader", key = "ChunkLoaderMaxEnergy", comment = "Chunk Loader Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;
	//  @ConfigRegistry(config = "machines", category = "chunk_loader", key = "ChunkLoaderWrenchDropRate", comment = "Chunk Loader Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public Inventory<TileChunkLoader> inventory = new Inventory<>(1, "TileChunkLoader", 64, this).withConfiguredAccess();

	public boolean isRunning;
	public int tickTime;

	public TileChunkLoader() {
		super(TRTileEntities.CHUNK_LOADER );
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
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
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
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
	public Inventory<TileChunkLoader> getInventory() {
		return this.inventory;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("chunkloader").player(player.inventory).inventory(8,84).hotbar(8,142).addInventory()
			.create(this);
	}
}
