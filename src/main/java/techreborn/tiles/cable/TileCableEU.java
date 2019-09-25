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

package techreborn.tiles.cable;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.cable.EnumCableType;

import java.util.List;

/**
 * @author estebes, modmuss50
 */
@Optional.InterfaceList({
	@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyConductor", modid = "ic2"),
	@Optional.Interface(iface = "techreborn.tiles.cable.ICable", modid = "ic2"),
	@Optional.Interface(iface = "net.minecraft.util.ITickable", modid = "ic2")
})
public class TileCableEU extends TileEntity implements ITickable, IListInfoProvider, IToolDrop, IEnergyConductor, ICable {
	// Fields >>
	private int transferRate = 0;
	private EnumCableType cableType = null;
	protected boolean addedToEnet = false;
	protected boolean checkedConnections = false;
	private byte connectivity = 0;
	// << Fields

	//MC calls this during world load. Keep it, please.
	public TileCableEU() {
		super();
	}

	public TileCableEU(EnumCableType cableType) {
		this.cableType = cableType;
		this.transferRate = cableType.transferRate * RebornCoreConfig.euPerFU;
	}

	private void updateCableType() {
		if (cableType == null) {
			cableType = world.getBlockState(pos).getValue(BlockCable.TYPE);
			transferRate = cableType.transferRate * RebornCoreConfig.euPerFU;
		}
		return;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public void invalidate() {
		if (getWorld().isRemote) return;

		if (addedToEnet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			addedToEnet = false;
		}

		super.invalidate();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		notifyBlockUpdate();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		connectivity = compound.getByte("connectivity");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setByte("connectivity", connectivity);
		return compound;
	}

	// Networking >>
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		notifyBlockUpdate();
	}

	private void notifyBlockUpdate() {
		final IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, state, 3);
	}
	// << Networking

	// ICable >>
	@Optional.Method(modid = "ic2")
	@Override
	public void onNeighborChanged(Block neighbor, BlockPos neighborPos) {
		if (!world.isRemote) updateConnectivity();
	}

	@Optional.Method(modid = "ic2")
	@Override
	public void updateConnectivity() {
		World world = getWorld();
		byte newConnectivity = 0;
		int mask = 1;

		for (EnumFacing facing : EnumFacing.values()) {
			IEnergyTile energyTile = EnergyNet.instance.getTile(world, pos.offset(facing));
			if (energyTile != null) newConnectivity |= mask;

			mask *= 2;
		}

		if (connectivity != newConnectivity) {
			connectivity = newConnectivity;
			markDirty();
		}
	}

	@Optional.Method(modid = "ic2")
	@Override
	public byte getConnectivity() {
		return connectivity;
	}
	// << ICable

	// ITickable
	@Optional.Method(modid = "ic2")
	@Override
	public void update() {
		if (world.isRemote) return;

		if (cableType == null) updateCableType();

		if (!addedToEnet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			addedToEnet = true;
		}

		if (!checkedConnections) {
			updateConnectivity();
			checkedConnections = true;
		}
	}

	// IListInfoProvider >>
	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		info.add(TextFormatting.GRAY + StringUtils.t("techreborn.tooltip.transferRate") + ": "
			+ TextFormatting.GOLD
			+ PowerSystem.getLocaliszedPowerFormatted(transferRate / RebornCoreConfig.euPerFU) + "/t");
		info.add(TextFormatting.GRAY + StringUtils.t("techreborn.tooltip.tier") + ": "
			+ TextFormatting.GOLD + StringUtils.toFirstCapitalAllLowercase(cableType.tier.toString()));
	}
	// << IListInfoProvider

	// IToolDrop >>
	@Override
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return cableType.getStack();
	}
	// << IToolDrop

	// IEnergyConductor >>
	@Optional.Method(modid = "ic2")
	@Override
	public double getConductionLoss() {
		return cableType.euCableLoss;
	}

	/**
	 * Amount of energy the insulation will handle before shocking nearby players and mobs.
	 *
	 * @return Insulation energy absorption in EU
	 */
	@Optional.Method(modid = "ic2")
	@Override
	public double getInsulationEnergyAbsorption() {
		switch (cableType) {
			case TIN:
				return 3;
			case COPPER:
			case GOLD:
			case HV:
				return 0;
			case ICOPPER:
			case IGOLD:
			case IHV:
			case GLASSFIBER:
			case SUPERCONDUCTOR:
				return Integer.MAX_VALUE;
		}

		return Integer.MAX_VALUE;
	}

	// This should be deprecated in future IC2 APIs
	@Optional.Method(modid = "ic2")
	@Override
	public double getInsulationBreakdownEnergy() {
		return 9001; // Obsolete
	}

	/**
	 * Amount of energy the conductor will handle before it melts.
	 *
	 * @return Conductor-destroying energy in EU
	 */
	@Optional.Method(modid = "ic2")
	@Override
	public double getConductorBreakdownEnergy() {
		return cableType.transferRate + 1;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public void removeInsulation() {
		// NO-OP
	}

	@Optional.Method(modid = "ic2")
	@Override
	public void removeConductor() {
		world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

		for(int l = 0; l < 8; l++)  {
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + 1.2, pos.getZ() + Math.random(), 0, 0, 0);
		}

		world.setBlockToAir(pos); // Just destroy the block
	}

	/**
	 * This is only useful for the purposes of colored cables
	 * @return true, if it's a fitting cable or any other energy source
	 */
	@Optional.Method(modid = "ic2")
	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing enumFacing) {
		return true;
	}

	/**
	 * This is only useful for the purposes of colored cables
	 * @return true, if it's a fitting cable or any other energy sink
	 */
	@Optional.Method(modid = "ic2")
	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor iEnergyAcceptor, EnumFacing enumFacing) {
		return true;
	}
	// << IEnergyConductor
}
