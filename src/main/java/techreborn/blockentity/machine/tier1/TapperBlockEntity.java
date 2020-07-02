/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.api.items.InventoryBase;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.client.screen.builder.ScreenHandlerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.RebornInventory;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.blockentity.machine.misc.ChargeOMatBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.config.TechRebornConfig;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static reborncore.api.items.InventoryUtils.getInventoryAt;

public class TapperBlockEntity extends MachineBaseBlockEntity {

	private static final int OUTPUT_SLOT = 0;

	private RebornInventory<TapperBlockEntity> inventory = new RebornInventory<>(1, "TapperBlockEntity", 64, this);


	//TODO LIST
	// Textures
	// Orientable
	// States

	public TapperBlockEntity() {
		super(TRBlockEntities.TAPPER);
	}

	@Override
	public void tick() {

		if(world == null || world.isClient) return;


		if (world.getTime() % 100 != 0) {
			return;
		}

		BlockPos originPos = this.pos.offset(Direction.NORTH);
		BlockState originState = world.getBlockState(originPos);
		Inventory invBelow = getInventoryBelow();

		if(originState.getBlock() != TRContent.RUBBER_LOG || invBelow == null) return;

		HashMap<BlockPos, BlockState> sapLogs = new HashMap<>();

		if(originState.get(BlockRubberLog.HAS_SAP)) {
			sapLogs.put(originPos, originState);
		}

		// Get rubber logs with sap above origin
		addLogsWithSap(originPos, sapLogs);

		// Harvest the sap to inventory, if possible.
		if(harvestSap(sapLogs, invBelow)){
			world.playSound(pos.getX(),pos.getY(),pos.getZ(), ModSounds.SAP_EXTRACT, SoundCategory.BLOCKS, 0.6F, 1F, false);
		}
	}

	private Inventory getInventoryBelow() {
		return getInventoryAt(this.getWorld(), this.pos.offset(Direction.DOWN));
	}

	private boolean harvestSap(HashMap<BlockPos, BlockState> sapLogs, Inventory invBelow){
		// Used for sound
		boolean hasSapped = false;

		for (Map.Entry<BlockPos, BlockState> entry : sapLogs.entrySet()) {
			BlockPos pos = entry.getKey();
			BlockState state = entry.getValue();

			ItemStack out = new ItemStack(TRContent.Parts.SAP, 1);
			out = HopperBlockEntity.transfer(null, invBelow, out, Direction.UP);
			if(out.isEmpty()){
				world.setBlockState(pos, state.with(BlockRubberLog.HAS_SAP, false).with(BlockRubberLog.SAP_SIDE, Direction.fromHorizontal(0)));
				hasSapped = true;
			}else{
				// Can't deposit into inventory, don't sap
				return hasSapped;
			}
		}

		return  hasSapped;
	}

	private void addLogsWithSap(BlockPos originPos, HashMap<BlockPos, BlockState> sapLogs){
		boolean shouldExit = false;

		BlockPos current = originPos;
		// Progress Up (Gravity fed, won't consider sap under current log), origin log has already been checked)
		while(!shouldExit){
			current = current.offset(Direction.UP);

			BlockState state = world.getBlockState(current);
			if(state.getBlock() == TRContent.RUBBER_LOG){
				if( state.get(BlockRubberLog.HAS_SAP)){
					sapLogs.put(current, state);
				}
			}else{
				shouldExit = true;
			}
		}
	}

	@Override
	public boolean hasSlotConfig() {
		return false;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
