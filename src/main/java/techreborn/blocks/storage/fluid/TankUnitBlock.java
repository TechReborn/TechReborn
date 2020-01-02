package techreborn.blocks.storage.fluid;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.client.EGui;
import techreborn.init.TRContent;

public class TankUnitBlock extends BlockMachineBase {

	public final TRContent.TankUnit unitType;

	public TankUnitBlock(TRContent.TankUnit unitType) {
		super();
		this.unitType = unitType;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new TankUnitBaseBlockEntity(unitType);
	}

	@Override
	public IMachineGuiHandler getGui() {
		return EGui.TANK_UNIT;
	}
}
