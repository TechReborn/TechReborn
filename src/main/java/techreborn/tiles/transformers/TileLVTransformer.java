package techreborn.tiles.transformers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import reborncore.api.power.EnumPowerTier;
import techreborn.init.ModBlocks;
import techreborn.tiles.storage.TileBatBox;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class TileLVTransformer extends TileBatBox {

    @Override
    public double getMaxOutput() {
        return 32;
    }

    @Override
    public double getMaxInput() {
        return 128;
    }

    @Override
    //Can take medium power in
    public EnumPowerTier getTier() {
        return EnumPowerTier.MEDIUM;
    }

    @Override
    public double getMaxPower() {
        return getMaxInput() * 2;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(worldObj.getBlockState(pos).getBlock());
    }

}
