package techreborn.tiles.storage;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class TileBatBox  extends TilePowerAcceptor implements IWrenchable {

    public TileBatBox() {
        super(1);
    }

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
        return false;
    }

    @Override
    public EnumFacing getFacing() {
        return getFacingEnum();
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        if (entityPlayer.isSneaking()) {
            return true;
        }
        return false;
    }

    @Override
    public float getWrenchDropRate() {
        return 1.0F;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(ModBlocks.batBox);
    }

    public boolean isComplete() {
        return false;
    }

    @Override
    public double getMaxPower() {
        return 40000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return getFacingEnum() != direction;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return getFacingEnum() == direction;
    }

    @Override
    public double getMaxOutput() {
        return 32;
    }

    @Override
    public double getMaxInput() {
        return 32;
    }

    @Override
    public EnumPowerTier getTier() {
        return EnumPowerTier.LOW;
    }
}