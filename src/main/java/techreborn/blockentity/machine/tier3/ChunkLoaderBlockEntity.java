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

package techreborn.blockentity.machine.tier3;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.chunkloading.ChunkLoaderManager;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class ChunkLoaderBlockEntity extends MachineBaseBlockEntity implements IToolDrop, InventoryProvider, BuiltScreenHandlerProvider {

	public RebornInventory<ChunkLoaderBlockEntity> inventory = new RebornInventory<>(0, "ChunkLoaderBlockEntity", 64, this);
	private int radius;
	private String ownerUdid;

	public ChunkLoaderBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.CHUNK_LOADER, pos, state);
		this.radius = 1;
	}

	public void handleGuiInputFromClient(int buttonID, @Nullable PlayerEntity playerEntity) {
		radius += buttonID;

		if (radius > TechRebornConfig.chunkLoaderMaxRadius) {
			radius = TechRebornConfig.chunkLoaderMaxRadius;
		}
		if (radius <= 1) {
			radius = 1;
		}

		reload();

		if (playerEntity != null) {
			ChunkLoaderManager manager = ChunkLoaderManager.get(getWorld());
			manager.syncChunkLoaderToClient((ServerPlayerEntity) playerEntity, getPos());
		}
	}

	private void reload() {
		unloadAll();
		load();
	}

	private void load() {
		ChunkLoaderManager manager = ChunkLoaderManager.get(getWorld());
		ChunkPos rootPos = getChunkPos();
		int loadRadius = radius - 1;
		for (int i = -loadRadius; i <= loadRadius; i++) {
			for (int j = -loadRadius; j <= loadRadius; j++) {
				ChunkPos loadPos = new ChunkPos(rootPos.x + i, rootPos.z + j);

				if (!manager.isChunkLoaded(getWorld(), loadPos, getPos())) {
					manager.loadChunk(getWorld(), loadPos, getPos(), ownerUdid);
				}
			}
		}
	}

	private void unloadAll() {
		ChunkLoaderManager manager = ChunkLoaderManager.get(world);
		manager.unloadChunkLoader(world, getPos());
	}

	public ChunkPos getChunkPos() {
		return new ChunkPos(getPos());
	}

	// MachineBaseBlockEntity
	@Override
	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState) {
		if (world.isClient) {
			return;
		}
		unloadAll();
		ChunkLoaderManager.get(world).clearClient((ServerPlayerEntity) playerEntity);
	}

	@Override
	public void onPlace(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		ownerUdid = placer.getUuidAsString();
		if (worldIn.isClient) return;
		reload();
	}

	@Override
	public void writeNbt(NbtCompound tagCompound) {
		super.writeNbt(tagCompound);
		tagCompound.putInt("radius", radius);
		if (ownerUdid != null && !ownerUdid.isEmpty()){
			tagCompound.putString("ownerUdid", ownerUdid);
		}
		inventory.write(tagCompound);
	}

	@Override
	public void readNbt(NbtCompound nbtCompound) {
		super.readNbt(nbtCompound);
		this.radius = nbtCompound.getInt("radius");
		this.ownerUdid = nbtCompound.getString("ownerUdid");
		if (!StringUtils.isBlank(ownerUdid)) {
			nbtCompound.putString("ownerUdid", this.ownerUdid);
		}
		inventory.read(nbtCompound);
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.CHUNK_LOADER.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<ChunkLoaderBlockEntity> getInventory() {
		return this.inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("chunkloader").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this).sync(this::getRadius, this::setRadius).addInventory().create(this, syncID);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}


}
