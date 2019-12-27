package techreborn.blockentity.storage.fluid;

import reborncore.common.fluid.FluidValue;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class DigitalTankBlockEntity extends TankStorageBaseBlockEntity {

	public DigitalTankBlockEntity(){
		super(TRBlockEntities.DIGITAL_TANK,  FluidValue.BUCKET.multiply(TechRebornConfig.digitalTankCapacity));
	}
}
