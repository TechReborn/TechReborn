package techreborn.tiles;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileAesu extends TilePowerAcceptor implements IWrenchable
{

	public static final int MAX_OUTPUT = ConfigTechReborn.AesuMaxOutput;
	public static final int MAX_STORAGE = ConfigTechReborn.AesuMaxStorage;
	public Inventory inventory = new Inventory(4, "TileAesu", 64, this);
	private int OUTPUT = 64; // The current output
	private double euLastTick = 0;
	private double euChange;
	private int ticks;

	public TileAesu()
	{
		super(5);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (ticks == ConfigTechReborn.AverageEuOutTickTime)
		{
			euChange = -1;
			ticks = 0;

		} else
		{
			ticks++;
			euChange += getEnergy() - euLastTick;
			if (euLastTick == getEnergy())
			{
				euChange = 0;
			}
		}

		euLastTick = getEnergy();
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
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return getDropWithNBT();
	}

	public boolean isComplete()
	{
		return false;
	}

	public void handleGuiInputFromClient(int id)
	{
		if (id == 0)
		{
			OUTPUT += 256;
		}
		if (id == 1)
		{
			OUTPUT += 64;
		}
		if (id == 2)
		{
			OUTPUT -= 64;
		}
		if (id == 3)
		{
			OUTPUT -= 256;
		}
		if (OUTPUT > MAX_OUTPUT)
		{
			OUTPUT = MAX_OUTPUT;
		}
		if (OUTPUT <= -1)
		{
			OUTPUT = 0;
		}
	}

	public double getEuChange()
	{
		if (euChange == -1)
		{
			return -1;
		}
		return (euChange / ticks);
	}

	public ItemStack getDropWithNBT()
	{
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.Aesu, 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setDouble("euChange", euChange);
		tagCompound.setDouble("euLastTick", euLastTick);
		tagCompound.setInteger("output", OUTPUT);
		inventory.writeToNBT(tagCompound);
	}

	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		this.euChange = nbttagcompound.getDouble("euChange");
		this.euLastTick = nbttagcompound.getDouble("euLastTick");
		this.OUTPUT = nbttagcompound.getInteger("output");
		inventory.readFromNBT(nbttagcompound);
	}

	@Override
	public double getMaxPower()
	{
		return TileAesu.MAX_STORAGE;
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
		return OUTPUT;
	}

	@Override
	public double getMaxInput()
	{
		return 4096 * 2;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.EXTREME;
	}
}
