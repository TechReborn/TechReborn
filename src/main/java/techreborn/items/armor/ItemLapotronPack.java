package techreborn.items.armor;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.power.IEnergyInterfaceTile;
import reborncore.api.power.IEnergyItemInfo;
import techreborn.client.TechRebornCreativeTab;
import techreborn.config.ConfigTechReborn;
import techreborn.lib.ModInfo;
import techreborn.power.EnergyItem;

import java.util.List;

public class ItemLapotronPack extends ItemArmor implements IEnergyItemInfo, ITexturedItem {

    public static final int MAX_CHARGE = ConfigTechReborn.LapotronPackCharge;
    public static final int TIER = ConfigTechReborn.LapotronPackTier;
    public static final double TRANSFER_LIMIT = 100000;

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
        return MAX_CHARGE;
    }

    @Override
    public double getMaxTransfer(ItemStack stack) {
        return TRANSFER_LIMIT;
    }

    @Override
    public int getStackTier(ItemStack stack) {
        return TIER;
    }

    @Override
    public boolean canAcceptEnergy(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        itemList.add(itemStack);

        ItemStack charged = new ItemStack(this, 1);
        EnergyItem.setEnergy(getMaxPower(charged), charged);
        itemList.add(charged);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        double charge = (EnergyItem.getEnergy(stack) / getMaxPower(stack));
        return 1 - charge;

    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i) != null) {
                ItemStack itemStack1 = player.inventory.getStackInSlot(i);
                if (itemStack1 != null) {
                    Item item1 = itemStack1.getItem();
                    if (item1 instanceof IEnergyInterfaceItem ||
                            item1 instanceof IEnergyContainerItem ||
                            item1 instanceof IElectricItem ||
                            itemStack1.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null)) {
                        EnergyItem.charge(itemStack1, new IEnergyInterfaceTile() {
                            @Override
                            public double getEnergy() {
                                return EnergyItem.getEnergy(itemStack);
                            }

                            @Override
                            public void setEnergy(double energy) {
                                EnergyItem.setEnergy(energy, itemStack);
                            }

                            @Override
                            public double getMaxPower() {
                                return MAX_CHARGE;
                            }

                            @Override
                            public EnumPowerTier getTier() {
                                return EnumPowerTier.EXTREME;
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public String getTextureName(int damage) {
        return "techreborn:items/tool/lapotronicPack";
    }

    @Override
    public int getMaxMeta() {
        return 1;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
    }

}
