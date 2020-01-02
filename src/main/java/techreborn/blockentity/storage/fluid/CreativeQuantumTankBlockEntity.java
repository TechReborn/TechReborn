package techreborn.blockentity.storage.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.util.Tank;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

@Deprecated
public class CreativeQuantumTankBlockEntity extends TankUnitBaseBlockEntity {

	// Save
	public CreativeQuantumTankBlockEntity() {
		super(TRBlockEntities.CREATIVE_QUANTUM_TANK, TRContent.TankUnit.QUANTUM);
	}

	@Override
	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState) {
		inventory.clear();
	}

	@Override
	public void tick() {

		if (world.isClient()) {
			return;
		}

		Tank tank = this.getTank();

		inventory.clear();

		world.setBlockState(pos, TRContent.TankUnit.CREATIVE.block.getDefaultState());

		TankUnitBaseBlockEntity tankEntity = (TankUnitBaseBlockEntity) world.getBlockEntity(pos);

		tankEntity.setTank(tank);
	}
}
