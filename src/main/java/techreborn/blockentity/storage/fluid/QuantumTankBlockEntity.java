package techreborn.blockentity.storage.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.fluid.FluidValue;
import reborncore.common.util.Tank;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

@Deprecated
public class QuantumTankBlockEntity extends TankUnitBaseBlockEntity {

	// Save
	public QuantumTankBlockEntity() {
		super(TRBlockEntities.QUANTUM_TANK, TRContent.TankUnit.QUANTUM);
	}

	@Override
	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState) {
		inventory.clear();
	}

	@Override
	public void tick() {

		if(world.isClient()){
			return;
		}

		Tank tank = this.getTank();

		ItemStack inputSlotStack = getInvStack(0).copy();
		ItemStack outputSlotStack = getInvStack(1).copy();
		inventory.clear();

		world.setBlockState(pos, TRContent.TankUnit.QUANTUM.block.getDefaultState());

		TankUnitBaseBlockEntity tankEntity = (TankUnitBaseBlockEntity)world.getBlockEntity(pos);

		tankEntity.setTank(tank);

		tankEntity.setInvStack(0, inputSlotStack);
		tankEntity.setInvStack(1, outputSlotStack);
	}
}
