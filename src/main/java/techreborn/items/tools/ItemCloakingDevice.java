package techreborn.items.tools;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import techreborn.items.ItemTR;

public class ItemCloakingDevice extends ItemTR implements IElectricItem
{
	public static int Teir = 3;
	public static int MaxCharge = 100000;
	public static int Limit = 100;
	public static boolean isActive;
	
	public ItemCloakingDevice()
	{
		setUnlocalizedName("techreborn.cloakingdevice");
	}
	
	@Override
	public boolean canProvideEnergy(ItemStack itemStack)
	{
		return false;
	}

	@Override
	public Item getChargedItem(ItemStack itemStack) 
	{
		return this;
	}

	@Override
	public Item getEmptyItem(ItemStack itemStack) 
	{
		return this;
	}

	@Override
	public double getMaxCharge(ItemStack itemStack) 
	{
		return MaxCharge;
	}

	@Override
	public int getTier(ItemStack itemStack) 
	{
		return Teir;
	}

	@Override
	public double getTransferLimit(ItemStack itemStack) 
	{
		return Limit;
	}
}
