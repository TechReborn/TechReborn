package reborncore.common.powerSystem;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleBatteryItem;

/**
 * Implement on simple energy-containing items and (on top of what {@link SimpleBatteryItem} does):
 * <ul>
 *     <li>A tooltip will be added for the item, indicating the stored power,
 *     the max power and the extraction rates.</li>
 *     <li>Any {@link RcEnergyItem} input in a crafting recipe input will automatically
 *     give its energy to the output if the output implements {@link RcEnergyItem}.</li>
 * </ul>
 * TODO: consider moving this functionality to the energy API?
 */
public interface RcEnergyItem extends SimpleBatteryItem, FabricItem {
	long getEnergyCapacity();

	/**
	 * @return {@link RcEnergyTier} the tier of this {@link EnergyStorage}, used to have standard I/O rates.
	 */
	RcEnergyTier getTier();

	default long getEnergyMaxInput() {
		return getTier().getMaxInput();
	}

	default long getEnergyMaxOutput() {
		return getTier().getMaxOutput();
	}

	@Override
	default boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return !ItemUtils.isEqualIgnoreEnergy(oldStack, newStack);
	}

	@Override
	default boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
		return ItemUtils.isEqualIgnoreEnergy(oldStack, newStack);
	}
}
