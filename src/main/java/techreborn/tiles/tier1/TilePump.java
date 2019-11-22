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

package techreborn.tiles.tier1;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.fluids.RebornFluidTank;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;

import techreborn.lib.ModInfo;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author estebes, modmuss50
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
public class TilePump extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IContainerProvider {
    // Config >>
    @ConfigRegistry(config = "machines", category = "pump", key = "PumpInput", comment = "Pump max input (Value in EU)")
    public static int maxInput = 32;

    @ConfigRegistry(config = "machines", category = "pump", key = "PumpMaxEnergy", comment = "Pump max energy (Value in EU)")
    public static int maxEnergy = 1_000;

    @ConfigRegistry(config = "machines", category = "pump", key = "PumpEUCost", comment = "Pump cost for one block of fluid (Value in EU)")
    public static int pumpExtractEU = 20;
    // << Config

    public TilePump() {
        super();
        this.inventory = new Inventory(3, "TilePump", 64, this);
        this.tank = new RebornFluidTank("TilePump", TANK_CAPACITY, this);
    }

    @Override
    public void update() {
        super.update();

        if (world.isRemote) return;

        if (world.getTotalWorldTime() % 10 == 0) {
            // handle fluid containers
            if (FluidUtils.fillContainers(tank, inventory, 0, 1, tank.getFluidType())) {
                this.syncWithAll();
            }

            if (!tank.isFull() && tank.getCapacity() - tank.getFluidAmount() >= 1000 && canUseEnergy(pumpExtractEU)) {
                useEnergy(pumpExtractEU);
            }
        }
    }

    @Override
    public void addInfo(List<String> info, boolean isRealTile) {
        super.addInfo(info, isRealTile);
        info.add(TextFormatting.LIGHT_PURPLE + "Eu per extract " + TextFormatting.GREEN
                + PowerSystem.getLocaliszedPower(pumpExtractEU));
        info.add(TextFormatting.LIGHT_PURPLE + "Speed: " + TextFormatting.GREEN
                + "1000mb/5 sec");
    }

    public static FluidStack drainBlock(World world, BlockPos pos, boolean doDrain) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

        if (fluid != null && FluidRegistry.isFluidRegistered(fluid)) {
            if (block instanceof IFluidBlock) {
                IFluidBlock fluidBlock = (IFluidBlock) block;
                if (!fluidBlock.canDrain(world, pos)) {
                    return null;
                }
                return fluidBlock.drain(world, pos, doDrain);
            } else {
                //Checks if source
                int level = state.getValue(BlockLiquid.LEVEL);
                if (level != 0) {
                    return null;
                }
                if (doDrain) {
                    world.setBlockToAir(pos);
                }
                return new FluidStack(fluid, 1000);
            }
        } else {
            return null;
        }
    }

    // NBT >>
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
    // << NBT

    //
    @Override
    public double getBaseMaxPower() {
        return maxEnergy;
    }

    @Override
    public boolean canAcceptEnergy(final EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(final EnumFacing direction) {
        return false;
    }

    @Override
    public double getBaseMaxOutput() {
        return 0;
    }

    @Override
    public double getBaseMaxInput() {
        return maxInput;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    // Inventory >>
    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
        if (slotIndex == 1) {
            if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                return true;
            } else {
                return false;
            }
        }
        return super.isItemValidForSlot(slotIndex, itemStack);
    }
    // << Inventory

    // IInventoryProvider >>
    @Override
    public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
//		return new ItemStack(ModBlocks.PUMP, 1);
        return ItemStack.EMPTY;
    }
    // << IInventoryProvider


    // IContainerProvider >>
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("pump").player(player.inventory).inventory().hotbar().addInventory().tile(this)
                .fluidSlot(0, 124, 35).outputSlot(1, 124, 55).energySlot(2, 8, 72)
                .syncEnergyValue().addInventory().create(this);
    }
    // << IContainerProvider


    // Tank >>
    @Nullable
    @Override
    public RebornFluidTank getTank() {
        return tank;
    }
    // << Tank

    // Fields >>
    public static final int TANK_CAPACITY = 8_000;

    public final Inventory inventory;
    public final RebornFluidTank tank;
    // << Fields
}
