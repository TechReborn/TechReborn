package techreborn.parts;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;

import java.util.List;

/**
 * Created by modmuss50 on 27/02/2016.
 */
public class ItemCables extends ItemMultiPart implements ITexturedItem {

    public ItemCables() {
        setCreativeTab(TechRebornCreativeTab.instance);
        setHasSubtypes(true);
        setUnlocalizedName("techreborn.cable");
        setNoRepair();
        RebornCore.jsonDestroyer.registerObject(this);
        ItemStandaloneCables.mcPartCable = this;
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player) {
        try {
            return TechRebornParts.multipartHashMap.get(EnumCableType.values()[stack.getItemDamage()]).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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