package techreborn.items.armor;


import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;

import java.util.List;

public class ItemLapotronPack extends ItemArmor implements IEnergyItemInfo, ITexturedItem {

    public static final int maxCharge = ConfigTechReborn.LapotronPackCharge;
    public static final int tier = ConfigTechReborn.LapotronPackTier;
    public double transferLimit = 100000;

    public ItemLapotronPack() {
        super(ItemArmor.ArmorMaterial.DIAMOND, 7, EntityEquipmentSlot.CHEST);
        setCreativeTab(TechRebornCreativeTab.instance);
        setUnlocalizedName("techreborn.lapotronpack");
        setMaxStackSize(1);
        RebornCore.jsonDestroyer.registerObject(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
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
    public int getStackTier(ItemStack stack) {
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
        return "techreborn:items/tool/lapotronicEnergyOrb";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }

}
