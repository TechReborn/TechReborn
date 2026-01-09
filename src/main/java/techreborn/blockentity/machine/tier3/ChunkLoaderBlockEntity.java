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

import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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

	public final RebornInventory<ChunkLoaderBlockEntity> inventory = new RebornInventory<>(0, "ChunkLoaderBlockEntity", 64, this);
	private int radius;
	private String ownerUdid;

	public ChunkLoaderBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.CHUNK_LOADER, pos, state);
		this.radius = 1;
	}

	public void handleGuiInputFromClient(int buttonID, @Nullable Player playerEntity) {
		radius += buttonID;

		if (radius > TechRebornConfig.chunkLoaderMaxRadius) {
			radius = TechRebornConfig.chunkLoaderMaxRadius;
		}
		if (radius <= 1) {
			radius = 1;
		}

		reload();

		if (playerEntity != null) {
			ChunkLoaderManager manager = ChunkLoaderManager.get(getLevel());
			manager.syncChunkLoaderToClient((ServerPlayer) playerEntity, getBlockPos());
		}
	}

	private void reload() {
		unloadAll();
		load();
	}

	private void load() {
		ChunkLoaderManager manager = ChunkLoaderManager.get(getLevel());
		ChunkPos rootPos = getChunkPos();
		int loadRadius = radius - 1;
		for (int i = -loadRadius; i <= loadRadius; i++) {
			for (int j = -loadRadius; j <= loadRadius; j++) {
				ChunkPos loadPos = new ChunkPos(rootPos.x() + i, rootPos.z() + j);

				if (!manager.isChunkLoaded(getLevel(), loadPos, getBlockPos())) {
					manager.loadChunk(getLevel(), loadPos, getBlockPos(), ownerUdid);
				}
			}
		}
	}

	private void unloadAll() {
		ChunkLoaderManager manager = ChunkLoaderManager.get(level);
		manager.unloadChunkLoader(level, getBlockPos());
	}

	public ChunkPos getChunkPos() {
		return ChunkPos.containing(getBlockPos());
	}

	// MachineBaseBlockEntity
	@Override
	public void onBreak(Level world, Player playerEntity, BlockPos blockPos, BlockState blockState) {
		if (world.isClientSide()) {
			return;
		}
		unloadAll();
		ChunkLoaderManager.get(world).clearClient((ServerPlayer) playerEntity);
	}

	@Override
	public void onPlace(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		ownerUdid = placer.getStringUUID();
		if (worldIn.isClientSide()) return;
		reload();
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putInt("radius", radius);
		if (ownerUdid != null && !ownerUdid.isEmpty()){
			view.putString("ownerUdid", ownerUdid);
		}
		inventory.write(view);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		this.radius = view.getIntOr("radius", 0);
		this.ownerUdid = view.getStringOr("ownerUdid", "");
		inventory.read(view);
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final Player entityPlayer) {
		return TRContent.Machine.CHUNK_LOADER.getStack();
	}

	// InventoryProvider
	@Override
	public RebornInventory<ChunkLoaderBlockEntity> getInventory() {
		return this.inventory;
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		return new ScreenHandlerBuilder("chunkloader").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this).sync(ByteBufCodecs.INT, this::getRadius, this::setRadius).addInventory().create(this, syncID);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}


}
