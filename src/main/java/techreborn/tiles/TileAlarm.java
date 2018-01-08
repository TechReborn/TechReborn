/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import techreborn.blocks.BlockAlarm;
import techreborn.init.ModSounds;

public class TileAlarm extends TileEntity implements ITickable {
	boolean state = false;
	int selectedSound;

	@Override
	public void update() {
		if (!world.isRemote && world.getTotalWorldTime() % 25 == 0 && world.isBlockPowered(getPos())) {
			BlockAlarm.setActive(true, world, pos);
			world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), ModSounds.ALARM, SoundCategory.BLOCKS, 4F, 1F);
			state = true;
		} else if (!world.isRemote && world.getTotalWorldTime() % 25 == 0) {
			BlockAlarm.setActive(false, world, pos);
			state = false;
		}
	}
}
