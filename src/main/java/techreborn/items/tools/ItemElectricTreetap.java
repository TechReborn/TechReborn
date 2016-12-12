package techreborn.items.tools;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;
import techreborn.items.ItemTRNoDestroy;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 05/11/2016.
 */
public class ItemElectricTreetap extends ItemTRNoDestroy implements IEnergyItemInfo {

	public static final int maxCharge = 5120;
	public static final int tier = 1;
	public int cost = 20;

	public ItemElectricTreetap() {
		super();
		setUnlocalizedName("techreborn.electric_treetap");
		setCreativeTab(TechRebornCreativeTab.instance);
		setMaxStackSize(1);
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