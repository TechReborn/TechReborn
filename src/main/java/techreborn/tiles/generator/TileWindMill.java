package techreborn.tiles.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;

import techreborn.init.ModBlocks;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileWindMill extends TilePowerAcceptor implements IWrenchable {

	int basePower = 16;

	public TileWindMill() {
		super(2);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.pos.getY() > 64) {
			int actualPower = this.basePower;
			if (this.world.isThundering()) {
				actualPower *= 1.25;
			}
			this.addEnergy(actualPower); // Value taken from
			// http://wiki.industrial-craft.net/?title=Wind_Mill
			// Not worth making more complicated
		}
	}

	@Override
	public double getMaxPower() {
		return 10000;
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
		return 128;
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
		return new ItemStack(ModBlocks.WIND_MILL);
	}
}
