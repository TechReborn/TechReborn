package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;

import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileWaterMill extends TilePowerAcceptor implements IWrenchable {

	int waterblocks = 0;

	public TileWaterMill() {
		super(1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.world.getTotalWorldTime() % 20 == 0) {
			this.checkForWater();
		}
		if (this.waterblocks > 0) {
			this.addEnergy(this.waterblocks);
		}
	}

	public void checkForWater() {
		this.waterblocks = 0;
		for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (this.world.getBlockState(this.getPos().offset(facing)).getBlock() == Blocks.WATER) {
				this.waterblocks++;
			}
		}
	}

	@Override
	public double getMaxPower() {
		return 1000;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public double getMaxOutput() {
		return 32;
	}

	@Override
	public double getMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer p0) {
		return new ItemStack(ModBlocks.WATER_MILL);
	}
}
