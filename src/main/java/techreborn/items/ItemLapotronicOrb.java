package techreborn.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.powerSystem.PoweredItem;


public class ItemLapotronicOrb extends Item implements IEnergyItemInfo{

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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("techreborn:" + "lapotronicEnergyOrb");
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
}
