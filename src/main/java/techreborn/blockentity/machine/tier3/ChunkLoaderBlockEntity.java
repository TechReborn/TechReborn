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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class ChunkLoaderBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, InventoryProvider, IContainerProvider {

	public RebornInventory<ChunkLoaderBlockEntity> inventory = new RebornInventory<>(1, "ChunkLoaderBlockEntity", 64, this);
	private int radius;

	public ChunkLoaderBlockEntity() {
		super(TRBlockEntities.CHUNK_LOADER );
		this.radius = 1;
	}
	
	public void handleGuiInputFromClient(int buttonID) {
		radius += buttonID;

		if (radius > TechRebornConfig.chunkLoaderMaxRadius) {
			radius = TechRebornConfig.chunkLoaderMaxRadius;
		}
		if (radius <= 1) {
			radius = 1;
		}
	}

	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.CHUNK_LOADER.getStack();
	}
	
	@Override
	public void tick() {
		super.tick();
		// TODO: chunkload
	}

	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);
		tagCompound.putInt("radius", radius);
		inventory.write(tagCompound);
		return tagCompound;
	}

	@Override
	public void fromTag(CompoundTag nbttagcompound) {
		super.fromTag(nbttagcompound);
		this.radius = nbttagcompound.getInt("radius");
		inventory.read(nbttagcompound);
	}
	
	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.chunkLoaderMaxEnergy;
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
		return TechRebornConfig.chunkLoaderMaxInput;
	}

	@Override
	public RebornInventory<ChunkLoaderBlockEntity> getInventory() {
		return this.inventory;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public BuiltContainer createContainer(int syncID, PlayerEntity player) {
		return new ContainerBuilder("chunkloader").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).energySlot(0, 8, 72).syncEnergyValue().syncIntegerValue(this::getRadius, this::setRadius).addInventory().create(this, syncID);
	}
}
