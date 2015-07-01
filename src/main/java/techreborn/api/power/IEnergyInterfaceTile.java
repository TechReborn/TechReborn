package techreborn.api.power;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyInterfaceTile {

	/**
	 *
	 * @return Amount of energy in the tile
	 */
	public int getEnergy();

	/**
	 * Sets the energy in the tile
	 *
	 * @param energy the amount of energy to set.
	 */
	public void setEnergy(int energy);

	/**
	 * Gets the max stored energy in the tile
	 * @return The max energy
	 */
	public int getMaxPower();

	/**
	 *
	 * @param energy amount of energy to add to the tile
	 *
	 * @return will return true if can fit all
	 */
	public boolean canAddEnergy(int energy);

	/**
	 *
	 * Will try add add the full amount of energy.
	 *
	 * @param energy amount to add
	 *
	 * @return The amount of energy that was added.
	 */
	public int addEnergy(int energy);

	/**
	 * Returns true if it can use the full amount of energy
	 *
	 * @param energy amount of energy to use from the tile.
	 *
	 * @return if all the energy can be used.
	 */
	public boolean canUseEnergy(int energy);

	/**
	 * Will try and use the full amount of energy
	 *
	 * @param energy energy to use
	 *
	 * @return if the energy was used
	 */
	public boolean useEnergy(int energy);

	/**
	 *
	 * @param direction The direction to insert energy into
	 *
	 * @return if the tile can accept energy from the direction
	 */
	public boolean canAcceptEnergy(ForgeDirection direction);

	/**
	 *
	 * @param direction The direction to provide energy from
	 * @return
	 */
	public boolean canProvideEnergy(ForgeDirection direction);

	/**
	 * Gets the max output, set to -1 if you don't want the tile to provide energy
	 *
	 * @return the max amount of energy outputted per tick.
	 */
	public int getMaxOutput();

	/**
	 *  Return -1 if you don't want to accept power ever.
	 *
	 * @return The max amount of energy that can be added to the tile in one tick.
	 */
	public int getMaxInput();
}
