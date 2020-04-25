package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.IWorld;
import reborncore.common.crafting.RebornRecipe;
import techreborn.config.TechRebornConfig;
import techreborn.items.DynamicCellItem;

import java.util.List;
import java.util.Random;

/**
 * Created by drcrazy on 10-Jan-20 for TechReborn-1.15.
 */
public class TRDispenserBehavior {

	public static void init(){
		if (TechRebornConfig.dispenseScrapboxes) {
			DispenserBlock.registerBehavior(TRContent.SCRAP_BOX, new ItemDispenserBehavior() {
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack){
					List<RebornRecipe> scrapboxRecipeList = ModRecipes.SCRAPBOX.getRecipes(pointer.getWorld());
					int random = new Random().nextInt(scrapboxRecipeList.size());
					ItemStack out = scrapboxRecipeList.get(random).getOutputs().get(0);
					stack.split(1);

					Direction facing = pointer.getBlockState().get(DispenserBlock.FACING);
					Position position = DispenserBlock.getOutputLocation(pointer);
					spawnItem(pointer.getWorld(), out, 6, facing, position);
					return stack;
				}
			} );
		}

		DispenserBlock.registerBehavior(TRContent.CELL, new ItemDispenserBehavior(){
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				DynamicCellItem cell = (DynamicCellItem)stack.getItem();
				IWorld iWorld = pointer.getWorld();
				BlockPos blockPos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				BlockState blockState = iWorld.getBlockState(blockPos);
				Block block = blockState.getBlock();
				if (cell.getFluid(stack) == Fluids.EMPTY){
					// fill cell
					if (block instanceof FluidDrainable) {
						Fluid fluid = ((FluidDrainable)block).tryDrainFluid(iWorld, blockPos, blockState);
						if (!(fluid instanceof BaseFluid)) {
							return super.dispenseSilently(pointer, stack);
						} else {
							ItemStack filledCell = DynamicCellItem.getCellWithFluid(fluid, 1);
							if (stack.getCount() == 1) {
								stack = filledCell;
							} else {
								stack.decrement(1);
								if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(filledCell) < 0) {
									this.dispense(pointer, filledCell);
								}
							}
							return stack;
						}
					} else {
						return super.dispenseSilently(pointer, stack);
					}
				}
				else {
					// drain cell
					if (cell.placeFluid(null, pointer.getWorld(), blockPos, null, stack)) {
						ItemStack emptyCell = cell.getEmpty();
						if(stack.getCount() == 1) {
							stack = emptyCell;
						} else {
							stack.decrement(1);
							if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(emptyCell) < 0) {
								this.dispense(pointer, emptyCell);
							}
						}
					}
					return stack;
				}
			}
		});
	}
}
