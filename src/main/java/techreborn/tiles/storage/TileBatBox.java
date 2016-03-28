package techreborn.tiles.storage;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileBatBox extends TilePowerAcceptor implements IWrenchable, ITickable
{

	public Inventory inventory = new Inventory(2, "TileBatBox", 64, this);
	public TileBatBox()
	{
		super(1);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side)
	{
		return true;
	}

	@Override
	public EnumFacing getFacing()
	{
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			return true;
		}
		return false;
	}

	@Override public void updateEntity()
	{
		if(inventory.getStackInSlot(0) != null){
			ItemStack stack = inventory.getStackInSlot(0);
			IEnergyItemInfo item = (IEnergyItemInfo) inventory.getStackInSlot(0).getItem();
			if(PoweredItem.getEnergy(stack) != PoweredItem.getMaxPower(stack)){
				if(canUseEnergy(item.getMaxTransfer(stack)))
				{
					useEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) + item.getMaxTransfer(stack), stack);
				}
			}
		}if(inventory.getStackInSlot(1) != null){
			ItemStack stack = inventory.getStackInSlot(1);
			IEnergyItemInfo item = (IEnergyItemInfo) stack.getItem();
			if(item.canProvideEnergy(stack)){
				if(getEnergy() != getMaxPower())
				{
					addEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) - item.getMaxTransfer(stack), stack);
				}
			}
		}
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.batBox);
	}

	public boolean isComplete()
	{
		return false;
	}

	@Override
	public double getMaxPower()
	{
		return 40000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return getFacingEnum() != direction;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return getFacingEnum() == direction;
	}

	@Override
	public double getMaxOutput()
	{
		return 32;
	}

	@Override
	public double getMaxInput()
	{
		return 32;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		inventory.writeToNBT(tagCompound);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		inventory.readFromNBT(nbttagcompound);
	}
}