package techreborn.items.armor;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import techreborn.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.powerSystem.PoweredItem;

import java.util.List;

public class ItemLithiumBatpack extends ItemArmor implements IEnergyItemInfo, ITexturedItem {

    public static final int maxCharge = ConfigTechReborn.LithiumBatpackCharge;
    public static final int tier = ConfigTechReborn.LithiumBatpackTier;
    public double transferLimit = 10000;

    public ItemLithiumBatpack() {
        super(ItemArmor.ArmorMaterial.DIAMOND, 7, 1);
        setMaxStackSize(1);
        setUnlocalizedName("techreborn.lithiumbatpack");
        setCreativeTab(TechRebornCreativeTab.instance);
        RebornCore.jsonDestroyer.registerObject(this);
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

    @SuppressWarnings(
            {"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        itemList.add(itemStack);

        ItemStack charged = new ItemStack(this, 1);
        PoweredItem.setEnergy(getMaxPower(charged), charged);
        itemList.add(charged);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
        return 1 - charge;

    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/lithiumBatpack";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }

}
