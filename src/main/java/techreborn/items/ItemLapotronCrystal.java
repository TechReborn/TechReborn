package techreborn.items;

import net.minecraft.item.ItemStack;
import reborncore.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;


public class ItemLapotronCrystal extends ItemTextureBase implements IEnergyItemInfo {

    public static final int maxCharge = 100000;
    public static final int tier = 2;
    public double transferLimit = 512;

    public ItemLapotronCrystal() {
        super();
        setMaxStackSize(1);
        setMaxDamage(13);
        setUnlocalizedName("techreborn.lapotroncrystal");
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
    public int getStackTeir(ItemStack stack) {
        return tier;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/lapotroncrystal";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }
}
