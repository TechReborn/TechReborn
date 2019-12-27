package techreborn.blockentity.machine.tier1;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import techreborn.blocks.lighting.BlockLamp;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.Collections;
import java.util.List;

public class GreenhouseControllerBlockEntity extends PowerAcceptorBlockEntity
		implements IToolDrop, InventoryProvider, IContainerProvider {
	
	private final RebornInventory<GreenhouseControllerBlockEntity> inventory = new RebornInventory<>(7, "GreenhouseControllerBlockEntity", 64, this);
	private BlockPos multiblockCenter;
	private int ticksToNextMultiblockCheck = 0;
	private boolean growthBoost = false;
	
	public GreenhouseControllerBlockEntity() {
		super(TRBlockEntities.GREENHOUSE_CONTROLLER);
	}
	
	public boolean getMultiBlock() {
		if (multiblockCenter == null || world == null) {
			return false;
		}
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				BlockState block = world.getBlockState(multiblockCenter.add(i * 3, 3, i * 3));
				if (!(block.getBlock() instanceof BlockLamp) || !BlockLamp.isActive(block)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void tick() {
		if (multiblockCenter == null) {
			multiblockCenter = pos.offset(getFacing().getOpposite(), 5);
		}
		charge(6);
		super.tick();
		
		if (world.isClient) {
			return;
		}
		
		if (useEnergy(getEuPerTick(TechRebornConfig.greenhouseControllerEnergyPerTick)) != getEuPerTick(TechRebornConfig.greenhouseControllerEnergyPerTick)) {
			return;
		}
		
		if (--ticksToNextMultiblockCheck < 0) {
			growthBoost = getMultiBlock();
			ticksToNextMultiblockCheck = 200;
		}
		
		if (world.getTime() % 20 == 0) {
			double cyclesLimit = getSpeedMultiplier() * 4 + 1;
			while (cyclesLimit-- > 0) {
				workCycle();
			}
		}
		
	}
	
	private void workCycle() {
		BlockPos blockPos = multiblockCenter.add(world.random.nextInt(9) - 4, 0, world.random.nextInt(9) - 4);
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		
		if (growthBoost) {
			if (block instanceof Fertilizable
					|| block instanceof PlantBlock
					|| block instanceof SugarCaneBlock
					|| block instanceof CactusBlock
			) {
				if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerBonemeal)) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerBonemeal);
					blockState.scheduledTick((ServerWorld) world, blockPos, world.random);
				}
			}
		}
		
		if (block instanceof CropBlock) {
			processAgedCrop(blockState, blockPos, ((CropBlock) block).getAgeProperty(), ((CropBlock) block).getMaxAge(), 0);
		} else if (block instanceof NetherWartBlock) {
			processAgedCrop(blockState, blockPos, NetherWartBlock.AGE, 3, 0);
		} else if (block instanceof SweetBerryBushBlock) {
			processAgedCrop(blockState, blockPos, SweetBerryBushBlock.AGE, 3, 1);
		} else if (block instanceof CocoaBlock) {
			processAgedCrop(blockState, blockPos, CocoaBlock.AGE, 2, 0);
		} else if (block instanceof GourdBlock) {
			if (tryHarvestBlock(blockState, blockPos)) {
				world.breakBlock(blockPos, false);
			}
		} else if (block instanceof SugarCaneBlock
				|| block instanceof CactusBlock
				|| block instanceof BambooBlock
		) {
			// If we can break bottom block we should at least remove all of them up to top so they don't break automatically
			boolean breakBlocks = false;
			for (int y = 1; (blockState = world.getBlockState(blockPos.up(y))).getBlock() == block; y++) {
				if (y == 1) {
					breakBlocks = tryHarvestBlock(blockState, blockPos.up(y));
				} else {
					tryHarvestBlock(blockState, blockPos.up(y));
				}
				if (breakBlocks) world.breakBlock(blockPos.up(y), false);
			}
		} else if (block instanceof BlockRubberLog) {
			for (int y = 1; (blockState = world.getBlockState(blockPos.up(y))).getBlock() == block && y < 10; y++) {
				if (blockState.get(BlockRubberLog.HAS_SAP)
						&& canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)
						&& insertIntoInv(Collections.singletonList(TRContent.Parts.SAP.getStack()))
				) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					world.setBlockState(blockPos.up(y), blockState.with(BlockRubberLog.HAS_SAP, false).with(BlockRubberLog.SAP_SIDE, Direction.fromHorizontal(0)));
				}
			}
		}
		
	}
	
	private void processAgedCrop(BlockState blockState, BlockPos blockPos, IntProperty ageProperty, int maxAge, int newAge) {
		if (blockState.get(ageProperty) >= maxAge) {
			if (tryHarvestBlock(blockState, blockPos)) {
				world.setBlockState(blockPos, blockState.with(ageProperty, newAge), 2);
			}
		}
	}
	
	private boolean tryHarvestBlock(BlockState blockState, BlockPos blockPos) {
		if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)
				&& insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null))) {
			useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
			return true;
		}
		return false;
	}
	
	private boolean insertIntoInv(List<ItemStack> stacks) {
		boolean result = false;
		for (ItemStack stack : stacks) {
			for (int i = 0; i < 6; i++) {
				if (insertIntoInv(i, stack)) result = true;
				if (stack.isEmpty()) break;
			}
		}
		return result;
	}
	
	private boolean insertIntoInv(int slot, ItemStack stack) {
		ItemStack targetStack = inventory.getInvStack(slot);
		if (targetStack.isEmpty()) {
			inventory.setInvStack(slot, stack.copy());
			stack.decrement(stack.getCount());
			return true;
		} else {
			if (ItemUtils.isItemEqual(stack, targetStack, true, false)) {
				int freeStackSpace = targetStack.getMaxCount() - targetStack.getCount();
				if (freeStackSpace > 0) {
					int transferAmount = Math.min(freeStackSpace, stack.getCount());
					targetStack.increment(transferAmount);
					stack.decrement(transferAmount);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canAcceptEnergy(Direction direction) {
		return true;
	}
	
	@Override
	public boolean canProvideEnergy(Direction direction) {
		return false;
	}
	
	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.greenhouseControllerMaxEnergy;
	}
	
	@Override
	public double getBaseMaxOutput() {
		return 0;
	}
	
	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.greenhouseControllerMaxInput;
	}
	
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.GREENHOUSE_CONTROLLER.getStack();
	}
	
	@Override
	public RebornInventory<GreenhouseControllerBlockEntity> getInventory() {
		return this.inventory;
	}
	
	@Override
	public boolean canBeUpgraded() {
		return true;
	}
	
	@Override
	public BuiltContainer createContainer(int syncID, PlayerEntity player) {
		return new ContainerBuilder("greenhousecontroller").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this)
				.outputSlot(0, 30, 22).outputSlot(1, 48, 22)
				.outputSlot(2, 30, 40).outputSlot(3, 48, 40)
				.outputSlot(4, 30, 58).outputSlot(5, 48, 58)
				.energySlot(6, 8, 72).syncEnergyValue()
				.addInventory().create(this, syncID);
	}
	
}
