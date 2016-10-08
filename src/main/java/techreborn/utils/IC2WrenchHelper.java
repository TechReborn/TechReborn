package techreborn.utils;

import ic2.api.item.IC2Items;
import ic2.core.item.tool.ItemToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IC2WrenchHelper {

	static ItemToolWrench wrench;

	public static EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
	                                              EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (wrench == null) {
			wrench = (ItemToolWrench) IC2Items.getItem("wrench").getItem();
		}
		return wrench.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
	}
}
