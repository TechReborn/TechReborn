package techreborn.items.tools;

import java.util.List;

import me.modmuss50.jsonDestroyer.api.IHandHeld;
import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.ItemNBTHelper;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;

import net.minecraft.item.Item.ToolMaterial;
public class ItemNanosaber extends ItemSword implements IEnergyItemInfo, ITexturedItem, IHandHeld
{

	public static int tier = 1;
	public int maxCharge = 1;
	public int cost = 250;
	public float unpoweredSpeed = 2.0F;
	public double transferLimit = 100;

	public ItemNanosaber()
	{
		super(ToolMaterial.DIAMOND);
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(240);
		setUnlocalizedName("techreborn.nanosaber");
		RebornCore.jsonDestroyer.registerObject(this);
		this.maxCharge = 1000;
		this.tier = 2;
	}

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
	{
		PoweredItem.useEnergy(cost, itemstack);
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player, EnumHand hand)
	{
		if (player.isSneaking())
		{
			changeMode(stack);
		}
		return new ActionResult<>(EnumActionResult.FAIL, stack);
	}

	public void changeMode(ItemStack stack)
	{
		if (!ItemNBTHelper.verifyExistance(stack, "isActive"))
		{
			ItemNBTHelper.setBoolean(stack, "isActive", true);
		} else if (ItemNBTHelper.verifyExistance(stack, "isActive"))
		{
			stack.getTagCompound().removeTag("isActive");
		}
	}

	public boolean isItemActive(ItemStack stack)
	{
		return !ItemNBTHelper.verifyExistance(stack, "isActive");
	}

//	@SideOnly(Side.CLIENT)
//	@Override
//	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
//	{
//		if (ItemNBTHelper.verifyExistance(stack, "isActive"))
//		{
//			list.add("Active");
//		} else if (!ItemNBTHelper.verifyExistance(stack, "isActive"))
//		{
//			list.add("Not Active");
//		}
//		super.addInformation(stack, player, list, par4);
//	}

	@Override
	public boolean isRepairable()
	{
		return false;
	}

	@Override
	public double getMaxPower(ItemStack stack)
	{
		return maxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack stack)
	{
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack)
	{
		return transferLimit;
	}

	@Override
	public int getStackTier(ItemStack stack)
	{
		return tier;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList)
	{
		ItemStack itemStack = new ItemStack(this, 1);
		itemList.add(itemStack);

		ItemStack charged = new ItemStack(this, 1);
		PoweredItem.setEnergy(getMaxPower(charged), charged);
		itemList.add(charged);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;

	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public String getTextureName(int damage)
	{
		if (damage == 1)
		{
			return "techreborn:items/tool/nanosaber_on";
		}
		return "techreborn:items/tool/nanosaber_off";
	}

	@Override
	public int getMaxMeta()
	{
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		tooltip.add(TextFormatting.RED + "WIP Coming Soon");
	}
}
