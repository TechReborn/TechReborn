package techreborn.items.tools;

import java.util.List;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.TorchHelper;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.lib.ModInfo;

public class ItemOmniTool extends ItemPickaxe implements IEnergyItemInfo, ITexturedItem
{

	public static final int maxCharge = ConfigTechReborn.OmniToolCharge;
	public static final int tier = ConfigTechReborn.OmniToolTier;
	public int cost = 100;
	public int hitCost = 125;

	public ItemOmniTool()
	{
		super(ToolMaterial.DIAMOND);
		efficiencyOnProperMaterial = 13F;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(200);
		setUnlocalizedName("techreborn.omniTool");
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos,
			EntityLivingBase entityLiving)
	{
		PoweredItem.useEnergy(cost, stack);
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state)
	{
		return Items.diamond_axe.canHarvestBlock(state) || Items.diamond_sword.canHarvestBlock(state)
				|| Items.diamond_pickaxe.canHarvestBlock(state) || Items.diamond_shovel.canHarvestBlock(state)
				|| Items.shears.canHarvestBlock(state);
	}

	// @Override
	// public float getDigSpeed(ItemStack stack, IBlockState state) {
	// if (PoweredItem.canUseEnergy(cost, stack)) {
	// PoweredItem.useEnergy(cost, stack);
	// return 5.0F;
	// }
	//
	// if (Items.wooden_axe.getDigSpeed(stack, state) > 1.0F
	// || Items.wooden_sword.getDigSpeed(stack, state) > 1.0F
	// || Items.wooden_pickaxe.getDigSpeed(stack, state) > 1.0F
	// || Items.wooden_shovel.getDigSpeed(stack, state) > 1.0F
	// || Items.shears.getDigSpeed(stack, state) > 1.0F) {
	// return efficiencyOnProperMaterial;
	// } else {
	// return super.getDigSpeed(stack, state);
	// }
	// }

	@Override
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker)
	{
		if (PoweredItem.canUseEnergy(hitCost, itemstack))
		{
			PoweredItem.useEnergy(hitCost, itemstack);
			entityliving.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 8F);
		}
		return false;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return TorchHelper.placeTorch(stack, playerIn, worldIn, pos, facing, hitX, hitY, hitZ, hand);
	}

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
	public boolean canProvideEnergy(ItemStack itemStack)
	{
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack)
	{
		return 200;
	}

	@Override
	public int getStackTier(ItemStack stack)
	{
		return 2;
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
		return "techreborn:items/tool/omnitool";
	}

	@Override
	public int getMaxMeta()
	{
		return 1;
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
