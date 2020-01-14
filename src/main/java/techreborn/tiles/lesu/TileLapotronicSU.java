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

package techreborn.tiles.lesu;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.RebornCoreConfig;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.blocks.storage.BlockLapotronicSU;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
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
	public static int storagePerBlock = 1_000_000;
	@ConfigRegistry(config = "machines", category = "lesu", key = "LesuExtraIO", comment = "LESU Extra I/O Multiplier")
	public static int extraIOPerBlock = 8;

	public int connectedBlocks = 0;
	private ArrayList<LesuNetwork> countedNetworks = new ArrayList<>();

	public TileLapotronicSU() {
		super("LESU", 2, ModBlocks.LAPOTRONIC_SU, EnumPowerTier.INSANE, 8192, baseOutput, 1_000_000);
		checkOverfill = false;
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
			BlockPos adjucentBlockPos = new BlockPos(pos.getX() + dir.getXOffset(),
					pos.getY() + dir.getYOffset(), pos.getZ() + dir.getZOffset());
			TileEntity adjucentTile = world.getTileEntity(adjucentBlockPos);
			if (adjucentTile == null || !(adjucentTile instanceof TileLSUStorage)) {
				continue;
			}
			if (((TileLSUStorage) adjucentTile).network == null) {
				continue;
			}
			LesuNetwork network = ((TileLSUStorage) adjucentTile).network;
			if (!countedNetworks.contains(network)) {
				if (network.master == null || network.master == this) {
					connectedBlocks += network.storages.size();
					countedNetworks.add(network);
					network.master = this;
					break;
				}
			}

		}
		setMaxStorage();
		maxOutput = (connectedBlocks * extraIOPerBlock) + baseOutput;
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
		return maxOutput;
	}
	
	public void setOutputRate(int output) {
		this.maxOutput = output;
	}
	
	public int getConnectedBlocksNum() {
		return connectedBlocks;
	}
	
	public void setConnectedBlocksNum(int value) {
		this.connectedBlocks = value;
		if (world.isRemote) {
			setMaxStorage();
		}
	}
	
	public void setMaxStorage(){
		maxStorage  = (connectedBlocks + 1) * storagePerBlock;
		if (maxStorage < 0 || maxStorage > Integer.MAX_VALUE / RebornCoreConfig.euPerFU) {
			maxStorage = Integer.MAX_VALUE / RebornCoreConfig.euPerFU;
		}
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("lesu").player(player.inventory).inventory().hotbar().armor().complete(8, 18)
				.addArmor().addInventory().tile(this).energySlot(0, 62, 45).energySlot(1, 98, 45).syncEnergyValue()
				.syncIntegerValue(this::getRedstoneModeInt, this::setRedstoneModeInt)
				.syncIntegerValue(this::getOutputRate, this::setOutputRate)
				.syncIntegerValue(this::getConnectedBlocksNum, this::setConnectedBlocksNum).addInventory().create(this);
	}
}
