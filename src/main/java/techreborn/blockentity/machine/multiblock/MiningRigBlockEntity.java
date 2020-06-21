package techreborn.blockentity.machine.multiblock;

import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class MiningRigBlockEntity extends GenericMachineBlockEntity {
	public MiningRigBlockEntity() {

		// TODO config these values
		super(TRBlockEntities.MINING_RIG, "Mining Rig", 512, 50000, TRContent.Machine.MINING_RIG.block, 6);
	}
}
