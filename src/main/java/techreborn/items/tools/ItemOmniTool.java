package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.TorchHelper;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModItems;

import java.util.List;

public class ItemOmniTool extends ItemPickaxe implements IEnergyItemInfo {

	public static final int maxCharge = ConfigTechReborn.OmniToolCharge;
	public static final int tier = ConfigTechReborn.OmniToolTier;
	public int cost = 100;
	public int hitCost = 125;

	public ItemOmniTool() {
		super(ToolMaterial.DIAMOND);
		efficiencyOnProperMaterial = 13F;
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
		setMaxDamage(200);
		setUnlocalizedName("techreborn.omniTool");
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos,
	                                EntityLivingBase entityLiving) {
		PoweredItem.useEnergy(cost, stack);
		return true;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return Items.DIAMOND_AXE.canHarvestBlock(state) || Items.DIAMOND_SWORD.canHarvestBlock(state)
			|| Items.DIAMOND_PICKAXE.canHarvestBlock(state) || Items.DIAMOND_SHOVEL.canHarvestBlock(state)
			|| Items.SHEARS.canHarvestBlock(state);
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
	public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase attacker) {
		if (PoweredItem.canUseEnergy(hitCost, itemstack)) {
			PoweredItem.useEnergy(hitCost, itemstack);
			entityliving.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 8F);
		}
		return false;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos,
	                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return TorchHelper.placeTorch(playerIn.getHeldItem(hand), playerIn, worldIn, pos, facing, hitX, hitY, hitZ, hand);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public double getMaxPower(ItemStack stack) {
		return maxCharge;
	}

	@Override
	public boolean canAcceptEnergy(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return 200;
	}

	@Override
	public int getStackTier(ItemStack stack) {
		return 2;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, NonNullList itemList) {
		ItemStack uncharged = new ItemStack(ModItems.omniTool);
		ItemStack charged = new ItemStack(ModItems.omniTool);
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;

	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.RED + "WIP Coming Soon");
	}
}
