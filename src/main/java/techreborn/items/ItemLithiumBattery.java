package techreborn.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.powerSystem.PoweredItem;
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
    public double getDurabilityForDisplay(ItemStack stack) {
        double charge = (PoweredItem.getEnergy(stack) / getMaxPower(stack));
        return 1 - charge;

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
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
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
