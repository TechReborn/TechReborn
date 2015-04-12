package techreborn.items.tools;

import ic2.api.item.IElectricItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTab;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLithiumBatpack extends ItemArmor implements IElectricItem{

	public int maxCharge = 60000000;
	public int tier = 3;
	public double transferLimit = 10000;
	public int energyPerDamage = 100;
	    
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
