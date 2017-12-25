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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import java.util.List;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileQuantumTank extends TileLegacyMachineBase
	implements IInventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "quantum_tank", key = "QuantumTankMaxStorage", comment = "Maximum amount of millibuckets a Quantum Tank can store")
	public static int maxStorage = Integer.MAX_VALUE;
	//  @ConfigRegistry(config = "machines", category = "quantum_tank", key = "QuantumTankWrenchDropRate", comment = "Quantum Tank Wrench Drop Rate")
	public static float wrenchDropRate = 1.0F;

	public Tank tank = new Tank("TileQuantumTank", maxStorage, this);
	public Inventory inventory = new Inventory(3, "TileQuantumTank", 64, this);

	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.readFromNBTWithoutCoords(tagCompound);
	}

	public void readFromNBTWithoutCoords(final NBTTagCompound tagCompound) {
		this.tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		this.writeToNBTWithoutCoords(tagCompound);
		return tagCompound;
	}

	public NBTTagCompound writeToNBTWithoutCoords(final NBTTagCompound tagCompound) {
		this.tank.writeToNBT(tagCompound);
		return tagCompound;
	}

	@Override
	public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet) {
		this.world.markBlockRangeForRenderUpdate(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(),
			this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void update() {
		super.update();
		if (!this.world.isRemote) {
			if (FluidUtils.drainContainers(this.tank, this.inventory, 0, 1)
				|| FluidUtils.fillContainers(this.tank, this.inventory, 0, 1, this.tank.getFluidType()))
				this.syncWithAll();

			if (this.tank.getFluidType() != null && this.getStackInSlot(2) == ItemStack.EMPTY) {
				this.inventory.setInventorySlotContents(2, new ItemStack(this.tank.getFluidType().getBlock()));
			} else if (this.tank.getFluidType() == null && this.getStackInSlot(2) != ItemStack.EMPTY) {
				this.setInventorySlotContents(2, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.tank;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return this.getDropWithNBT();
	}

	public ItemStack getDropWithNBT() {
		final NBTTagCompound tileEntity = new NBTTagCompound();
		final ItemStack dropStack = new ItemStack(ModBlocks.QUANTUM_TANK, 1);
		this.writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	@Override
	public void addInfo(final List<String> info, final boolean isRealTile) {
		if (isRealTile) {
			if (this.tank.getFluid() != null) {
				info.add(this.tank.getFluidAmount() + " of " + this.tank.getFluidType().getName());
			} else {
				info.add("Empty");
			}
		}
		info.add("Capacity " + this.tank.getCapacity() + " mb");

	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("quantumtank").player(player.inventory).inventory(8, 84).hotbar(8, 142)
			.addInventory().tile(this).fluidSlot(0, 80, 17).outputSlot(1, 80, 53).fakeSlot(2, 59, 42).addInventory()
			.create(this);
	}
}
