package techreborn.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import techreborn.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;

public class ItemLithiumBattery extends ItemTR implements IEnergyItemInfo {

    public ItemLithiumBattery() {
        super();
        setMaxStackSize(1);
        setMaxDamage(1);
        setUnlocalizedName("techreborn.lithiumBattery");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("techreborn:" + "lithiumBattery");
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
}
