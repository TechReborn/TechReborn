package techreborn.tiles.generator;

import ic2.api.tile.IWrenchable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;

public class TileGenerator extends TilePowerAcceptor implements IWrenchable, IInventory
{
    public Inventory inventory = new Inventory(2, "TileGenerator", 64, this);
    
    public int fuelSlot = 0;
    public int burnTime;
    public int totalBurnTime = 0;
    public static int outputAmount = 40; //This is in line with BC engines rf, sould properly use the conversion ratio here.
    public boolean isBurning;
    public boolean lastTickBurning;
    ItemStack burnItem;

	public TileGenerator(){
		super(1);
	}

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj.isRemote){
            return;
        }
        if(getEnergy() < getMaxPower()) {
	        if (burnTime > 0){
	            burnTime--;
	            addEnergy(outputAmount);
	            isBurning = true;
	        }
        } else {
            isBurning = false;
        }

        if (burnTime == 0) {
            updateState();
            burnTime = totalBurnTime = getItemBurnTime(getStackInSlot(fuelSlot));
            if (burnTime > 0) {
                updateState();
                burnItem = getStackInSlot(fuelSlot);
                if(getStackInSlot(fuelSlot).stackSize == 1){
                    setInventorySlotContents(fuelSlot, null);
                } else {
                    decrStackSize(fuelSlot, 1);
                }
            }
        }

        lastTickBurning = isBurning;
    }

    public void updateState(){
        IBlockState BlockStateContainer = worldObj.getBlockState(pos);
        if(BlockStateContainer.getBlock() instanceof BlockMachineBase){
            BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
            if(BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != burnTime > 0)
                blockMachineBase.setActive(burnTime > 0, worldObj, pos);
        }
    }

    public static int getItemBurnTime(ItemStack stack) {
        return TileEntityFurnace.getItemBurnTime(stack);
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
        return false;
    }

    @Override
    public EnumFacing getFacing() {
        return getFacingEnum();
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
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return inventory.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return inventory.decrStackSize(p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack removeStackFromSlot(int p_70304_1_) {
        return inventory.removeStackFromSlot(p_70304_1_);
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        inventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return inventory.isUseableByPlayer(p_70300_1_);
    }


    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return inventory.isItemValidForSlot(p_94041_1_, p_94041_2_);
    }

    @Override
    public double getMaxPower() {
        return 10000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public double getMaxOutput() {
        return 64;
    }

    @Override
    public double getMaxInput() {
        return 0;
    }

    @Override
    public EnumPowerTier getTier() {
        return EnumPowerTier.LOW;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        inventory.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inventory.closeInventory(player);
    }


    @Override
    public int getField(int id) {
        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inventory.getFieldCount();
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inventory.hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return inventory.getDisplayName();
    }

	@Override
	public ItemStack getWrenchDrop(EntityPlayer p0) {
		return new ItemStack(ModBlocks.Generator);
	}
	
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.writeToNBT(tagCompound);
    }
}
