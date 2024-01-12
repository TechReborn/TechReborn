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

package techreborn.blockentity.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.WorldUtils;
import techreborn.blocks.machine.tier1.PlayerDetectorBlock;
import techreborn.blocks.machine.tier1.PlayerDetectorBlock.PlayerDetectorType;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class PlayerDetectorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, BuiltScreenHandlerProvider {


	public String ownerUdid = "";
	boolean redstone = false;
	int radius = 16;

	public PlayerDetectorBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.PLAYER_DETECTOR, pos, state);
	}

	public boolean isProvidingPower() {
		return redstone;
	}

	public void handleGuiInputFromClient(int amount) {
		radius += amount;

		if (radius > TechRebornConfig.playerDetectorMaxRadius) {
			radius = TechRebornConfig.playerDetectorMaxRadius;
		}
		if (radius <= 1) {
			radius = 1;
		}
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world == null || world.isClient) {
			return;
		}

		if (world.getTime() % 20 != 0) {
			return;
		}

		boolean lastRedstone = redstone;
		redstone = false;
		if (getStored() > TechRebornConfig.playerDetectorEuPerTick) {
			for (PlayerEntity player : world.getPlayers()) {
				if (player.isSpectator()){
					continue;
				}
				if (MathHelper.sqrt((float)player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ())) <= (float)radius ) {
					PlayerDetectorType type = world.getBlockState(pos).get(PlayerDetectorBlock.TYPE);
					if (type == PlayerDetectorType.ALL) {// ALL
						redstone = true;
					} else if (type == PlayerDetectorType.OTHERS) {// Others
						if (!ownerUdid.isEmpty() && !ownerUdid.equals(player.getUuid().toString())) {
							redstone = true;
						}
					} else {// You
						if (!ownerUdid.isEmpty() && ownerUdid.equals(player.getUuid().toString())) {
							redstone = true;
						}
					}
				}
			}
			useEnergy(TechRebornConfig.playerDetectorEuPerTick);
		}
		if (lastRedstone != redstone) {
			WorldUtils.updateBlock(world, pos);
			world.updateNeighborsAlways(pos, world.getBlockState(pos).getBlock());
		}
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.playerDetectorMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return TechRebornConfig.playerDetectorMaxInput;
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		ownerUdid = tag.getString("ownerID");
		radius = tag.getInt("radius");
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putString("ownerID", ownerUdid);
		tag.putInt("radius", radius);
	}

	// MachineBaseBlockEntity
	@Override
	public boolean hasSlotConfig() {
		return false;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.PLAYER_DETECTOR.getStack();
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("player_detector")
				.player(player.getInventory())
				.inventory().hotbar().addInventory()
				.blockEntity(this)
				.syncEnergyValue()
				.sync(this::getCurrentRadius, this::setCurrentRadius)
				.addInventory().create(this, syncID);
	}

	public int getCurrentRadius() {
		return radius;
	}

	public void setCurrentRadius(int radius) {
		this.radius = radius;
	}
}
