package techreborn.blockentity.machine.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MultiblockWriter;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.utils.WorldHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class MiningRigBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {
	private int pipeReserveCount;
	private BlockPos reserveHead = null;

	private int drillDepth;
	private BlockPos drillHead =  null;

	// Position from just inside block at bottom
	private final int DRILL_OFFSET = 3;


	public MiningRigBlockEntity() {
		// TODO config these values
		super(TRBlockEntities.MINING_RIG, "MiningRig", 512, 50000, TRContent.Machine.MINING_RIG.block, 6);
	}

	@Override
	public void tick() {
		if(world == null || world.isClient || !isMultiblockValid()){
			return;
		}

		if (world.getTime() % 40 == 0) {
			updatePipeReserve();
			updatePipeDrillDepth();
			addDrillHead();
		}




	}

	// Checks that the stored quantity is equal to physical world
	private void updatePipeReserve() {
		pipeReserveCount = WorldHelper.getBlockCountAlongY(this.pos,1, TRContent.DRILL_PIPE,world);

		// Reserve position is just machine offset by count
		reserveHead = this.pos.offset(Direction.UP, pipeReserveCount);
	}

	private void updatePipeDrillDepth(){
		Block[] blocks = {TRContent.DRILL_PIPE, TRContent.DRILL_HEAD};
		drillDepth = WorldHelper.getBlockCountAlongY(this.pos.offset(Direction.DOWN, DRILL_OFFSET),-1, Arrays.asList(blocks), world);

		// Reserve position is just machine offset by count
		drillHead = this.pos.offset(Direction.DOWN, drillDepth + DRILL_OFFSET + 1);
	}
	private boolean hasPipeAbove(){
		return world.getBlockState(this.pos.offset(Direction.UP)).getBlock() == TRContent.DRILL_PIPE;
	}

	private boolean consumePipe(){

		if(!validReserveHead() || pipeReserveCount == 0 && hasPipeAbove()){
			updatePipeReserve();
		}

		if(pipeReserveCount == 0){
			return false;
		}

		world.setBlockState(reserveHead, Blocks.AIR.getDefaultState());
		pipeReserveCount--;
		reserveHead = reserveHead.offset(Direction.DOWN);


		return true;
	}

	// Ensure the head of the reserve stack has air above it, pipe or machine below
	private boolean validReserveHead(){
		Block blockHead = world.getBlockState(reserveHead).getBlock();
		Block blockAbove = world.getBlockState(reserveHead.offset(Direction.UP)).getBlock();
		Block blockBelow = world.getBlockState(reserveHead.offset(Direction.DOWN)).getBlock();

		Block pipe = TRContent.DRILL_PIPE;
		return blockHead.is(pipe) && blockAbove.is(Blocks.AIR) &&
				blockBelow.is(pipe) || blockBelow.is(this.getBlockType());
	}

	private boolean addDrillHead(){
		BlockPos drillPos = pos.offset(Direction.DOWN, DRILL_OFFSET + 1);
		world.setBlockState(drillPos,  TRContent.DRILL_HEAD.getDefaultState());
		this.drillHead = drillPos;
		drillDepth++;
		return true;
	}

	private void removeDrillHead(){

	}


	private void progressDrill(){

	}

	@Override
	public void writeMultiblock(MultiblockWriter writer) {
		BlockState basic = TRContent.MachineBlocks.BASIC.getCasing().getDefaultState();
		BlockState advanced = TRContent.MachineBlocks.ADVANCED.getCasing().getDefaultState();
		BlockState reinGlass = TRContent.REINFORCED_GLASS.getDefaultState();
		BlockState pipe = TRContent.DRILL_PIPE.getDefaultState();

		// TODO use predicate for manual blocks
		writer.translate(-1, -4, -1)
				.ring(Direction.Axis.Y, 3, 1, 3, (view, pos) -> view.getBlockState(pos) == basic, basic, (view, pos) -> view.getBlockState(pos) == pipe, pipe)
				.ring(Direction.Axis.Y, 3, 3, 3, (view, pos) -> view.getBlockState(pos) == basic, basic, (view, pos) -> view.getBlockState(pos) == pipe, pipe)
				.add(2, 2, 2, advanced) // Corner 1
				.add(1, -2, 1, advanced) // Corner 2
				.add(1, -2, -1, advanced) // Corner 3
				.add(-1, -2, 1, advanced) // Corner 4
				.add(0,-2, 1, reinGlass) // Glass 1
				.add(1,-2, 0, reinGlass) // Glass 2
				.add(-1,-2, 0, reinGlass) // Glass 3
				.add(0,-2, -1, reinGlass) // Glass 4
				.add(0, -2, 0, pipe); // Pipe in middle
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("miningrig").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).addInventory().create(this, syncID);
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}


}
