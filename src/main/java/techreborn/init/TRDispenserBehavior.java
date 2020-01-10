package techreborn.init;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import reborncore.common.crafting.RebornRecipe;
import techreborn.config.TechRebornConfig;

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

//		DispenserBlock.registerBehavior(TRContent.CELL, new ItemDispenserBehavior(){
//
//		});

	}
}
