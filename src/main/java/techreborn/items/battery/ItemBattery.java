package techreborn.items.battery;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.util.PowerLocalization;
import techreborn.items.ItemTRNoDestroy;
import techreborn.power.EnergyItem;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBattery extends ItemTRNoDestroy implements IEnergyItemInfo {

    int maxEnergy = 0;
    int maxTransfer = 0;
    int tier = 0;

    public ItemBattery(String name, int maxEnergy, int maxTransfer, int tier) {
        super();
        setMaxStackSize(1);
        setMaxDamage(1);
        setUnlocalizedName("techreborn." + name);
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        this.tier = tier;
        this.addPropertyOverride(new ResourceLocation("techreborn:empty"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn,
                               @Nullable EntityLivingBase entityIn) {
                if (stack != null && EnergyItem.getEnergy(stack) == 0.0) {
                    return 1.0F;
                }
                return 0.0F;
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List<ItemStack> itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        itemList.add(itemStack);

        ItemStack charged = new ItemStack(this, 1);
        EnergyItem.setEnergy(getMaxPower(charged), charged);
        itemList.add(charged);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        String max = PowerLocalization.getLocalizedPower(getMaxPower(stack));
        String energy = PowerLocalization.getLocalizedPower(EnergyItem.getEnergy(stack));
        tooltip.add(TextFormatting.GRAY + energy + " / " + max);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - (EnergyItem.getEnergy(stack) / getMaxPower(stack));
    }

    @Override
    public double getMaxPower(ItemStack stack) {
        return maxEnergy;
    }

    @Override
    public double getMaxTransfer(ItemStack stack) {
        return maxTransfer;
    }

    @Override
    public int getStackTier(ItemStack stack) {
        return tier;
    }

    @Override
    public boolean canAcceptEnergy(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

}
