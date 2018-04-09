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

package techreborn.tiles.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.api.fluidreplicator.FluidReplicatorRecipeCrafter;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileGenericMachine;

/**
 * @author drcrazy
 *
 */

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileFluidReplicator extends TileGenericMachine implements IContainerProvider {

	@ConfigRegistry(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxInput", comment = "Fluid Replicator Max Input (Value in EU)")
	public static int maxInput = 128;
	@ConfigRegistry(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxEnergy", comment = "Fluid Replicator Max Energy (Value in EU)")
	public static int maxEnergy = 40_000;

	public MultiblockChecker multiblockChecker;
	public static final int TANK_CAPACITY = 16_000;
	public Tank tank;
	int ticksSinceLastChange;
	public FluidReplicatorRecipeCrafter crafter;

	public TileFluidReplicator() {
		super("FluidReplicator", maxInput, maxEnergy, ModBlocks.FLUID_REPLICATOR, 3);
		final int[] inputs = new int[] { 0 };
		this.inventory = new Inventory(4, "TileFluidReplicator", 64, this);
		this.crafter = new FluidReplicatorRecipeCrafter(this, this.inventory, inputs, null);
		this.tank = new Tank("TileFluidReplicator", TileFluidReplicator.TANK_CAPACITY, this);
	}

	public boolean getMultiBlock() {
		if (multiblockChecker == null) {
			return false;
		}
		final boolean ring = multiblockChecker.checkRingY(1, 1, MultiblockChecker.REINFORCED_CASING,
				MultiblockChecker.ZERO_OFFSET);
		return ring;
	}

	// TileGenericMachine
	@Override
	public void update() {
		if (multiblockChecker == null) {
			final BlockPos pos = getPos().offset(getFacing().getOpposite(), 2);
			multiblockChecker = new MultiblockChecker(this.world, pos);
		}

		ticksSinceLastChange++;
		// Check cells input slot 2 time per second
		if (!world.isRemote && ticksSinceLastChange >= 10) {
			if (!inventory.getStackInSlot(1).isEmpty()) {
				FluidUtils.fillContainers(tank, inventory, 1, 2, tank.getFluidType());
			}
			ticksSinceLastChange = 0;
		}

		if (getMultiBlock()) {
			super.update();
		}

		tank.compareAndUpdate();
	}
	
	@Override
	public RecipeCrafter getRecipeCrafter() {
		return (RecipeCrafter) crafter;
	}

	// TilePowerAcceptor
	@Override
	public void readFromNBT(final NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		tank.readFromNBT(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tank.writeToNBT(tagCompound);
		return tagCompound;
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

	// TileLegacyMachineBase
	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
		if (slotIndex == 0) {
			if (itemStack.isItemEqual(new ItemStack(ModItems.UU_MATTER))) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(EntityPlayer player) {
		return new ContainerBuilder("fluidreplicator").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).fluidSlot(1, 124, 35).filterSlot(0, 55, 45, stack -> stack.getItem() == ModItems.UU_MATTER)
				.outputSlot(2, 124, 55).energySlot(3, 8, 72).syncEnergyValue().syncCrafterValue().addInventory()
				.create(this);
	}
}
