package techreborn.tiles;

import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import reborncore.api.IListInfoProvider;
import reborncore.common.misc.Location;
import reborncore.common.util.FluidUtils;
import reborncore.common.util.Inventory;
import reborncore.common.util.Tank;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;
import techreborn.init.ModFluids;
import techreborn.lib.Reference;
import techreborn.powerSystem.TilePowerAcceptor;

import java.util.List;

public class TileIndustrialSawmill extends TilePowerAcceptor implements IWrenchable, IEnergyTile, IFluidHandler, IInventory, ISidedInventory, IListInfoProvider {

    public int tickTime;
    public Inventory inventory = new Inventory(5, "TileIndustrialSawmill", 64);
    public Tank tank = new Tank("TileSawmill", 16000, this);
    public RecipeCrafter crafter;

    public TileIndustrialSawmill() {
        super(2);
        //TODO configs
        //Input slots
        int[] inputs = new int[2];
        inputs[0] = 0;
        inputs[1] = 1;
        int[] outputs = new int[3];
        outputs[0] = 2;
        outputs[1] = 3;
        outputs[2] = 4;
        crafter = new RecipeCrafter(Reference.industrialSawmillRecipe, this, 2, 3, inventory, inputs, outputs);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (getMutliBlock()) {
            crafter.updateEntity();
        }
        FluidUtils.drainContainers(this, inventory, 0, 4);
        FluidUtils.drainContainers(this, inventory, 1, 4);
    }

    public boolean getMutliBlock() {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (tileEntity instanceof TileMachineCasing) {
                if ((tileEntity.getBlockType() instanceof BlockMachineCasing)) {
                    int heat;
                    heat = BlockMachineCasing.getHeatFromMeta(tileEntity.getBlockMetadata());
                    Location location = new Location(xCoord, yCoord, zCoord, direction);
                    location.modifyPositionFromSide(direction, 1);
                    if (worldObj.getBlock(location.getX(), location.getY(), location.getZ()).getUnlocalizedName().equals("tile.lava")) {
                        heat += 500;
                    }
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        return false;
    }

    @Override
    public short getFacing() {
        return 0;
    }

    @Override
    public void setFacing(short facing) {
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        if (entityPlayer.isSneaking()) {
            return true;
        }
        return false;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModBlocks.industrialSawmill, 1);
    }

    public boolean isComplete() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
        tank.readFromNBT(tagCompound);
        crafter.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
        tank.writeToNBT(tagCompound);
        crafter.writeToNBT(tagCompound);
    }

    /* IFluidHandler */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.getFluid() == FluidRegistry.WATER || resource.getFluid() == ModFluids.fluidMercury || resource.getFluid() == ModFluids.fluidSodiumpersulfate) {
            int filled = tank.fill(resource, doFill);
            tank.compareAndUpdate();
            return filled;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
            return null;
        }
        FluidStack fluidStack = tank.drain(resource.amount, doDrain);
        tank.compareAndUpdate();
        return fluidStack;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        FluidStack drained = tank.drain(maxDrain, doDrain);
        tank.compareAndUpdate();
        return drained;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        if (fluid == FluidRegistry.WATER) {
            return true;
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {
        inventory.openInventory();
    }

    @Override
    public void closeInventory() {
        inventory.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    // ISidedInventory
    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return side == ForgeDirection.DOWN.ordinal() ? new int[]{0, 1, 2, 3, 4} : new int[]{0, 1, 2, 3, 4};
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side) {
        if (slotIndex >= 2)
            return false;
        return isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
        return slotIndex == 2 || slotIndex == 3 || slotIndex == 4;
    }

    public int getProgressScaled(int scale) {
        if (crafter.currentTickTime != 0) {
            return crafter.currentTickTime * scale / crafter.currentNeededTicks;
        }
        return 0;
    }

    @Override
    public double getMaxPower() {
        return 10000;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return false;
    }

    @Override
    public double getMaxOutput() {
        return 0;
    }

    @Override
    public double getMaxInput() {
        return 64;
    }
}
