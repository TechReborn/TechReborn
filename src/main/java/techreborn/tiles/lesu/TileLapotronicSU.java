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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.blocks.storage.BlockLapotronicSU;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.tiles.storage.TileEnergyStorage;

import java.util.ArrayList;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileLapotronicSU extends TileEnergyStorage implements IContainerProvider{

//	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuMaxInput", comment = "LESU Max Input (Value in EU)")
//	public static int maxInput = 8192;
	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuMaxOutput", comment = "LESU Base Output (Value in EU)")
	public static int baseOutput = 16;
	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuMaxEnergyPerBlock", comment = "LESU Max Energy Per Block (Value in EU)")
	public static int storagePerBlock = 1000000;
	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuExtraIO", comment = "LESU Extra I/O Multiplier")
	public static int extraIOPerBlock = 8;

	public int connectedBlocks = 0;
	private ArrayList<LesuNetwork> countedNetworks = new ArrayList<>();

	public TileLapotronicSU() {
		super("LESU", 2, ModBlocks.LAPOTRONIC_SU, EnumPowerTier.INSANE, 8192, baseOutput, 1000000);
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
		this.maxStorage = ((connectedBlocks + 1) * storagePerBlock);
		this.maxOutput = (connectedBlocks * extraIOPerBlock) + baseOutput;
	}

	@Override
	public EnumFacing getFacingEnum() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockLapotronicSU) {
			return ((BlockLapotronicSU) block).getFacing(world.getBlockState(pos));
		}
		return null;
	}
	
	public int getOutputRate() {
		return this.maxOutput;
	}
	
	public void setOutputRate(int output) {
		this.maxOutput = output;
	}
	
	public int getMaxStorage() {
		return this.maxStorage;
	}
	
	public void setMaxStorage(int value) {
		this.maxStorage = value;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("lesu").player(player.inventory).inventory().hotbar().armor().complete(8, 18)
				.addArmor().addInventory().tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45).syncEnergyValue()
				.syncIntegerValue(this::getOutputRate, this::setOutputRate)
				.syncIntegerValue(this::getMaxStorage, this::setMaxStorage).addInventory().create(this);
	}
}
