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

package techreborn.blockentity.machine.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import reborncore.api.IToolDrop;
import techreborn.blocks.misc.BlockAlarm;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class AlarmBlockEntity extends BlockEntity
		implements BlockEntityTicker<AlarmBlockEntity>, IToolDrop {
	private int selectedSound = 1;

	public AlarmBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.ALARM, pos, state);
	}

	public void rightClick(Entity entity) {
		if (level == null) return;

		if (selectedSound < 3) {
			selectedSound++;
		} else {
			selectedSound = 1;
		}

		if (entity instanceof ServerPlayer serverPlayerEntity) {
			serverPlayerEntity.sendOverlayMessage(Component.translatable("techreborn.message.alarm")
											.withStyle(ChatFormatting.GRAY)
											.append(" Alarm ")
											.append(String.valueOf(selectedSound)));
		}
	}

	// BlockEntity
	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putInt("selectedSound", this.selectedSound);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		selectedSound = view.getIntOr("selectedSound", 0);
	}

	// Tickable
	@Override
	public void tick(Level world, BlockPos pos, BlockState state, AlarmBlockEntity blockEntity) {
		if (world == null || world.isClientSide()) return;
		if (world.getGameTime() % 25 != 0) return;

		if (world.hasNeighborSignal(getBlockPos())) {
			BlockAlarm.setActive(true, world, pos);
			switch (selectedSound) {
				case 1 -> world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM, SoundSource.BLOCKS, 4F, 1F);
				case 2 -> world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM_2, SoundSource.BLOCKS, 4F, 1F);
				case 3 -> world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM_3, SoundSource.BLOCKS, 4F, 1F);
			}
		} else {
			BlockAlarm.setActive(false, world, pos);
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final Player entityPlayer) {
		return TRContent.Machine.ALARM.getStack();
	}
}
