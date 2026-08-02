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

import reborncore.common.screen.builder.SyncedObjectTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
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

		if (radius > TechRebornConfig.playerDetectorMaxRadius.get()) {
			radius = TechRebornConfig.playerDetectorMaxRadius.get();
		}
		if (radius <= 1) {
			radius = 1;
		}
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);

		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}

		if (serverLevel.getGameTime() % 20 != 0) {
			return;
		}

		boolean lastRedstone = redstone;
		redstone = false;
		if (getStored() > TechRebornConfig.playerDetectorEuPerTick.get()) {
			for (Player player : serverLevel.players()) {
				if (player.isSpectator()){
					continue;
				}
				if (Mth.sqrt((float)player.distanceToSqr(pos.getX() +0.5f, pos.getY() +0.5f, pos.getZ() +0.5f)) <= (float)radius ) {
					PlayerDetectorType type = serverLevel.getBlockState(pos).getValue(PlayerDetectorBlock.TYPE);
					if (type == PlayerDetectorType.ALL) {// ALL
						redstone = true;
					} else if (type == PlayerDetectorType.OTHERS) {// Others
						if (!ownerUdid.isEmpty() && !ownerUdid.equals(player.getUUID().toString())) {
							redstone = true;
						}
					} else {// You
						if (!ownerUdid.isEmpty() && ownerUdid.equals(player.getUUID().toString())) {
							redstone = true;
						}
					}
				}
			}
			useEnergy(TechRebornConfig.playerDetectorEuPerTick.get());
		}
		if (lastRedstone != redstone) {
			WorldUtils.updateBlock(serverLevel, pos);
			serverLevel.updateNeighborsAt(pos, serverLevel.getBlockState(pos).getBlock(), ExperimentalRedstoneUtils.initialOrientation(serverLevel, null, null));
		}
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.playerDetectorMaxEnergy.get();
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
		return TechRebornConfig.playerDetectorMaxInput.get();
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		ownerUdid = view.getStringOr("ownerID", "");
		radius = view.getIntOr("radius", 0);
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putString("ownerID", ownerUdid);
		view.putInt("radius", radius);
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
	public ItemStack getToolDrop(Player p0) {
		return TRContent.Machine.PLAYER_DETECTOR.getStack();
	}

	// BuiltScreenHandlerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, Player player) {
		return new ScreenHandlerBuilder("player_detector")
				.player(player.getInventory())
				.inventory().hotbar().addInventory()
				.blockEntity(this)
				.syncEnergyValue()
				.sync(SyncedObjectTypes.INT, this::getCurrentRadius, this::setCurrentRadius)
				.addInventory().create(this, syncID);
	}

	public int getCurrentRadius() {
		return radius;
	}

	public void setCurrentRadius(int radius) {
		this.radius = radius;
	}
}
