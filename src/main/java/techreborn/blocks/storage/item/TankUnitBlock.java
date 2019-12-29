package techreborn.blocks.storage.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.client.EGui;
import techreborn.init.TRContent;

public class TankUnitBlock extends BlockMachineBase {

	public final TRContent.TankUnit unitType;

	public TankUnitBlock(TRContent.TankUnit unitType){
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
