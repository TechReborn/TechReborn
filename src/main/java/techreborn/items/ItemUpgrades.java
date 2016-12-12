package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import reborncore.common.recipes.RecipeCrafter;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;
import techreborn.utils.upgrade.IMachineUpgrade;

import java.security.InvalidParameterException;
import java.util.List;

public class ItemUpgrades extends ItemTRNoDestroy implements IMachineUpgrade {

	public static final String[] types = new String[] { "overclock", "transformer", "energy_storage" };

	public ItemUpgrades() {
		setUnlocalizedName("techreborn.upgrade");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}

	public static ItemStack getUpgradeByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.upgrades, count, i);
			}
		}
		throw new InvalidParameterException("The upgrade " + name + " could not be found.");
	}

	public static ItemStack getUpgradeByName(String name) {
		return getUpgradeByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, NonNullList list) {
		for (int meta = 0; meta < types.length; ++meta) {
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public void processUpgrade(RecipeCrafter crafter, ItemStack stack) {
		// Remember the max speed multiplier can only be 0.99!!

		if (stack.getItemDamage() == 0) {// Check the meta data here
			crafter.addSpeedMulti(0.2);// This will set the speed multiplier to
			// 0.8
			crafter.addPowerMulti(0.5);// This will use eu/tick x 1.5
			// crafter.addPowerMulti(2); This will use twice the amount of
			// power.
		}
		if (stack.getItemDamage() == 1) {
			crafter.addPowerMulti(-0.2);// This will use eu/tick 0.8
		}
		if (stack.getItemDamage() == 2) {
			crafter.addSpeedMulti(0.5);
			crafter.addPowerMulti(1);
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.wip"));
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.upBroken"));
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.ingredient"));
	}
}