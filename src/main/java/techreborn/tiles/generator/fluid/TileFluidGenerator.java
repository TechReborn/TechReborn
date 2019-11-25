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

package techreborn.tiles.generator.fluid;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.fluids.Fluid;

import reborncore.api.IToolDrop;
import reborncore.api.praescriptum.fuels.Fuel;
import reborncore.api.praescriptum.fuels.FuelHandler;
import reborncore.api.praescriptum.ingredients.input.FluidStackInputIngredient;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.common.fluids.RebornFluidTank;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author estebes
 */
public abstract class TileFluidGenerator extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IContainerProvider {
    // Constructors >>
    public TileFluidGenerator(String name, int maxOutput, int maxEnergy, int tankCapacity, FuelHandler fuelHandler, Block drop) {
        this.name = "Tile" + name;
        this.maxOutput = maxOutput;
        this.maxEnergy = maxEnergy;
        List<Fluid> fluids = fuelHandler.getFuels()
                .stream()
                .flatMap(fuel -> fuel.getInputIngredients()
                        .stream()
                        .filter(ingredient -> ingredient instanceof FluidStackInputIngredient)
                        .map(ingredient -> ((FluidStackInputIngredient) ingredient).ingredient.getFluid()))
                .collect(Collectors.toList());
        this.tank = new RebornFluidTank(this.name, tankCapacity, this, fluids::contains);
        this.inventory = new Inventory(2, name, 64, this);
        this.inputSlots = new int[]{0};
        this.outputSlots = new int[]{1};
        this.fuelHandler = fuelHandler;
        this.drop = drop;

        checkTeir();
    }
    // << Constructors

    // TileEntity >>
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagCompound data = tag.getCompoundTag("TileFluidGenerator");
        remainingEnergy = data.hasKey("remainingEnergy") ? data.getInteger("remainingEnergy") : 0;
        totalEnergy = data.hasKey("totalEnergy") ? data.getInteger("totalEnergy") : 0;
        energyPerTick = data.hasKey("energyPerTick") ? data.getInteger("energyPerTick") : 0;

        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("remainingEnergy", this.remainingEnergy);
        data.setInteger("totalEnergy", this.totalEnergy);
        data.setInteger("energyPerTick", this.energyPerTick);
        tag.setTag("TileFluidGenerator", data);

        tank.writeToNBT(tag);

        return tag;
    }

    @Override
    public void update() {
        super.update();

        if (world.isRemote) return;

        boolean needsInventoryUpdate = false; // To reduce update frequency of inventories, only call onInventory changed if this was set to true

        if (world.getTotalWorldTime() % 10 == 0) {
            if (!inventory.getStackInSlot(0).isEmpty()) {
                boolean containerDrained = FluidUtils.drainContainers(tank, inventory, 0, 1);
                if (containerDrained) {
                    needsInventoryUpdate = true;
                }
            }
        }

        if (remainingEnergy > 0) {
            int amount = Math.min(remainingEnergy, energyPerTick);
            if (canAddEnergy(amount)) {
                addEnergy(amount); // use energy
                remainingEnergy -= amount; // update remaining energy
            }
        } else {
            if (work()) {
                needsInventoryUpdate = true;

                if (!active) setActive(true);
            } else { // operation conditions not satisfied
                if (active) setActive(false);
            }
        }

        if (needsInventoryUpdate) super.markDirty();
    }
    // << TileEntity

    // TilePowerAcceptor >>
    @Override
    public double getBaseMaxPower() {
        return maxEnergy;
    }

    @Override
    public boolean canAcceptEnergy(final EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(final EnumFacing direction) {
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
    // << TilePowerAcceptor

    // TileFluidGenerator >>
    protected boolean work() {
        // if the tank is empty the generator cannot operate
        if (tank.isEmpty() || tank.getFluid() == null) return false;

        // if the energy buffer is full the generator cannot operate
        if (getEnergy() == maxEnergy) return false;

        // try to find a matching fuel and adjust input
        Fuel fuel = fuelHandler.findAndApply2(tank.getFluid(), false);

        if (fuel == null) {
            reset(); // the process could not be completed so reset
            return false;
        }

        // set the fuel value
        remainingEnergy = (int) fuel.getEnergyOutput();
        totalEnergy = (int) fuel.getEnergyOutput();
        energyPerTick = (int) fuel.getEnergyPerTick();

        return true;
    }

    protected void reset() {
        remainingEnergy = 0;
        totalEnergy = 0;
        energyPerTick = 0;
    }
    // << TileFluidGenerator

    // IInventoryProvider >>
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    // << IInventoryProvider

    // IToolDrop >>
    @Override
    public ItemStack getToolDrop(EntityPlayer player) {
        return new ItemStack(drop, 1);
    }
    // << IToolDrop

    // Getters & Setters >>
    @Override
    public RebornFluidTank getTank() {
        return tank;
    }

    public int getRemainingEnergy() {
        return remainingEnergy;
    }

    public void setRemainingEnergy(int remainingEnergy) {
        this.remainingEnergy = remainingEnergy;
    }

    public int getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(int totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public int getRemainingEnergyScaled(int scale) {
        return remainingEnergy == 0 || totalEnergy == 0 ? 0 : (totalEnergy - remainingEnergy) * scale / totalEnergy;
    }
    // << Getters & Setters

    // Fields >>
    public final String name;
    public final int maxOutput;
    public final int maxEnergy;
    public final RebornFluidTank tank;
    public final Inventory inventory;
    public final FuelHandler fuelHandler;
    public final Block drop;

    // Slots
    protected final int[] inputSlots;
    protected final int[] outputSlots;

    protected int remainingEnergy = 0;
    protected int totalEnergy = 0;
    protected int energyPerTick = 0;
    // << Fields
}