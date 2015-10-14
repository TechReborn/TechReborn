package techreborn.api.power;

import net.minecraft.item.ItemStack;

public interface IEnergyInterfaceItem {
    /**
     * @return Amount of energy in the tile
     */
    public double getEnergy(ItemStack stack);

    /**
     * Sets the energy in the tile
     *
     * @param energy the amount of energy to set.
     */
    public void setEnergy(double energy, ItemStack stack);

    /**
     * Gets the max stored energy in the tile
     *
     * @return The max energy
     */
    public double getMaxPower(ItemStack stack);

    /**
     * @param energy amount of energy to add to the tile
     * @return will return true if can fit all
     */
    public boolean canAddEnergy(double energy, ItemStack stack);

    /**
     * Will try add add the full amount of energy.
     *
     * @param energy amount to add
     * @return The amount of energy that was added.
     */
    public double addEnergy(double energy, ItemStack stack);

    /**
     * Will try add add the full amount of energy, if simulate is true it wont add the energy
     *
     * @param energy amount to add
     * @return The amount of energy that was added.
     */
    public double addEnergy(double energy, boolean simulate, ItemStack stack);

    /**
     * Returns true if it can use the full amount of energy
     *
     * @param energy amount of energy to use from the tile.
     * @return if all the energy can be used.
     */
    public boolean canUseEnergy(double energy, ItemStack stack);

    /**
     * Will try and use the full amount of energy
     *
     * @param energy energy to use
     * @return the amount of energy used
     */
    public double useEnergy(double energy, ItemStack stack);


    /**
     * Will try and use the full amount of energy, if simulate is true it wont add the energy
     *
     * @param energy energy to use
     * @return the amount of energy used
     */
    public double useEnergy(double energy, boolean simulate, ItemStack stack);

    /**
     * @return if it can accept energy
     */
    public boolean canAcceptEnergy(ItemStack stack);

    /**
     * @return if it can provide energy
     */
    public boolean canProvideEnergy(ItemStack stack );


    public double getMaxTransfer(ItemStack stack);

    public int getStackTeir(ItemStack stack);

}

