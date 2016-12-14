package techreborn.items.tools;

import ic2.core.item.tool.ItemTreetap;
import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;
import techreborn.items.ItemTRNoDestroy;
import techreborn.compat.CompatManager;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 05/11/2016.
 */
public class ItemElectricTreetap extends ItemTRNoDestroy implements IEnergyItemInfo {

	public static final int maxCharge = 5120;
	public static final int tier = 1;
	public int cost = 20;

	public ItemElectricTreetap() {
		setUnlocalizedName("techreborn.electric_treetap");
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if(CompatManager.isIC2Loaded && block == Block.getBlockFromName("ic2:rubber_wood") && PoweredItem.canUseEnergy(20, playerIn.getHeldItem(hand)))
			if (ItemTreetap.attemptExtract(playerIn, worldIn, pos, side, state, null) && !worldIn.isRemote) {
				PoweredItem.useEnergy(20, playerIn.getHeldItem(hand));
				return EnumActionResult.SUCCESS;
			}
		return EnumActionResult.PASS;
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
	public boolean canProvideEnergy(ItemStack stack) {
		return false;
	}

	@Override
	public double getMaxTransfer(ItemStack stack) {
		return 200;
	}

	@Override
	public int getStackTier(ItemStack stack) {
		return tier;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
		return 1 - charge;

	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item,
	                        CreativeTabs par2CreativeTabs, NonNullList itemList) {
		ItemStack uncharged = new ItemStack(ModItems.electricTreetap);
		ItemStack charged = new ItemStack(ModItems.electricTreetap);
		PoweredItem.setEnergy(getMaxPower(charged), charged);

		itemList.add(uncharged);
		itemList.add(charged);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}
}