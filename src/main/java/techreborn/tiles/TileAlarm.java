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

import net.minecraft.ChatFormat;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import reborncore.api.IToolDrop;
import reborncore.common.util.ChatUtils;
import reborncore.common.util.StringUtils;
import techreborn.blocks.BlockAlarm;
import techreborn.init.ModSounds;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;
import techreborn.utils.MessageIDs;

public class TileAlarm extends BlockEntity 
	implements Tickable, IToolDrop {
	private int selectedSound = 1;

	public TileAlarm() {
		super(TRTileEntities.ALARM);
	}

	public void rightClick() {
		if (!world.isClient) {
			if (selectedSound < 3) {
				selectedSound++;
			} else {
				selectedSound = 1;
			}
			ChatUtils.sendNoSpamMessages(MessageIDs.alarmID, new TextComponent(
					ChatFormat.GRAY + StringUtils.t("techreborn.message.alarm") + " " + "Alarm " + selectedSound));
		}
	}
	
	// TileEntity
	@Override
	public CompoundTag toTag(CompoundTag compound) {
		if (compound == null) {
			compound = new CompoundTag();
		}
		compound.putInt("selectedSound", this.selectedSound);
		return super.toTag(compound);
	}

	@Override
	public void fromTag(CompoundTag compound) {
		if (compound != null && compound.containsKey("selectedSound")) {
			selectedSound = compound.getInt("selectedSound");
		}
		super.fromTag(compound);
	}

	//TODO 1.13 seems to be gone?
//	@Override
//	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
//		return false;
//	}

	// ITickable
	@Override
	public void tick() {
		if (!world.isClient && world.getTime() % 25 == 0 && world.isReceivingRedstonePower(getPos())) {
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

		} else if (!world.isClient && world.getTime() % 25 == 0) {
			BlockAlarm.setActive(false, world, pos);
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return TRContent.Machine.ALARM.getStack();
	}
}
