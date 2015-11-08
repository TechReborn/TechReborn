package techreborn.api.power;


import net.minecraft.item.ItemStack;

public interface IEnergyItemInfo {

    /**
     * Gets the max stored energy in the tile
     *
     * @return The max energy
     */
    double getMaxPower(ItemStack stack);

    /**
     * @return if it can accept energy
     */
    boolean canAcceptEnergy(ItemStack stack);

    /**
     * @return if it can provide energy
     */
    boolean canProvideEnergy(ItemStack stack);

    double getMaxTransfer(ItemStack stack);

    int getStackTeir(ItemStack stack);
}
