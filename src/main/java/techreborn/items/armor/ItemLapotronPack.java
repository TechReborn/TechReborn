package techreborn.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.powerSystem.PoweredArmor;

import java.util.List;

public class ItemLapotronPack extends PoweredArmor {

    public static final int maxCharge = ConfigTechReborn.LapotronPackCharge;
    public static final int tier = ConfigTechReborn.LapotronPackTier;
    public double transferLimit = 100000;

    public ItemLapotronPack(ArmorMaterial armormaterial, int par2, int par3) {
        super(armormaterial, par2, par3);
        setCreativeTab(TechRebornCreativeTab.instance);
        setUnlocalizedName("techreborn.lapotronpack");
        setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("techreborn:" + "tool/lapotronicEnergyOrb");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "techreborn:" + "textures/models/lapotronpack.png";
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
    public boolean canProvideEnergy(ItemStack itemStack) {
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
