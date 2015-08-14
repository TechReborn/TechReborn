package techreborn.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import techreborn.config.ConfigTechReborn;
import techreborn.items.ItemTR;

import java.util.List;

public class ItemCloakingDevice extends ItemTR implements IElectricItem {
    public static int Teir = ConfigTechReborn.CloakingDeviceTier;
    public static int MaxCharge = ConfigTechReborn.CloakingDeviceCharge;
    public static int Limit = 100;
    public static boolean isActive;
    private int armorType = 1;

    public ItemCloakingDevice() {
        setUnlocalizedName("techreborn.cloakingdevice");
        setMaxStackSize(1);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (ElectricItem.manager != null) {
            if (ElectricItem.manager.use(itemStack, ConfigTechReborn.CloakingDeviceEUTick, player)) {
                player.setInvisible(true);
            } else {
                if (!player.isPotionActive(Potion.invisibility)) {
                    player.setInvisible(false);
                }
            }

        }
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        return armorType == this.armorType;

    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (getChargedItem(itemStack) == this && ElectricItem.manager != null) {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, 2147483647, 2147483647, true,
                    false);
            itemList.add(charged);
        }
        if (getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, getMaxDamage()));
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (ElectricItem.manager != null) {
            double charge = (ElectricItem.manager.getCharge(stack) / getMaxCharge(stack));
            return 1 - charge;
        }
        return 1;
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        ItemStack itemstack1 = player.getCurrentArmor(3);

        if (itemstack1 == null) {
            player.setCurrentItemOrArmor(3, itemStack.copy());
            itemStack.stackSize = 0;
        }

        return itemStack;
    }

    @Override
    public int getDisplayDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return MaxCharge;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return Teir;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return Limit;
    }
}
