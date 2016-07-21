package techreborn.tiles.generator;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.tile.TilePowerProducer;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.power.EnergyUtils;

import java.util.List;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileSolarPanel extends TilePowerProducer implements ITickable {

	boolean shouldMakePower = false;

	int powerToAdd;

	@Override
	public void update() {
		super.update();
		if (!worldObj.isRemote) {
			if (worldObj.getTotalWorldTime() % 60 == 0) {
				shouldMakePower = isSunOut();

			}
			if (shouldMakePower) {
				powerToAdd = 10;
				addEnergy(powerToAdd);
			} else {
				powerToAdd = 0;
			}
			worldObj.setBlockState(getPos(), worldObj.getBlockState(this.getPos()).withProperty(BlockSolarPanel.ACTIVE, isSunOut()));
		}
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		super.addInfo(info, isRealTile);
		if (isRealTile) {
			// FIXME: 25/02/2016
			// info.add(TextFormatting.LIGHT_PURPLE + "Power gen/tick " +
			// TextFormatting.GREEN + PowerSystem.getLocalizedPower(
			// powerToAdd)) ;
		}
	}

	@Override
	public double emitEnergy(EnumFacing enumFacing, double amount) {
		BlockPos pos = getPos().offset(enumFacing);
		EnergyUtils.PowerNetReceiver receiver = EnergyUtils.getReceiver(
				worldObj, enumFacing.getOpposite(), pos);
		if(receiver != null) {
			addEnergy(amount - receiver.receiveEnergy(amount, false));
		} else addEnergy(amount);
		return 0; //Temporary hack die to my bug RebornCore
	}

	public boolean isSunOut() {
		return worldObj.canBlockSeeSky(pos.up()) && !worldObj.isRaining() && !worldObj.isThundering()
				&& worldObj.isDaytime();
	}

	@Override
	public double getMaxPower() {
		return 8000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

}
