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

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		if (world == null) return;

		if (selectedSound < 3) {
			selectedSound++;
		} else {
			selectedSound = 1;
		}

		if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(Text.translatable("techreborn.message.alarm")
											.formatted(Formatting.GRAY)
											.append(" Alarm ")
											.append(String.valueOf(selectedSound)), true);
		}
	}

	// BlockEntity
	@Override
	public void writeNbt(NbtCompound compound) {
		if (compound == null) {
			compound = new NbtCompound();
		}
		compound.putInt("selectedSound", this.selectedSound);
	}

	@Override
	public void readNbt(NbtCompound compound) {
		if (compound != null && compound.contains("selectedSound")) {
			selectedSound = compound.getInt("selectedSound");
		}
		super.readNbt(compound);
	}

	// Tickable
	@Override
	public void tick(World world, BlockPos pos, BlockState state, AlarmBlockEntity blockEntity) {
		if (world == null || world.isClient()) return;
		if (world.getTime() % 25 != 0) return;

		if (world.isReceivingRedstonePower(getPos())) {
			BlockAlarm.setActive(true, world, pos);
			switch (selectedSound) {
				case 1 -> world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM, SoundCategory.BLOCKS, 4F, 1F);
				case 2 -> world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM_2, SoundCategory.BLOCKS, 4F, 1F);
				case 3 -> world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM_3, SoundCategory.BLOCKS, 4F, 1F);
			}
		} else {
			BlockAlarm.setActive(false, world, pos);
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.ALARM.getStack();
	}
}
