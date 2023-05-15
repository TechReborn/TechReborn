package techreborn.items.armor;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;

public abstract class TREnergyArmourItem extends TRArmourItem implements RcEnergyItem {
	public final long maxCharge;
	private final RcEnergyTier energyTier;

	public TREnergyArmourItem(ArmorMaterial material, Type slot, long maxCharge, RcEnergyTier energyTier) {
		super(material, slot, new Item.Settings().maxDamage(-1).maxCount(1));
		this.maxCharge = maxCharge;
		this.energyTier = energyTier;
	}

	// ArmorItem
	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}

	// Item
	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return ItemUtils.getColorForDurabilityBar(stack);
	}

	// RcEnergyItem
	@Override
	public long getEnergyCapacity() {
		return maxCharge;
	}

	@Override
	public RcEnergyTier getTier() {
		return energyTier;
	}
}
