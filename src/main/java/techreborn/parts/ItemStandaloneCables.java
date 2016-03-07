package techreborn.parts;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTab;
import techreborn.items.ItemTextureBase;
import techreborn.lib.ModInfo;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by modmuss50 on 06/03/2016.
 */
public class ItemStandaloneCables extends ItemTextureBase {

    public static Item mcPartCable;

    public static ItemStack getCableByName(String name, int count) {
        for (int i = 0; i < EnumCableType.values().length; i++) {
            if (EnumCableType.values()[i].getName().equalsIgnoreCase(name)) {
                return new ItemStack(mcPartCable != null ? mcPartCable : StandalonePartCompact.itemStandaloneCable, count, i);
            }
        }
        throw new InvalidParameterException("The cable " + name + " could not be found.");
    }

    public static ItemStack getCableByName(String name) {
        return getCableByName(name, 1);
    }

    public ItemStandaloneCables() {
        setCreativeTab(TechRebornCreativeTab.instance);
        setHasSubtypes(true);
        setUnlocalizedName("techreborn.cable");
        setNoRepair();
        RebornCore.jsonDestroyer.registerObject(this);
    }

    @Override
    // gets Unlocalized Name depending on meta data
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= EnumCableType.values().length) {
            meta = 0;
        }

        return super.getUnlocalizedName() + "." + EnumCableType.values()[meta];
    }

    // Adds Dusts SubItems To Creative Tab
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < EnumCableType.values().length; ++meta) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    @Override
    public String getTextureName(int damage) {
        return ModInfo.MOD_ID + ":items/cables/" + EnumCableType.values()[damage];
    }

    @Override
    public int getMaxMeta() {
        return EnumCableType.values().length;
    }

    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        EnumCableType type = EnumCableType.values()[stack.getItemDamage()];
        tooltip.add(EnumChatFormatting.GREEN + "EU Transfer: " + EnumChatFormatting.LIGHT_PURPLE + type.transferRate);
        if (type.canKill) {
            tooltip.add(EnumChatFormatting.RED + "Damages entity's!");
        }
    }
}
