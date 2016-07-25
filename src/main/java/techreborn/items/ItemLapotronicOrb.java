package techreborn.items;

import net.minecraft.item.ItemStack;
import reborncore.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;

public class ItemLapotronicOrb extends ItemTextureBase implements IEnergyItemInfo {

    public static final int maxCharge = ConfigTechReborn.LapotronicOrbMaxCharge;
    public static final int tier = ConfigTechReborn.LithiumBatpackTier;
    public double transferLimit = 10000;

    public ItemLapotronicOrb() {
        super();
        setMaxStackSize(1);
        setMaxDamage(13);
        setUnlocalizedName("techreborn.lapotronicorb");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public double getMaxPower(ItemStack stack) {
        return maxCharge;
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
        return transferLimit;
    }

    @Override
    public int getStackTier(ItemStack stack) {
        return tier;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/lapotronicEnergyOrb";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }

}
