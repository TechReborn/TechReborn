package techreborn.tiles.transformers;

import reborncore.common.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.blocks.transformers.BlockTransformer;

/**
 * Created by Rushmead
 */
public class TileTransformer extends TilePowerAcceptor implements IWrenchable, ITickable
{

	public String name;
	public Block wrenchDrop;
	public EnumPowerTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

	public TileTransformer(String name, Block wrenchDrop, EnumPowerTier tier, int maxInput, int maxOuput, int maxStorage)
	{
		super(1);
		this.wrenchDrop = wrenchDrop;
		this.tier = tier;
		this.name = name;
		this.maxInput = maxInput;
		this.maxOutput = maxOuput;
		this.maxStorage = maxStorage;
	}

	@Override public boolean wrenchCanSetFacing(EntityPlayer p0, EnumFacing p1)
	{
		return true;
	}

	@Override public EnumFacing getFacing()
	{
		return getFacingEnum();
	}

	@Override public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return entityPlayer.isSneaking();
	}

	@Override public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override public ItemStack getWrenchDrop(EntityPlayer p0)
	{
		return new ItemStack(wrenchDrop);
	}

	@Override public double getMaxPower()
	{
		return maxStorage;
	}

	@Override public boolean canAcceptEnergy(EnumFacing direction)
	{

		return getFacingEnum() != direction;
	}

	@Override public EnumFacing getFacingEnum()
	{
		Block block = worldObj.getBlockState(pos).getBlock();
		if (block instanceof BlockTransformer)
		{
			return ((BlockTransformer) block).getFacing(worldObj.getBlockState(pos));
		}
		return null;
	}

	@Override public boolean canProvideEnergy(EnumFacing direction)
	{
		return getFacing() == direction;
	}


	@Override public double getMaxOutput()
	{
		return maxOutput;
	}

	@Override public double getMaxInput()
	{
		return maxInput;
	}

	@Override public EnumPowerTier getTier()
	{
		return tier;
	}
}
