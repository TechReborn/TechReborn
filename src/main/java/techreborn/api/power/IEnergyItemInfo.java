package techreborn.api.power;


import net.minecraft.item.ItemStack;

public interface IEnergyItemInfo {

    /**
     * Gets the max stored energy in the item
     *
     * @param stack The {@link ItemStack}  that contains the power
     * @return The max energy
     */
    double getMaxPower(ItemStack stack);

    /**
     * Can the item accept energy.
     * @param stack The {@link ItemStack}  that contains the power
     * @return if it can accept energy
     */
    boolean canAcceptEnergy(ItemStack stack);

    /**
     * Can the item recieve energy
     * @param stack The {@link ItemStack}  that contains the power
     * @return if it can provide energy
     */
    boolean canProvideEnergy(ItemStack stack);

    /**
     *
     * @param stack The {@link ItemStack}  that contains the power
     * @return Max amout of energy that can be transfured in one tick.
     */
    double getMaxTransfer(ItemStack stack);

    /**
     *
     * @param stack The {@link ItemStack}  that contains the power
     * @return The ic2 teir that the stack should be.
     */
    int getStackTeir(ItemStack stack);
}
