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

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.fluids.RebornFluidTank;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;

import techreborn.api.Reference;
import techreborn.api.recipe.ITileRecipeHandler;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileGenericMachine;

import javax.annotation.Nullable;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileIndustrialSawmill extends TileGenericMachine implements IContainerProvider, ITileRecipeHandler<IndustrialSawmillRecipe> {
    // Fields >>
    @ConfigRegistry(config = "machines", category = "industrial_sawmill", key = "IndustrialSawmillMaxInput", comment = "Industrial Sawmill Max Input (Value in EU)")
    public static int maxInput = 128;
    @ConfigRegistry(config = "machines", category = "industrial_sawmill", key = "IndustrialSawmillMaxEnergy", comment = "Industrial Sawmill Max Energy (Value in EU)")
    public static int maxEnergy = 10_000;

    public static final int TANK_CAPACITY = 16_000;
    public RebornFluidTank tank;
    public MultiblockChecker multiblockChecker;
    int ticksSinceLastChange;
    // << Fields

    public TileIndustrialSawmill() {
        super("IndustrialSawmill", maxInput, maxEnergy, ModBlocks.INDUSTRIAL_SAWMILL, 6);
        final int[] inputs = new int[]{0, 1};
        final int[] outputs = new int[]{2, 3, 4};
        this.inventory = new Inventory(7, "TileSawmill", 64, this);
        this.crafter = new RecipeCrafter(Reference.INDUSTRIAL_SAWMILL_RECIPE, this, 1, 3, this.inventory, inputs, outputs);
        this.tank = new RebornFluidTank("TileSawmill", TileIndustrialSawmill.TANK_CAPACITY, this);
    }

    public boolean getMutliBlock() {
        if (multiblockChecker == null) {
            return false;
        }
        final boolean down = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, MultiblockChecker.ZERO_OFFSET);
        final boolean up = multiblockChecker.checkRectY(1, 1, MultiblockChecker.STANDARD_CASING, new BlockPos(0, 2, 0));
        final boolean blade = multiblockChecker.checkRingY(1, 1, MultiblockChecker.REINFORCED_CASING, new BlockPos(0, 1, 0));
        final IBlockState centerBlock = multiblockChecker.getBlock(0, 1, 0);
        final boolean center = ((centerBlock.getBlock() instanceof BlockLiquid
                || centerBlock.getBlock() instanceof IFluidBlock)
                && centerBlock.getMaterial() == Material.WATER);
        return down && center && blade && up;
    }

    // TileGenericMachine
    @Override
    public void update() {
        if (multiblockChecker == null) {
            final BlockPos downCenter = pos.offset(getFacing().getOpposite(), 2).down();
            multiblockChecker = new MultiblockChecker(world, downCenter);
        }

        // Check cells input slot 2 times per second
        if (!world.isRemote && world.getTotalWorldTime() % 10 == 0) {
            if (!inventory.getStackInSlot(1).isEmpty()) {
                FluidUtils.drainContainers(tank, inventory, 1, 5);
                FluidUtils.fillContainers(tank, inventory, 1, 5, tank.getFluidType());
            }
        }

        if (!world.isRemote && getMutliBlock()) super.update();
    }

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

    // RebornMachineTile
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

    // IContainerProvider
    @Override
    public BuiltContainer createContainer(final EntityPlayer player) {
        return new ContainerBuilder("industrialsawmill").player(player.inventory).inventory().hotbar().addInventory()
                .tile(this).fluidSlot(1, 34, 35).slot(0, 84, 43).outputSlot(2, 126, 25).outputSlot(3, 126, 43)
                .outputSlot(4, 126, 61).outputSlot(5, 34, 55).energySlot(6, 8, 72)
                .syncEnergyValue()
                .syncCrafterValue()
                .syncTank(this.tank::getFluid, this.tank::setFluid)
                .addInventory()
                .create(this);
    }

    // ITileRecipeHandler
    @Override
    public boolean canCraft(TileEntity tile, IndustrialSawmillRecipe recipe) {
        if (!getMutliBlock()) {
            return false;
        }
        final FluidStack recipeFluid = recipe.fluidStack;
        final FluidStack tankFluid = tank.getFluid();
        if (recipe.fluidStack == null) {
            return true;
        }
        if (tankFluid == null) {
            return false;
        }
        if (tankFluid.isFluidEqual(recipeFluid)) {
            if (tankFluid.amount >= recipeFluid.amount) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCraft(TileEntity tile, IndustrialSawmillRecipe recipe) {
        final FluidStack recipeFluid = recipe.fluidStack;
        final FluidStack tankFluid = tank.getFluid();
        if (recipe.fluidStack == null) {
            return true;
        }
        if (tankFluid == null) {
            return false;
        }
        if (tankFluid.isFluidEqual(recipeFluid)) {
            if (tankFluid.amount >= recipeFluid.amount) {
                if (tankFluid.amount == recipeFluid.amount) {
                    tank.setFluid(null);
                } else {
                    tankFluid.amount -= recipeFluid.amount;
                }
                syncWithAll();
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public RebornFluidTank getTank() {
        return tank;
    }
}