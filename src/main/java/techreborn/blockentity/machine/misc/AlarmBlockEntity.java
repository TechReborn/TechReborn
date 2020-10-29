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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;
import reborncore.api.IToolDrop;
import reborncore.common.util.ChatUtils;
import techreborn.blocks.misc.BlockAlarm;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.MessageIDs;

public class AlarmBlockEntity extends BlockEntity
		implements Tickable, IToolDrop {
	private int selectedSound = 1;

	public AlarmBlockEntity() {
		super(TRBlockEntities.ALARM);
	}

	public void rightClick() {
		if (world == null || world.isClient) return;

		if (selectedSound < 3) {
			selectedSound++;
		} else {
			selectedSound = 1;
		}

		ChatUtils.sendNoSpamMessages(MessageIDs.alarmID, new TranslatableText("techreborn.message.alarm")
				.formatted(Formatting.GRAY)
				.append(" Alarm ")
				.append(String.valueOf(selectedSound)));
	}

	// BlockEntity
	@Override
	public CompoundTag toTag(CompoundTag compound) {
		if (compound == null) {
			compound = new CompoundTag();
		}
		compound.putInt("selectedSound", this.selectedSound);
		return super.toTag(compound);
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag compound) {
		if (compound != null && compound.contains("selectedSound")) {
			selectedSound = compound.getInt("selectedSound");
		}
		super.fromTag(blockState, compound);
	}

	// Tickable
	@Override
	public void tick() {
		if (world == null || world.isClient()) return;
		if (world.getTime() % 25 != 0) return;

		if (world.isReceivingRedstonePower(getPos())) {
			BlockAlarm.setActive(true, world, pos);
			switch (selectedSound) {
				case 1:
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM, SoundCategory.BLOCKS, 4F, 1F);
					break;
				case 2:
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM_2, SoundCategory.BLOCKS, 4F, 1F);
					break;
				case 3:
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.ALARM_3, SoundCategory.BLOCKS, 4F, 1F);
					break;
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
