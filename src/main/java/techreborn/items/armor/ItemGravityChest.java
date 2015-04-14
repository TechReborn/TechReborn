package techreborn.items.armor;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGravityChest extends ItemArmor implements IElectricItem, ISpecialArmor{
	
	public static int maxCharge = ConfigTechReborn.GravityCharge;
    public int tier = 3;
    public int cost = 100;
    public double transferLimit = 1000;
    public int energyPerDamage = 100;

	public ItemGravityChest(ArmorMaterial material, int par3, int par4) 
	{
		super(material, par3, par4);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.gravity");
		setMaxStackSize(1);
		setMaxDamage(120);
//		isDamageable();
	}
	
	 @SideOnly(Side.CLIENT)
	 @Override
	 public void registerIcons(IIconRegister iconRegister) 
	 {
		 this.itemIcon = iconRegister.registerIcon("techreborn:" + "items/gravity");
	 }
	 
	 @Override
	 @SideOnly(Side.CLIENT)
	 public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) 
	 {
		 return "techreborn:" + "textures/models/gravity.png";
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
	  public void onArmorTick(World world, EntityPlayer player, ItemStack stack) 
	  {
		  if (world.isRemote);
		  		if (ElectricItem.manager.canUse(stack, cost))
		  		{	
		  			player.capabilities.allowFlying = true;
			       
		  			if (player.fallDistance > 0.0F) 
			        		player.fallDistance = 0;
		  			
		  			if(player.capabilities.allowFlying == true & !player.onGround)
		  				ElectricItem.manager.discharge(stack, cost, tier, false, true, false);
		  			
		  			if(!ElectricItem.manager.canUse(stack, cost))
		  				player.capabilities.allowFlying = false;
		  		}	
		  		
	        if (player.fallDistance > 0.0F) player.fallDistance = 0;
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

    public int getEnergyPerDamage() 
    {
        return energyPerDamage;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) 
    {
        if (source.isUnblockable()) 
        {
            return new net.minecraftforge.common.ISpecialArmor.ArmorProperties(0, 0.0D, 3);
        } else {
            double absorptionRatio = getBaseAbsorptionRatio() * getDamageAbsorptionRatio();
            int energyPerDamage = getEnergyPerDamage();
            double damageLimit = energyPerDamage <= 0 ? 0 : (25 * ElectricItem.manager.getCharge(armor)) / energyPerDamage;
            return new net.minecraftforge.common.ISpecialArmor.ArmorProperties(3, absorptionRatio, (int) damageLimit);
        }
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) 
    {
        if (ElectricItem.manager.getCharge(armor) >= getEnergyPerDamage()) 
        {
            return (int) Math.round(20D * getBaseAbsorptionRatio() * getDamageAbsorptionRatio());
        } else {
            return 0;
        }
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) 
    {
        ElectricItem.manager.discharge(stack, damage * getEnergyPerDamage(), 0x7fffffff, true, false, false);
    }

    public double getDamageAbsorptionRatio() 
    {
        return 1.1000000000000001D;
    }

    private double getBaseAbsorptionRatio() 
    {
        return 0.14999999999999999D;
    }

}
