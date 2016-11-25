package techreborn.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

//import ic2.core.item.tool.ItemToolWrench;

//TODO IC2
public class IC2WrenchHelper {

	//static ItemToolWrench wrench;

	public static EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
	                                              EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		//		if (wrench == null) {
		//			wrench = (ItemToolWrench) IC2Items.getItem("wrench").getItem();
		//		}
		//		return wrench.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
		return EnumActionResult.FAIL;
	}

	public static EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		//		if (wrench == null) {
		//			wrench = (ItemToolWrench) IC2Items.getItem("wrench").getItem();
		//		}
		//		return wrench.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		return EnumActionResult.FAIL;
	}
}
