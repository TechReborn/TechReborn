package techreborn.items.tools;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.util.TorchHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemOmniTool extends ItemPickaxe implements IElectricItem{

	public int maxCharge = 20000;
    public int cost = 100;
    public int hitCost = 125;
    
	public ItemOmniTool(ToolMaterial toolMaterial) 
	{
		super(toolMaterial);
		efficiencyOnProperMaterial = 13F;
        setCreativeTab(TechRebornCreativeTab.instance);
        setMaxStackSize(1);
        setMaxDamage(200);
        setUnlocalizedName("techreborn.omniTool");
	}
	
	  @SideOnly(Side.CLIENT)
	  @Override
	  public void registerIcons(IIconRegister iconRegister) 
	  {
		  this.itemIcon = iconRegister.registerIcon("techreborn:" + "omnitool");
	  }

	    @SuppressWarnings({"rawtypes", "unchecked"})
	    @SideOnly(Side.CLIENT)
	    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) 
	    {
	    	ItemStack itemStack = new ItemStack(this, 1);
	    	if (getChargedItem(itemStack) == this) 
	    	{
	    		ItemStack charged = new ItemStack(this, 1);
	    		ElectricItem.manager.charge(charged, 2147483647, 2147483647, true,false);
	    		itemList.add(charged);
	    	}
	    	if (getEmptyItem(itemStack) == this) 
	    	{
	    		itemList.add(new ItemStack(this, 1, getMaxDamage()));
	    	}
	    }

	    @Override
	    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int par4, int par5, int par6, EntityLivingBase entityLiving)
	    {	        
	        ElectricItem.manager.use(stack, cost, entityLiving);
	        return true;
	    }

	    @Override
	    public boolean canHarvestBlock(Block block, ItemStack stack) 
	    {
	        return Items.diamond_axe.canHarvestBlock(block, stack) || Items.diamond_sword.canHarvestBlock(block, stack) || Items.diamond_pickaxe.canHarvestBlock(block, stack) || Items.diamond_shovel.canHarvestBlock(block, stack) || Items.shears.canHarvestBlock(block, stack);
	    }

	    @Override
	    public float getDigSpeed(ItemStack stack, Block block, int meta) 
	    {
	        if (!ElectricItem.manager.canUse(stack, cost)) 
	        {
	            return 5.0F;
	        }

	        if (Items.wooden_axe.getDigSpeed(stack, block, meta) > 1.0F || Items.wooden_sword.getDigSpeed(stack, block, meta) > 1.0F || Items.wooden_pickaxe.getDigSpeed(stack, block, meta) > 1.0F || Items.wooden_shovel.getDigSpeed(stack, block, meta) > 1.0F || Items.shears.getDigSpeed(stack, block, meta) > 1.0F) 
	        {
	            return efficiencyOnProperMaterial;
	        } else {
	            return super.getDigSpeed(stack, block, meta);
	        }
	    }

	    @Override
	    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker) 
	    {
	        if (ElectricItem.manager.use(itemstack, hitCost, attacker)) 
	        {
	            entityliving.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 8F);
	        }
	        return false;
	    }

	    @Override
	    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) 
	    {
	        return TorchHelper.placeTorch(stack, player, world, x, y, z, side, xOffset, yOffset, zOffset);
	    }

	    @Override
	    public boolean isRepairable() 
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
	    public boolean canProvideEnergy(ItemStack itemStack) 
	    {
	        return false;
	    }

	    @Override
	    public double getMaxCharge(ItemStack itemStack) 
	    {
	        return maxCharge;
	    }

	    @Override
	    public int getTier(ItemStack itemStack) 
	    {
	        return 2;
	    }

	    @Override
	    public double getTransferLimit(ItemStack itemStack) 
	    {
	        return 200;
	    }

}
