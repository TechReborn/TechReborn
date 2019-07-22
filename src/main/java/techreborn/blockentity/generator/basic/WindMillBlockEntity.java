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

package techreborn.blockentity.generator.basic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;

/**
 * Created by modmuss50 on 25/02/2016.
 */

@RebornRegister(TechReborn.MOD_ID)
public class WindMillBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop {

	@ConfigRegistry(config = "generators", category = "wind_mill", key = "WindMillMaxOutput", comment = "Wind Mill Max Output (Value in EU)")
	public static int maxOutput = 128;
	@ConfigRegistry(config = "generators", category = "wind_mill", key = "WindMillMaxEnergy", comment = "Wind Mill Max Energy (Value in EU)")
	public static int maxEnergy = 10_000;
	@ConfigRegistry(config = "generators", category = "wind_mill", key = "WindMillEnergyPerTick", comment = "Wind Mill Energy Per Tick (Value in EU)")
	public static int baseEnergy = 2;
	@ConfigRegistry(config = "generators", category = "wind_mill", key = "WindMillThunderMultiplier", comment = "Wind Mill Thunder Multiplier")
	public static double thunderMultiplier = 1.25;

	public WindMillBlockEntity() {
		super(TRBlockEntities.WIND_MILL);
	}

	@Override
	public void tick() {
		super.tick();
		if (pos.getY() > 64) {
			int actualPower = baseEnergy;
			if (world.isThundering()) {
				actualPower *= thunderMultiplier;
			}
			addEnergy(actualPower); // Value taken from
			// http://wiki.industrial-craft.net/?title=Wind_Mill
			// Not worth making more complicated
		}
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(Direction direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(Direction direction) {
		return true;
	}

	@Override
	public double getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return TRContent.Machine.WIND_MILL.getStack();
	}
}
