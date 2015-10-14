package techreborn.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.powerSystem.PoweredItem;

import java.util.List;

public class ItemCloakingDevice extends PoweredItem {
    public static int Teir = ConfigTechReborn.CloakingDeviceTier;
    public static int MaxCharge = ConfigTechReborn.CloakingDeviceCharge;
    public static int Limit = 100;
    public static boolean isActive;
    private int armorType = 1;

    public ItemCloakingDevice() {
        setUnlocalizedName("techreborn.cloakingdevice");
        setMaxStackSize(1);
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if(canUseEnergy(ConfigTechReborn.CloakingDeviceEUTick, itemStack)){
            useEnergy(ConfigTechReborn.CloakingDeviceEUTick, itemStack);
            player.setInvisible(true);
        } else {
            if (!player.isPotionActive(Potion.invisibility)) {
                player.setInvisible(false);
            }
        }
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        return armorType == this.armorType;

    }

    @Override
    public double getMaxPower(ItemStack stack) {
        return MaxCharge;
    }

    @Override
    public boolean canAcceptEnergy(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxTransfer(ItemStack stack) {
        return Limit;
    }

    @Override
    public int getStackTeir(ItemStack stack) {
        return Teir;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        if (getChargedItem(itemStack) == this) {
            ItemStack charged = new ItemStack(this, 1);
            setEnergy(MaxCharge, charged);
            itemList.add(charged);
        }
        if (getEmptyItem(itemStack) == this) {
            itemList.add(new ItemStack(this, 1, getMaxDamage()));
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double charge = (getEnergy(stack) / getMaxCharge(stack));
        return 1 - charge;

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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("techreborn:"
                + "techreborn.cloakingdevice");
    }


}
