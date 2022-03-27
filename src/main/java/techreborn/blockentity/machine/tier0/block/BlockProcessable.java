package techreborn.blockentity.machine.tier0.block;

import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.RedstoneConfigurable;
import reborncore.common.recipes.IUpgradeHandler;

/**
 * <b>Aggregated interface for use with BlockEntities that have a processor attached</b>
 * <br>
 * Example of a processor {@link techreborn.blockentity.machine.tier0.block.blockplacer.BlockPlacerProcessor}
 *
 * @author SimonFlapse
 */
public interface BlockProcessable extends IUpgradeHandler, RedstoneConfigurable, InventoryProvider {
	/**
	 * <b>Consume some amount of Energy</b>
	 * <br>
	 * Must only return true if it successfully consumed energy.
	 * Must not consume energy if only partial fulfilled
	 *
	 * @param amount {@code int} amount of energy to consume
	 * @return if the energy could be consumed
	 */
	boolean consumeEnergy(int amount);


	/**
	 * <b>Play a sound to the Minecraft world</b>
	 */
	void playSound();
}