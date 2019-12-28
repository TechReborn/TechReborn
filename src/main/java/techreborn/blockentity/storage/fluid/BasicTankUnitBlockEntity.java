package techreborn.blockentity.storage.fluid;

import reborncore.common.fluid.FluidValue;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;

public class BasicTankUnitBlockEntity extends TankStorageBaseBlockEntity {

	public BasicTankUnitBlockEntity(){
		super(TRBlockEntities.BASIC_TANK_UNIT,  FluidValue.BUCKET.multiply(TechRebornConfig.basicTankCapacity));
	}
}
