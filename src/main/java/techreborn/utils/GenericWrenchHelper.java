package techreborn.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.ICustomToolHandler;

public class GenericWrenchHelper implements ICustomToolHandler {

	ResourceLocation itemLocation;
	boolean damage;

	public GenericWrenchHelper(ResourceLocation itemLocation, boolean damage) {
		this.itemLocation = itemLocation;
		this.damage = damage;
	}

	@Override
	public boolean canHandleTool(ItemStack stack) {
		return stack.getItem().getRegistryName().equals(itemLocation);
	}

	@Override
	public boolean handleTool(ItemStack stack, BlockPos pos, World world, EntityPlayer player, EnumFacing side, boolean damage) {
		if(this.damage && damage){
			stack.damageItem(1, player);
		}
		return true;
	}
}
