package techreborn.items.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.powerSystem.PoweredArmor;

public class ItemLithiumBatpack extends PoweredArmor {

    public static final int maxCharge = ConfigTechReborn.LithiumBatpackCharge;
    public static final int tier = ConfigTechReborn.LithiumBatpackTier;
    public double transferLimit = 10000;

    public ItemLithiumBatpack(ArmorMaterial armorMaterial, int par3, int par4) {
        super(armorMaterial, par3, par4);
        setMaxStackSize(1);
        setUnlocalizedName("techreborn.lithiumbatpack");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("techreborn:" + "tool/lithiumBatpack");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return "techreborn:" + "textures/models/lithiumbatpack.png";
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
