package techreborn.items.armor;

import java.util.List;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLithiumBatpack extends ItemArmor implements IElectricItem{

	public static final int maxCharge = ConfigTechReborn.LithiumBatpackCharge;
	public int tier = 3;
	public double transferLimit = 10000;
	    
	public ItemLithiumBatpack(ArmorMaterial armorMaterial, int par3, int par4) 
	{
		super(armorMaterial, par3, par4);
		setMaxStackSize(1);
		setUnlocalizedName("techreborn.lithiumbatpack");
		setCreativeTab(TechRebornCreativeTab.instance);
	}
	
	 @SideOnly(Side.CLIENT)
	 @Override
	 public void registerIcons(IIconRegister iconRegister) 
	 {
		 this.itemIcon = iconRegister.registerIcon("techreborn:" + "lithiumbatpack");
	 }
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) 
	{
		return "techreborn:" + "textures/models/lithiumbatpack.png";
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) 
	{
		ItemStack itemStack = new ItemStack(this, 1);
		if (getChargedItem(itemStack) == this) 
		{
			ItemStack charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, 2147483647, 2147483647, true, false);
			itemList.add(charged);
		}
		if (getEmptyItem(itemStack) == this) 
		{
			itemList.add(new ItemStack(this, 1, getMaxDamage()));
		}
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) 
	{
		return true;
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
		return maxCharge;
	}

	@Override
	public int getTier(ItemStack itemStack) 
	{
		return tier;
	}

	@Override
	public double getTransferLimit(ItemStack itemStack) 
	{
		return transferLimit;
	}

}
