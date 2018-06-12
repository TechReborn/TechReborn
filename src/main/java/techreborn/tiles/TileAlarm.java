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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import reborncore.common.util.ChatUtils;
import techreborn.blocks.BlockAlarm;
import techreborn.init.ModSounds;
import techreborn.lib.MessageIDs;

public class TileAlarm extends TileEntity implements ITickable {
	private int selectedSound = 1;

	@Override
	public void update() {
		if (!world.isRemote && world.getTotalWorldTime() % 25 == 0 && world.isBlockPowered(getPos())) {
			BlockAlarm.setActive(true, world, pos);
			switch (selectedSound) {
				case 1:
					world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), ModSounds.ALARM, SoundCategory.BLOCKS, 4F, 1F);
					break;
				case 2:
					world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), ModSounds.ALARM_2, SoundCategory.BLOCKS, 4F, 1F);
					break;
				case 3:
					world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), ModSounds.ALARM_3, SoundCategory.BLOCKS, 4F, 1F);
					break;
			}

		} else if (!world.isRemote && world.getTotalWorldTime() % 25 == 0) {
			BlockAlarm.setActive(false, world, pos);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (compound == null) {
			compound = new NBTTagCompound();
		}
		compound.setInteger("selectedSound", this.selectedSound);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound != null && compound.hasKey("selectedSound")) {
			this.selectedSound = compound.getInteger("selectedSound");
		}
		super.readFromNBT(compound);
	}

	public void rightClick() {
		if (!world.isRemote) {
			if (selectedSound < 3) {
				selectedSound++;
			} else {
				selectedSound = 1;
			}
			ChatUtils.sendNoSpamMessages(MessageIDs.alarmID, new TextComponentString(TextFormatting.GRAY + I18n.format("techreborn.message.alarm") + " " + "Alarm " + selectedSound));
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return false;
	}
}
