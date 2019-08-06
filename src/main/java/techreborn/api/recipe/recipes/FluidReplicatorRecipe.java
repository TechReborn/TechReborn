package techreborn.api.recipe.recipes;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.crafting.RebornFluidRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.multiblock.FluidReplicatorBlockEntity;
import techreborn.utils.FluidUtils;

public class FluidReplicatorRecipe extends RebornFluidRecipe {

	public FluidReplicatorRecipe(RebornRecipeType<?> type, Identifier name) {
		super(type, name);
	}

	@Override
	public Tank getTank(BlockEntity be) {
		FluidReplicatorBlockEntity blockEntity = (FluidReplicatorBlockEntity) be;
		return blockEntity.getTank();
	}

	@Override
	public boolean canCraft(BlockEntity be) {
		FluidReplicatorBlockEntity blockEntity = (FluidReplicatorBlockEntity) be;
		if (!blockEntity.getMultiBlock()) {
			return false;
		}
		final BlockPos hole = blockEntity.getPos().offset(blockEntity.getFacing().getOpposite(), 2);
		final Fluid fluid = techreborn.utils.FluidUtils.fluidFromBlock(blockEntity.getWorld().getBlockState(hole).getBlock());
		if (fluid == Fluids.EMPTY) {
			return true;
		}
		if (!FluidUtils.fluidEquals(fluid, getFluidInstance().getFluid())) {
			return false;
		}
		final Fluid tankFluid = blockEntity.tank.getFluid();
		if (tankFluid != Fluids.EMPTY && !FluidUtils.fluidEquals(tankFluid, fluid)) {
			return false;
		}
		return blockEntity.tank.canFit(getFluidInstance().getFluid(), getFluidInstance().getAmount());
	}

	@Override
	public boolean onCraft(BlockEntity be) {
		FluidReplicatorBlockEntity blockEntity = (FluidReplicatorBlockEntity) be;
		if (blockEntity.tank.canFit(getFluidInstance().getFluid(), getFluidInstance().getAmount())) {
			blockEntity.tank.setFluid(getFluidInstance().getFluid());
			blockEntity.tank.getFluidInstance().addAmount(getFluidInstance().getAmount());
		}
		return true;
	}
}
