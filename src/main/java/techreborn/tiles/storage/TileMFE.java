package techreborn.tiles.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileMFE extends TileBatBox
{

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.mfe);
	}

	@Override
	public double getMaxPower()
	{
		return 4000000;
	}

	@Override
	public double getMaxOutput()
	{
		return 512;
	}

	@Override
	public double getMaxInput()
	{
		return 512;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.MEDIUM;
	}
}
