package techreborn.blockentity.storage.fluid;

import reborncore.common.fluid.FluidValue;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;

public class IndustrialTankUnitBlockEntity extends TankUnitBaseBlockEntity {

	public IndustrialTankUnitBlockEntity(){
		super(TRBlockEntities.INDUSTRIAL_TANK_UNIT,  FluidValue.BUCKET.multiply(TechRebornConfig.digitalTankCapacity));
	}
}
