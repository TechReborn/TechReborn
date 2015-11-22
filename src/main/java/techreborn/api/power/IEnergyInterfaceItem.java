package techreborn.api.power;

import net.minecraft.item.ItemStack;

public interface IEnergyInterfaceItem extends IEnergyItemInfo{
    /**
     * @return Amount of energy in the tile
     *
     * @param stack The {@link ItemStack}  that contains the power
     */
    public double getEnergy(ItemStack stack);

    /**
     * Sets the energy in the tile
     *
     * @param energy the amount of energy to set.
     * @param stack The {@link ItemStack}  that contains the power
     */
    public void setEnergy(double energy, ItemStack stack);


    /**
     * @param energy amount of energy to add to the tile
     * @param stack The {@link ItemStack}  that contains the power
     * @return will return true if can fit all
     */
    public boolean canAddEnergy(double energy, ItemStack stack);

    /**
     * Will try add add the full amount of energy.
     *
     * @param energy amount to add
     * @param stack The {@link ItemStack}  that contains the power
     * @return The amount of energy that was added.
     */
    public double addEnergy(double energy, ItemStack stack);

    /**
     * Will try add add the full amount of energy, if simulate is true it wont add the energy
     *
     * @param energy amount to add
     * @param stack The {@link ItemStack}  that contains the power
     * @return The amount of energy that was added.
     */
    public double addEnergy(double energy, boolean simulate, ItemStack stack);

    /**
     * Returns true if it can use the full amount of energy
     *
     * @param energy amount of energy to use from the tile.
     * @param stack The {@link ItemStack}  that contains the power
     * @return if all the energy can be used.
     */
    public boolean canUseEnergy(double energy, ItemStack stack);

    /**
     * Will try and use the full amount of energy
     *
     * @param energy energy to use
     * @param stack The {@link ItemStack}  that contains the power
     * @return the amount of energy used
     */
    public double useEnergy(double energy, ItemStack stack);


    /**
     * Will try and use the full amount of energy, if simulate is true it wont add the energy
     *
     * @param energy energy to use
     * @param simulate if true it will not use the item, it will only simulate it.
     * @param stack The {@link ItemStack}  that contains the power
     * @return the amount of energy used
     */
    public double useEnergy(double energy, boolean simulate, ItemStack stack);

}

