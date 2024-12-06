/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blockentity.machine.tier2;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.blockentity.machine.tier2.miner.MinerProcessingState;
import techreborn.blocks.machine.tier2.MiningPipeBlock;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.*;

public class MinerBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider{

	/**
	 * Horizontal manhattan distance the ore prospection can cover per level.
	 */
	static final int DEFAULT_PROSPECTING_RANGE = 5;

	// Blocks per tick that are examined when a vertical level is scanned for ore.
	static final int DEFAULT_PROSPECTING_SPEED = 5;


	// Slots
	public static final int MINING_TOOL_SLOT = 0;
	public static final int PROSPECTING_TOOL_SLOT = MINING_TOOL_SLOT + 1;
	public static final int MINING_PIPE_INVENTORY_START = PROSPECTING_TOOL_SLOT + 1;
	public static final int MINING_PIPE_INVENTORY_SIZE = 3;
	public static final int OUTPUT_INVENTORY_START = MINING_PIPE_INVENTORY_START + MINING_PIPE_INVENTORY_SIZE + 1 ;
	public static final int OUTPUT_INVENTORY_SIZE = 12;
	public static final int ENERGY_SLOT = OUTPUT_INVENTORY_START + OUTPUT_INVENTORY_SIZE + 1;

	static final int INVENTORY_SIZE = ENERGY_SLOT + 1;

	//TODO: Add scanner when it is finished
	static final Set<Item> ALLOWED_PROSPECTING_TOOLS = Set.of();

	private static final Logger log = LoggerFactory.getLogger(MinerBlockEntity.class);

	private MinerProcessingState state;

	private Iterator<BlockPos> blockProspectionIterator;


	public MinerBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.MINER, pos, state, "Miner", 128, 10_000, TRContent.Machine.MINER.block, 0);
		// Inventory
		this.inventory = new RebornInventory<>(INVENTORY_SIZE, "MinerBlockEntity", 64, this);
		this.state = new MinerProcessingState.Ready();
	}

	public MinerProcessingState getState() {
		return state;
	}

	public static boolean canUseAsMiningTool(ItemStack itemStack) {
		return itemStack.isIn(TRContent.ItemTags.MINER_ACCEPTED_TOOLS);
	}

	public static boolean canUseAsProspectingTool(ItemStack itemStack) {
		return true;
//		return ALLOWED_PROSPECTING_TOOLS.contains(itemStack.getItem());
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		var builder = new ScreenHandlerBuilder("miner")
			.player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this);

		int offsetX = 36;
		int offsetY = 21;

		builder.filterSlot(MINING_TOOL_SLOT, offsetX, offsetY, MinerBlockEntity::canUseAsMiningTool);
		builder.filterSlot(PROSPECTING_TOOL_SLOT, offsetX, offsetY + 36, MinerBlockEntity::canUseAsProspectingTool);

		for (int i = 0; i< MINING_PIPE_INVENTORY_SIZE; i++){
			builder.filterSlot(MINING_PIPE_INVENTORY_START + i, offsetX + 21, offsetY + i * 18, itemStack -> itemStack.isOf(TRContent.MINING_PIPE.asItem()));
		}

		for (int row=0; row < 3; row++){
			for (int column=0; column < 4; column++) {
				builder.outputSlot(OUTPUT_INVENTORY_START + row * 4 + column, offsetX + 50 + 18 * column, offsetY + 18 * row);
			}
		}

		return builder.energySlot(ENERGY_SLOT, 8, 72)
			.syncEnergyValue()
			.addInventory()
			.create(this, syncID);

		//TODO Sync Nbt
	}

	private BlockPos findDrillHead(World world, final BlockPos startPosition) {
		BlockPos lastMiningPipePosition;
		BlockPos nextProbePosition = lastMiningPipePosition = startPosition;

		// Descent until we reached the end of the mining pipe
		while (world.getBlockState(nextProbePosition).isOf(TRContent.MINING_PIPE) || world.getBlockState(nextProbePosition).isOf(TRContent.Machine.MINER.block)) {
			lastMiningPipePosition = nextProbePosition;
			nextProbePosition = nextProbePosition.down();
		}
		return lastMiningPipePosition;
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) return;

		charge(ENERGY_SLOT);

		if (!this.isActive(RedstoneConfiguration.Element.RECIPE_PROCESSING)){
			return;
		}

		boolean nextTick = false;

		while (!nextTick) {
			nextTick = true;
			switch (this.state.getStatus()) {
				case NO_ENERGY -> {
					MinerProcessingState.NoEnergy noEnergy = (MinerProcessingState.NoEnergy) this.state;
					if (this.getEnergy() >= noEnergy.getMissingEnergy()) this.state = new MinerProcessingState.Ready();
				}
				case NO_TOOL -> {
					if (hasMiningTool()) this.state = new MinerProcessingState.Ready();
				}
				case NO_PIPE -> {
					if (hasMiningPipe()) this.state = new MinerProcessingState.Ready();
				}
				case READY -> {
					if (!hasMiningTool()) {
						this.state = new MinerProcessingState.NoTool();
						break;
					}
					if (!hasMiningPipe()) {
						this.state = new MinerProcessingState.NoPipe();
						break;
					}
					this.state = new MinerProcessingState.Probing(this.pos);
					nextTick = false;
				}
				case PROBING -> {
					MinerProcessingState.Probing probingState = (MinerProcessingState.Probing) this.state;

					if (!probingState.hasReachedProbe()) {
						probingState.setHeadPosition(findDrillHead(world, probingState.getHeadPosition()));
						probingState.setProbeReached();
						probingState.setHeadMovementCooldown((int) (55 * (1.0 - getSpeedMultiplier())) + 5);
					}

					if (this.hasProspectingTool() && !probingState.doSkipProspectionNextBlock()) {
						this.state = new MinerProcessingState.Prospecting(probingState.getHeadPosition());
						break;
					}

					BlockPos probedBlockPosition = probingState.getHeadPosition().down();
					BlockState probedBlock = world.getBlockState(probedBlockPosition);

					// TODO Figure out a better criteria for water and other fluids
					if (!probedBlock.isReplaceable()) {
						this.state = new MinerProcessingState.Drilling(probingState.getHeadPosition(), probedBlockPosition);
						break;
					}

					if (probingState.getHeadMovementCooldown() == 0) {
						if (!decrementMiningPipe()){
							this.state = new MinerProcessingState.NoPipe();
							break;
						}
						if (!moveDrillHead(world, Direction.DOWN, probingState.getHeadPosition())){
							//TODO This should basically not happen but change state to drilling maybe?
						}
						probingState.setHeadPosition(probingState.getHeadPosition().down());
						probingState.setSkipProspectionNextBlock(false);
						probingState.setHeadMovementCooldown((int) (55 * (1.0 - getSpeedMultiplier())) + 5);
					} else {
						probingState.tickProbeMoveCooldown();
					}
				}
				case DRILLING -> {
					MinerProcessingState.Drilling drillingState = (MinerProcessingState.Drilling) this.state;

					BlockState drilledBlock = world.getBlockState(drillingState.getDrilledBlockPosition());

					if (!drillingState.isRemainingDrillingTimeSet()) {
						float hardness = drilledBlock.getHardness(world, drillingState.getDrilledBlockPosition());
						if (hardness < 0) {
							this.state = new MinerProcessingState.Retracting(drillingState.getHeadPosition());
							break;
						}

						drillingState.setRemainingDrillingTime((int) ((30 * hardness) / getToolSpeedMultiplier(drilledBlock)));
						log.info("Block: {} ticksToBreak: {}", drilledBlock, drillingState.getRemainingDrillingTime());
					}

					if (drillingState.isDrillingTimeRemaining()) {
						drillingState.tickRemainingDrillingTime();
					} else {
						List<ItemStack> drops = getBlockDrops(world, drillingState.getDrilledBlockPosition());
						// TODO Check if the drops fit in the output if not, halt the machine
						addToOutputOrEject(world, drops);
						world.breakBlock(drillingState.getDrilledBlockPosition(), false);
						this.state = new MinerProcessingState.Probing(drillingState.getHeadPosition());
					}
				}
				case PROSPECTING -> {
					MinerProcessingState.Prospecting prospectingState = (MinerProcessingState.Prospecting) this.state;

					if (this.blockProspectionIterator == null){
						this.blockProspectionIterator = new BlockProspectingIterable(prospectingState.getHeadPosition(), DEFAULT_PROSPECTING_RANGE, false).iterator();
					}

					int checksPerTickRemaining = (int) (DEFAULT_PROSPECTING_SPEED * (1.0 + getSpeedMultiplier()));

					while(checksPerTickRemaining > 0){
						if (!this.blockProspectionIterator.hasNext()){
							this.blockProspectionIterator = null;
							this.state = new MinerProcessingState.DrillingOre(prospectingState.getHeadPosition(), prospectingState.getProspectedBlocks());
							break;
						}

						BlockPos prospectedBlockPosition = this.blockProspectionIterator.next();

						BlockState prospectedBlockState = world.getBlockState(prospectedBlockPosition);

						if (checkDesirable(prospectedBlockState)){
							prospectingState.addProspectedBlock(prospectedBlockPosition);
						}
						checksPerTickRemaining--;
					}
				}
				case DRILLING_ORE -> {
					MinerProcessingState.DrillingOre drillingOreState = (MinerProcessingState.DrillingOre) this.state;

					if(!drillingOreState.isRemainingDrillingTimeSet()){
						BlockPos prospectedBlock = drillingOreState.getNextProspectedBlock();

						if (prospectedBlock == null){
							this.state = new MinerProcessingState.Probing(pos.withY(drillingOreState.getHeadPosition().getY()), true);
							break;
						}

						BlockState prospectedBlockState = world.getBlockState(prospectedBlock);

						float hardness = prospectedBlockState.getHardness(world, prospectedBlock);
						if (hardness < 0) {
							this.state = new MinerProcessingState.Retracting(drillingOreState.getHeadPosition());
							break;
						}

						drillingOreState.setRemainingDrillingTime((int) ((30 * hardness) / getToolSpeedMultiplier(prospectedBlockState)));
					}

					// If we still have to drill
					if (drillingOreState.isDrillingTimeRemaining()) {
						drillingOreState.tickRemainingDrillingTime();
					} else {
						BlockPos prospectedBlock = drillingOreState.getNextProspectedBlock();
						List<ItemStack> drops = getBlockDrops(world, prospectedBlock);
						// TODO Check if the drops fit in the output if not, halt the machine
						addToOutputOrEject(world, drops);
						world.breakBlock(prospectedBlock, false);
						drillingOreState.completeNextProspectedBlock();
						drillingOreState.setRemainingDrillingTime(-1);
					}

				}
				case RETRACTING -> {
					MinerProcessingState.Retracting retractionState = (MinerProcessingState.Retracting) this.state;
					BlockPos nextPosition = retractionState.getHeadPosition();
					if (world.getBlockState(nextPosition).isOf(TRContent.MINING_PIPE)) {
						world.removeBlock(nextPosition, false);
						if (incrementMiningPipe()){
							//TODO Eject the mining pipe if it is too much
						}
						retractionState.setHeadPosition(nextPosition.up());
					} else {
						this.state = new MinerProcessingState.Done();
					}
				}
				case DONE -> {

				}
			}
		}

	}

	@Override
	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState) {
		BlockPos nextPosition = blockPos.down();
		while (world.getBlockState(nextPosition).isOf(TRContent.MINING_PIPE)) {
			world.breakBlock(nextPosition, true);
			nextPosition = nextPosition.down();
		}
	}

	public boolean moveDrillHead(World world, final Direction direction, final BlockPos oldPosition){
		BlockPos nextPosition = oldPosition.offset(direction);

		BlockState oldPositionBlockState = world.getBlockState(oldPosition);
		BlockState nextPositionOldState = world.getBlockState(nextPosition);

		BlockState oldPositionNewState;

		BlockState newPositionNewState;

		if (nextPositionOldState.isOf(TRContent.MINING_PIPE)) {
			// If we're going against the direction of the pipe
			oldPositionNewState = Blocks.AIR.getDefaultState();
			newPositionNewState = TRContent.MINING_PIPE.getDefaultState().with(MiningPipeBlock.TYPE, MiningPipeBlock.MiningPipeType.DRILL);

		} else if (nextPositionOldState.isReplaceable()) {
			newPositionNewState = TRContent.MINING_PIPE.getDefaultState().with(MiningPipeBlock.TYPE, MiningPipeBlock.MiningPipeType.DRILL);
			oldPositionNewState = TRContent.MINING_PIPE.getDefaultState().with(MiningPipeBlock.TYPE, MiningPipeBlock.MiningPipeType.PIPE);
		} else {
			return false;
		}

		if(oldPositionBlockState.isOf(TRContent.Machine.MINER.block)){
			oldPositionNewState = null;
		}

		if(newPositionNewState != null){
			world.setBlockState(nextPosition, newPositionNewState);
		}

		if(oldPositionNewState != null){
			world.setBlockState(oldPosition, oldPositionNewState);
		}

		return true;
	}


	public List<ItemStack> getBlockDrops(World world, BlockPos pos){
		BlockState blockState = world.getBlockState(pos);
		if (world instanceof ServerWorld serverWorld){
			LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(serverWorld)
					.add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
					.add(LootContextParameters.TOOL, this.inventory.getStack(MINING_TOOL_SLOT));
			List<ItemStack> droppedStacks = blockState.getDroppedStacks(builder);
			log.info("{} drops {}", blockState.getBlock(), droppedStacks);
			return droppedStacks;
		}
		return Collections.singletonList(blockState.getBlock().asItem().getDefaultStack());
	}

	public boolean addToOutputOrEject(World world, List<ItemStack> addedItems){
		List<ItemStack> remainder = addToOutputSlotsWithRemainder(addedItems);
		Vec3d ejectPosition = this.pos.toCenterPos().add(0,0.5,0);
		boolean ejectedItems = false;
		for(ItemStack remainderStack : remainder){
			if (!remainderStack.isEmpty()){
				ejectedItems = true;
				ItemEntity spawnedEntity = new ItemEntity(world, ejectPosition.x, ejectPosition.y, ejectPosition.z, remainderStack);
				spawnedEntity.setVelocity(0,0.2,0);
				world.spawnEntity(spawnedEntity);
			}
		}
		return ejectedItems;
	}

	public List<ItemStack> addToOutputSlotsWithRemainder(List<ItemStack> addedItems){
		Inventory inventory = this.getInventory();
		for(ItemStack addedItemStack : addedItems){
			for (int slot=0; slot < OUTPUT_INVENTORY_SIZE; slot++){
				final ItemStack inventoryStack = inventory.getStack(OUTPUT_INVENTORY_START + slot);
				if (inventoryStack.isOf(addedItemStack.getItem()) && inventoryStack.getCount() < inventoryStack.getMaxCount()){
					int transferredAmount = Math.min(addedItemStack.getCount(), inventoryStack.getMaxCount() - inventoryStack.getCount());
					addedItemStack.decrement(transferredAmount);
					inventoryStack.increment(transferredAmount);
				}
				if (addedItemStack.isEmpty()){
					break;
				}
			}

			for (int slot=0; slot < OUTPUT_INVENTORY_SIZE; slot++){
				final ItemStack inventoryStack = inventory.getStack(OUTPUT_INVENTORY_START + slot);
				if (inventoryStack.isEmpty()){
					inventory.setStack(OUTPUT_INVENTORY_START + slot, addedItemStack.copyAndEmpty());
				}
			}
		}

		return addedItems.stream().filter(itemStack -> !itemStack.isEmpty()).toList();
	}

	public boolean checkDesirable(BlockState state){
		return state.isIn(ConventionalBlockTags.ORES);
	}

	public boolean hasMiningTool() {
		ItemStack miningTool = this.inventory.getStack(MINING_TOOL_SLOT);
		return MinerBlockEntity.canUseAsMiningTool(miningTool);
	}

	public boolean hasMiningPipe() {
		for (int slot = 0; slot < MINING_PIPE_INVENTORY_SIZE; slot++){
			ItemStack itemStack = this.inventory.getStack(MINING_PIPE_INVENTORY_START + slot);
			if (itemStack.isOf(TRContent.MINING_PIPE.asItem())){
				return true;
			}
		}
		return false;
	}

	int lastIncrementedMiningPipeStack = -1;

	public boolean incrementMiningPipe(){
		// Increment last incremented stack
		if (lastIncrementedMiningPipeStack != -1){
			ItemStack itemStack = this.inventory.getStack(lastIncrementedMiningPipeStack);
			if (itemStack.getCount() < itemStack.getMaxCount()){
				itemStack.increment(1);
				return true;
			}else{
				lastIncrementedMiningPipeStack = -1;
			}
		}
		int firstEmptyStack = -1;
		// Find another stack
		for (int slot = 0; slot < MINING_PIPE_INVENTORY_SIZE; slot++){
			ItemStack itemStack = this.inventory.getStack(MINING_PIPE_INVENTORY_START + slot);
			if (!itemStack.isEmpty()){
				if (itemStack.getCount() < itemStack.getMaxCount()){
					itemStack.increment(1);
					lastIncrementedMiningPipeStack = slot;
					return true;
				}
			}else{
				if (firstEmptyStack == -1){
					firstEmptyStack = slot;
				}
			}
		}
		return false;
	}

	public boolean decrementMiningPipe(){
		for (int slot = 0; slot < MINING_PIPE_INVENTORY_SIZE; slot++){
			ItemStack itemStack = this.inventory.getStack(MINING_PIPE_INVENTORY_START + slot);
			if (!itemStack.isEmpty()){
				itemStack.decrement(1);
				return true;
			}
		}
		return false;
	}

	public boolean hasProspectingTool() {
		ItemStack scanner = this.inventory.getStack(PROSPECTING_TOOL_SLOT);
		return !scanner.isEmpty();
	}

	public float getToolSpeedMultiplier(BlockState blockState) {
		Item toolItem = this.inventory.getStack(MINING_TOOL_SLOT).getItem();
		if (toolItem instanceof RcEnergyItem energyItem) {
			// TODO Investigate for performance
			// FakeItem hack so tool is always charged.
			ItemStack fakeTool = new ItemStack(toolItem);
			energyItem.setStoredEnergy(fakeTool, energyItem.getEnergyCapacity(fakeTool));
			return fakeTool.getMiningSpeedMultiplier(blockState);
		}
		return this.inventory.getStack(MINING_TOOL_SLOT).getMiningSpeedMultiplier(blockState);
	}

	static class BlockProspectingIterable implements Iterable<BlockPos> {

		final BlockPos probePosition;
		final int probeRange;
		final boolean clockwise;

		int offsetX;
		int offsetZ;
		int direction;


		public BlockProspectingIterable(BlockPos probePosition, int probeRange, boolean rotateClockwise) {
			this.probePosition = probePosition;
			this.probeRange = probeRange;
			this.clockwise = rotateClockwise;
			this.offsetX = 1;
			this.offsetZ = 0;
			this.direction = 0;
		}

		@NotNull
		@Override
		public Iterator<BlockPos> iterator() {
			return new Iterator<>() {
				@Override
				public boolean hasNext() {
					return offsetX + offsetZ < probeRange;
				}

				@Override
				public BlockPos next() {
					BlockPos nextPosition = probePosition.add(offsetX, 0, offsetZ);
					// rotate the offset by 90 degrees
					int temp = offsetZ;
					offsetZ = offsetX;
					offsetX = temp;

					if (clockwise) {
						offsetZ = -offsetZ;
					} else {
						offsetX = -offsetX;
					}

					// if we rotated so the coordinate is in the first quadrant again
					if (offsetX >= 1 && offsetZ >= 0) {
						// We iterate to the next manhattan distance position
						if (offsetZ == 0) {
							offsetZ = offsetX;
							offsetX = 1;
						} else {
							offsetX++;
							offsetZ--;
						}
					}

					return nextPosition;
				}
			};
		}
	}
}
