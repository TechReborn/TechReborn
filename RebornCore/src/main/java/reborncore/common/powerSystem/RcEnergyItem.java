package reborncore.common.powerSystem;

import team.reborn.energy.api.base.SimpleBatteryItem;

/**
 * Implement on simple energy-containing items and (on top of what SimpleBatteryItem does):
 * <ul>
 *     <li>A tooltip will be added for the item, indicating the stored power, the max power and the extraction rates.</li>
 *     <li>Any RcEnergyItem input in a crafting recipe input will automatically give its energy to the output if the output implements RcEnergyItem.</li>
 * </ul>
 * TODO: consider moving this functionality to the energy API?
 */
public interface RcEnergyItem extends SimpleBatteryItem {
	long getEnergyCapacity();

	/**
	 * @return the tier of this EnergyStorage, used to have standard I/O rates.
	 */
	RcEnergyTier getTier();

	default long getEnergyMaxInput() {
		return getTier().getMaxInput();
	}

	default long getEnergyMaxOutput() {
		return getTier().getMaxOutput();
	}
}
