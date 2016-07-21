package techreborn.tiles.generator;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.tile.TilePowerProducer;
import techreborn.power.EnergyUtils;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileWindMill extends TilePowerProducer {

	int basePower = 16;


	@Override
	public void update() {
		super.update();
		if (pos.getY() > 64) {
			double actualPower = basePower + basePower * worldObj.getThunderStrength(1.0F);
			addEnergy(actualPower);
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

	@Override
	public double getMaxPower() {
		return 8000;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
	}

}
