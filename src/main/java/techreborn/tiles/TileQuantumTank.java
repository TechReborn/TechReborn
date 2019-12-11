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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.fluids.RebornFluidTank;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.tile.RebornMachineTile;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import javax.annotation.Nullable;
import java.util.List;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileQuantumTank extends RebornMachineTile
        implements IInventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {

    @ConfigRegistry(config = "machines", category = "quantum_tank", key = "QuantumTankMaxStorage", comment = "Maximum amount of millibuckets a Quantum Tank can store")
    public static int maxStorage = Integer.MAX_VALUE;

    public RebornFluidTank tank = new RebornFluidTank("TileQuantumTank", maxStorage, this);
    public Inventory inventory = new Inventory(3, "TileQuantumTank", 64, this);

    public void readFromNBTWithoutCoords(final NBTTagCompound tagCompound) {
        tank.readFromNBT(tagCompound);
    }

    public NBTTagCompound writeToNBTWithoutCoords(final NBTTagCompound tagCompound) {
        tank.writeToNBT(tagCompound);
        return tagCompound;
    }

    public ItemStack getDropWithNBT() {
        final NBTTagCompound tileEntity = new NBTTagCompound();
        final ItemStack dropStack = new ItemStack(ModBlocks.QUANTUM_TANK, 1);
        this.writeToNBTWithoutCoords(tileEntity);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.getTagCompound().setTag("tileEntity", tileEntity);
        return dropStack;
    }

    // RebornMachineTile
    @Override
    public void update() {
        super.update();

        // Check cells input slot 10 times per second
        if (!world.isRemote && world.getTotalWorldTime() % 2 == 0) {
            if (!inventory.getStackInSlot(0).isEmpty()) {
                FluidUtils.drainContainers(tank, inventory, 0, 1);
                FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluidType());
                this.syncWithAll();
            }
        }
    }

    @Override
    public boolean canBeUpgraded() {
        return false;
    }

    @Override
    public void readFromNBT(final NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        readFromNBTWithoutCoords(tagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeToNBTWithoutCoords(tagCompound);
        return tagCompound;
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity packet) {
        world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        readFromNBT(packet.getNbtCompound());
    }

    // IInventoryProvider
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    // IToolDrop
    @Override
    public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
        return this.getDropWithNBT();
    }

    // IListInfoProvider
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

    // IContainerProvider
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("quantumtank").player(player.inventory).inventory().hotbar()
                .addInventory().tile(this).fluidSlot(0, 80, 17)
                .syncTank(this.tank::getFluid, this.tank::setFluid)
                .outputSlot(1, 80, 53)
                .addInventory()
                .create(this);
    }

    @Nullable
    @Override
    public RebornFluidTank getTank() {
        return tank;
    }
}
