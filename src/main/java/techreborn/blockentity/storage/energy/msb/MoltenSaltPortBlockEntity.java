/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2021 TechReborn
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
package techreborn.blockentity.storage.energy.msb;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.common.powerSystem.RcEnergyTier;
import team.reborn.energy.api.EnergyStorage;
import techreborn.blocks.storage.energy.msb.MoltenSaltPortBlock;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent.MoltenSaltPorts;

public class MoltenSaltPortBlockEntity extends BlockEntity {

	private RcEnergyTier tier;
	private BatteryEnergyStore battery;
	private EnergyStorage cachedView = BatteryEnergyStore.ZERO;

	public MoltenSaltPortBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.MOLTEN_SALT_PORT, pos, state);
		MoltenSaltPorts port = MoltenSaltPorts.BLOCKS.get(state.getBlock());
		this.tier = port.tier;
	}



	public void link(BatteryEnergyStore battery) {
		this.battery = battery;
		blockStateUpdate();
	}

	public EnergyStorage getEnergyStorage() {
		return cachedView;
	}

	public void blockStateUpdate() {
		if (battery == null) {
			return;
		}

		boolean isCharging = world.getBlockState(pos).get(MoltenSaltPortBlock.CHARGING);
		if (isCharging) {
			this.cachedView = this.battery.newView(tier.getMaxInput(), 0);
		} else {
			this.cachedView = this.battery.newView(0, tier.getMaxOutput());
		}
	}
}
