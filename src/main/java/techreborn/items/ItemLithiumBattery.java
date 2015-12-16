package techreborn.items;

import net.minecraft.item.ItemStack;
import techreborn.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;

public class ItemLithiumBattery extends ItemTextureBase implements IEnergyItemInfo {

    public ItemLithiumBattery() {
        super();
        setMaxStackSize(1);
        setMaxDamage(1);
        setUnlocalizedName("techreborn.lithiumBattery");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public double getMaxPower(ItemStack stack) {
        return 100000;
    }

    @Override
    public boolean canAcceptEnergy(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    @Override
    public double getMaxTransfer(ItemStack stack) {
        return 64;
    }

    @Override
    public int getStackTeir(ItemStack stack) {
        return 1;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/lithiumBattery";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
