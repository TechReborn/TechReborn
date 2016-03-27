package techreborn.tiles.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.init.ModBlocks;
import ic2.api.tile.IWrenchable;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileBatBox extends TilePowerAcceptor implements IWrenchable
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