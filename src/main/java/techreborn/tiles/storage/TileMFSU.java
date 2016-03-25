package techreborn.tiles.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileMFSU extends TileBatBox
{

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.mfsu);
	}

	@Override
	public double getMaxPower()
	{
		return 40000000;
	}

	@Override
	public double getMaxOutput()
	{
		return 2048;
	}

	@Override
	public double getMaxInput()
	{
		return 2048;
	}

	@Override
	public EnumPowerTier getTier()
	{
		return EnumPowerTier.HIGH;
	}
}
