package techreborn.tiles.generator;

import reborncore.common.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

public class TileHeatGenerator extends TilePowerAcceptor implements IWrenchable
{

	public static final int euTick = ConfigTechReborn.HeatGeneratorOutput;

	public TileHeatGenerator()
	{
		super(1);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!worldObj.isRemote)
		{
			if (worldObj.getBlockState(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ()))
					.getBlock() == Blocks.LAVA)
			{
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() + 1))
					.getBlock() == Blocks.LAVA)
			{
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() - 1))
					.getBlock() == Blocks.LAVA)
			{
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ()))
					.getBlock() == Blocks.LAVA)
			{
				addEnergy(euTick);
			} else if (worldObj.getBlockState(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ()))
					.getBlock() == Blocks.LAVA)
			{
				addEnergy(euTick);
			}

		}
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side)
	{
		return false;
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
		return new ItemStack(ModBlocks.heatGenerator, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

	@Override
	public double getMaxPower()
	{
		return 10000;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction)
	{
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction)
	{
		return true;
	}

	@Override
	public double getMaxOutput()
	{
		return 64;
	}

	@Override
	public double getMaxInput()
	{
		return 0;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.LOW;
	}

	// @Override
	// public void addWailaInfo(List<String> info)
	// {
	// super.addWailaInfo(info);
	// info.add("Power Generarating " + euTick +" EU/t");
	//
	// }

}
