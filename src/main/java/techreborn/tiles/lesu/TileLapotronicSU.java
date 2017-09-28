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

package techreborn.tiles.lesu;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.blocks.storage.BlockLapotronicSU;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import java.util.ArrayList;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileLapotronicSU extends TilePowerAcceptor implements IToolDrop{// TODO wrench

	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuMaxInput", comment = "LESU Max Input (Value in EU)")
	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuMaxOutput", comment = "LESU Base Output (Value in EU)")
	public static int baseOutput = 16;
	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuMaxEnergyPerBlock", comment = "LESU Max Energy Per Block (Value in EU)")
	public static int storagePerBlock = 1000000;
	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuExtraIO", comment = "LESU Extra I/O Multiplier")
	public static int extraIOPerBlock = 8;

	public int connectedBlocks = 0;
	public Inventory inventory = new Inventory(2, "TileAdjustableSU", 64, this);
	private ArrayList<LesuNetwork> countedNetworks = new ArrayList<>();
	private double euLastTick = 0;
	private double euChange;
	private int ticks;
	private int output;
	private int maxStorage;

	public TileLapotronicSU() {
		super();
	}

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			return;
		}
		countedNetworks.clear();
		connectedBlocks = 0;
		for (EnumFacing dir : EnumFacing.values()) {
			if (world.getTileEntity(
				new BlockPos(getPos().getX() + dir.getFrontOffsetX(), getPos().getY() + dir.getFrontOffsetY(),
					getPos().getZ() + dir.getFrontOffsetZ())) instanceof TileLSUStorage) {
				if (((TileLSUStorage) world.getTileEntity(
					new BlockPos(getPos().getX() + dir.getFrontOffsetX(), getPos().getY() + dir.getFrontOffsetY(),
						getPos().getZ() + dir.getFrontOffsetZ()))).network != null) {
					LesuNetwork network = ((TileLSUStorage) world.getTileEntity(new BlockPos(
						getPos().getX() + dir.getFrontOffsetX(), getPos().getY() + dir.getFrontOffsetY(),
						getPos().getZ() + dir.getFrontOffsetZ()))).network;
					if (!countedNetworks.contains(network)) {
						if (network.master == null || network.master == this) {
							connectedBlocks += network.storages.size();
							countedNetworks.add(network);
							network.master = this;
							break;
						}
					}
				}
			}
		}
		maxStorage = ((connectedBlocks + 1) * storagePerBlock);
		output = (connectedBlocks * extraIOPerBlock) + baseOutput;

		if (ticks == 100) {
			euChange = -1;
			ticks = 0;
		} else {
			ticks++;
			if (euChange == -1) {
				euChange = 0;
			}
			euChange += getEnergy() - euLastTick;
			if (euLastTick == getEnergy()) {
				euChange = 0;
			}
		}

		euLastTick = getEnergy();
	}

	public double getEuChange() {
		if (euChange == -1) {
			return 0;
		}
		return (euChange / ticks);
	}

	@Override
	public double getBaseMaxPower() {
		return maxStorage;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return direction != getFacingEnum();
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return direction == getFacingEnum();
	}

	@Override
	public double getBaseMaxOutput() {
		return output;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	@Override
	public EnumFacing getFacingEnum() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockLapotronicSU) {
			return ((BlockLapotronicSU) block).getFacing(world.getBlockState(pos));
		}
		return null;
	}

	@Override
	public ItemStack getToolDrop(EntityPlayer p0) {
		return new ItemStack(ModBlocks.LAPOTRONIC_SU, 1);
	}
}
