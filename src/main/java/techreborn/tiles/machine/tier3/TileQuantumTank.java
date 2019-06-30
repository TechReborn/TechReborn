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

package techreborn.tiles.machine.tier3;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.tile.ItemHandlerProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;

import javax.annotation.Nullable;
import java.util.List;

@RebornRegister(TechReborn.MOD_ID)
public class TileQuantumTank extends TileMachineBase
	implements ItemHandlerProvider, IToolDrop, IListInfoProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "quantum_tank", key = "QuantumTankMaxStorage", comment = "Maximum amount of millibuckets a Quantum Tank can store")
	public static int maxStorage = Integer.MAX_VALUE;

	public Tank tank = new Tank("TileQuantumTank", maxStorage, this);
	public Inventory<TileQuantumTank> inventory = new Inventory<>(3, "TileQuantumTank", 64, this).withConfiguredAccess();

	public TileQuantumTank(){
		this(TRTileEntities.QUANTUM_TANK);
	}

	public TileQuantumTank(BlockEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public void readWithoutCoords(final CompoundTag tagCompound) {
		tank.read(tagCompound);
	}
	
	public CompoundTag writeWithoutCoords(final CompoundTag tagCompound) {
		tank.write(tagCompound);
		return tagCompound;
	}
	
	public ItemStack getDropWithNBT() {
		final CompoundTag tileEntity = new CompoundTag();
		final ItemStack dropStack = TRContent.Machine.QUANTUM_TANK.getStack();
		this.writeWithoutCoords(tileEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("tileEntity", tileEntity);
		return dropStack;
	}
	
	// TileMachineBase
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
// TODO: Fix in 1.13
//			if (FluidUtils.drainContainers(tank, inventory, 0, 1)
//					|| FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluidType())) {
//				this.syncWithAll();
//			}
				
		}
		tank.compareAndUpdate();
	}
	
	@Override
	public boolean canBeUpgraded() {
		return false;
	}
	
	@Override
	public void fromTag(final CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		readWithoutCoords(tagCompound);
	}

	@Override
	public CompoundTag toTag(final CompoundTag tagCompound) {
		super.toTag(tagCompound);
		writeWithoutCoords(tagCompound);
		return tagCompound;
	}
	
	// ItemHandlerProvider
	@Override
	public Inventory<TileQuantumTank> getInventory() {
		return this.inventory;
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return this.getDropWithNBT();
	}
	
	// IListInfoProvider
	@Override
	public void addInfo(final List<Text> info, final boolean isRealTile, boolean hasData) {
		if (isRealTile | hasData) {
			if (this.tank.getFluid() != null) {
				info.add(new LiteralText(this.tank.getFluidAmount() + " of " + this.tank.getFluidType().getName()));
			} else {
				info.add(new LiteralText("Empty"));
			}
		}
		info.add(new LiteralText("Capacity " + this.tank.getCapacity() + " mb"));
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final PlayerEntity player) {
		return new ContainerBuilder("quantumtank").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this).fluidSlot(0, 80, 17).outputSlot(1, 80, 53).addInventory()
			.create(this);
	}

	@Nullable
	@Override
	public Tank getTank() {
		return tank;
	}
}
