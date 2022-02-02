/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.blocks;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import reborncore.api.ToolManager;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.api.blockentity.IUpgrade;
import reborncore.api.blockentity.IUpgradeable;
import reborncore.common.BaseBlockEntityProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidUtils;
import reborncore.common.util.ItemHandlerUtils;
import reborncore.common.util.Tank;
import reborncore.common.util.WrenchUtils;

public abstract class BlockMachineBase extends BaseBlockEntityProvider implements InventoryProvider {

	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

	boolean hasCustomStates;

	public BlockMachineBase() {
		this(Block.Settings.of(Material.METAL).strength(2F, 2F));
	}

	public BlockMachineBase(Block.Settings builder) {
		this(builder, false);
	}

	public BlockMachineBase(Block.Settings builder, boolean hasCustomStates) {
		super(builder);
		this.hasCustomStates = hasCustomStates;
		if (!hasCustomStates) {
			this.setDefaultState(
					this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
		}
		BlockWrenchEventHandler.wrenchableBlocks.add(this);
	}

	public void setFacing(Direction facing, World world, BlockPos pos) {
		if (hasCustomStates) {
			return;
		}
		world.setBlockState(pos, world.getBlockState(pos).with(FACING, facing));
	}

	public Direction getFacing(BlockState state) {
		return state.get(FACING);
	}

	public void setActive(Boolean active, World world, BlockPos pos) {
		if (hasCustomStates) {
			return;
		}
		Direction facing = world.getBlockState(pos).get(FACING);
		BlockState state = world.getBlockState(pos).with(ACTIVE, active).with(FACING, facing);
		world.setBlockState(pos, state, 3);
	}

	public boolean isActive(BlockState state) {
		return state.get(ACTIVE);
	}

	public boolean isAdvanced() {
		return false;
	}

	public abstract IMachineGuiHandler getGui();

	// BaseBlockEntityProvider
	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(worldIn, pos, state, placer, stack);
		setFacing(placer.getHorizontalFacing().getOpposite(), worldIn, pos);

		BlockEntity blockEntity = worldIn.getBlockEntity(pos);
		if (blockEntity instanceof MachineBaseBlockEntity) {
			((MachineBaseBlockEntity) blockEntity).onPlace(worldIn, pos, state, placer, stack);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return null;
	}

	// Block
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, ACTIVE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStateReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			ItemHandlerUtils.dropContainedItems(worldIn, pos);
			super.onStateReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof MachineBaseBlockEntity) {
			((MachineBaseBlockEntity) blockEntity).onBreak(world, playerEntity, blockPos, blockState);
		}
		super.onBreak(world, blockPos, blockState, playerEntity);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(getInventory(state, world, pos));
	}

	/*
	 *  Right-click should open GUI for all non-wrench items
	 *  Shift-Right-click should apply special action, like fill\drain bucket, install behavior, etc.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hitResult) {

		ItemStack stack = playerIn.getStackInHand(hand);
		BlockEntity blockEntity = worldIn.getBlockEntity(pos);

		// We extended BlockTileBase. Thus, we should always have blockEntity entity. I hope.
		if (blockEntity == null) {
			return ActionResult.PASS;
		}

		if (blockEntity instanceof MachineBaseBlockEntity) {
			Tank tank = ((MachineBaseBlockEntity) blockEntity).getTank();
			if (tank != null && FluidUtils.interactWithFluidHandler(playerIn, hand, tank)) {
				return ActionResult.SUCCESS;
			}
		}

		if (!stack.isEmpty()) {
			if (ToolManager.INSTANCE.canHandleTool(stack)) {
				if (WrenchUtils.handleWrench(stack, worldIn, pos, playerIn, hitResult.getSide())) {
					return ActionResult.SUCCESS;
				}
			} else if (stack.getItem() instanceof IUpgrade && blockEntity instanceof IUpgradeable upgradeableEntity) {
				if (upgradeableEntity.canBeUpgraded()) {
					int inserted = (int) insertItemStacked(
							InventoryStorage.of(upgradeableEntity.getUpgradeInventory(), null),
							ItemVariant.of(stack),
							stack.getCount()
					);
					if (inserted > 0) {
						stack.decrement(inserted);
						return ActionResult.success(worldIn.isClient());
					}
				}
			}
		}

		if (getGui() != null && !playerIn.isSneaking()) {
			getGui().open(playerIn, pos, worldIn);
			return ActionResult.SUCCESS;
		}

		return super.onUse(state, worldIn, pos, playerIn, hand, hitResult);
	}

	// TODO: use the fabric one when it will be PR'ed.
	@SuppressWarnings({"deprecation", "UnstableApiUsage"})
	public static long insertItemStacked(InventoryStorage inventory, ItemVariant variant, long maxAmount) {
		long inserted = 0;
		try (Transaction tx = Transaction.openOuter()) {
			outer: for (int loop = 0; loop < 2; ++loop) {
				for (SingleSlotStorage<ItemVariant> slot : inventory.getSlots()) {
					if (slot.getResource().equals(variant) || loop == 1) {
						inserted += slot.insert(variant, maxAmount - inserted, tx);

						if (inserted == maxAmount) {
							break outer;
						}
					}
				}
			}

			tx.commit();
		}
		return inserted;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	// InventoryProvider
	@Override
	public SidedInventory getInventory(BlockState blockState, WorldAccess world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof MachineBaseBlockEntity) {
			return (MachineBaseBlockEntity) blockEntity;
		}
		return null;
	}
}
