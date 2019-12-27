package techreborn.blockentity.machine.tier1;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
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
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

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
			// Normal crop
			CropBlock crop = (CropBlock) block;
			if (crop.isMature(blockState)) {
				if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null));
					world.setBlockState(blockPos, crop.withAge(0), 2);
				}
			}
		} else if (block instanceof NetherWartBlock) {
			// Why it isn't a CropBlock? :(
			if (blockState.get(NetherWartBlock.AGE) >= 3) {
				if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null));
					world.setBlockState(blockPos, blockState.with(NetherWartBlock.AGE, 0), 2);
				}
			}
		} else if (block instanceof SweetBerryBushBlock) {
			if (blockState.get(SweetBerryBushBlock.AGE) >= 3) {
				if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null));
					world.setBlockState(blockPos, blockState.with(SweetBerryBushBlock.AGE, 1), 2);
				}
			}
		} else if (block instanceof CocoaBlock) {
			if (blockState.get(CocoaBlock.AGE) >= 2) {
				if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null));
					world.setBlockState(blockPos, blockState.with(CocoaBlock.AGE, 0), 2);
				}
			}
		} else if (block instanceof GourdBlock) {
			// Pumpkin, Melon
			if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)) {
				useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
				insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos, null));
				world.breakBlock(blockPos, false);
			}
		} else if (block instanceof SugarCaneBlock
				|| block instanceof CactusBlock
				|| block instanceof BambooBlock
		) {
			for (int y = 1; world.getBlockState(blockPos.up(y)).getBlock() == block; y++) {
				if (canUseEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest)) {
					useEnergy(TechRebornConfig.greenhouseControllerEnergyPerHarvest);
					insertIntoInv(Block.getDroppedStacks(blockState, (ServerWorld) world, blockPos.up(y), null));
				}
				world.breakBlock(blockPos.up(y), false);
			}
		}
		
	}
	
	private void insertIntoInv(List<ItemStack> stacks) {
		stacks.forEach(stack -> {
			for (int i = 0; i < 6; i++) {
				if (insertIntoInv(i, stack).isEmpty()) {
					break;
				}
			}
		});
	}
	
	private ItemStack insertIntoInv(int slot, ItemStack stack) {
		ItemStack targetStack = inventory.getInvStack(slot);
		if (targetStack.isEmpty()) {
			inventory.setInvStack(slot, stack);
			return ItemStack.EMPTY;
		} else {
			if (ItemUtils.isItemEqual(stack, targetStack, true, false)) {
				int freeStackSpace = targetStack.getMaxCount() - targetStack.getCount();
				if (freeStackSpace > 0) {
					int transferAmount = Math.min(freeStackSpace, stack.getCount());
					targetStack.increment(transferAmount);
					stack.decrement(transferAmount);
				}
			}
			
			return stack;
		}
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
