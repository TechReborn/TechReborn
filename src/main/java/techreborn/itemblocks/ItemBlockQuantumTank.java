package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.tiles.TileQuantumTank;

public class ItemBlockQuantumTank extends ItemBlock {

	public ItemBlockQuantumTank(Block block) {
		super(block);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
	                            float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!world.setBlockState(pos, newState)) {
			return false;
		}
		if (world.getBlockState(pos).getBlock() == block) {
			world.getBlockState(pos).getBlock().onBlockPlacedBy(world, pos, newState, player, stack);
			// world.getBlockState(pos).getBlock().onPostBlockPlaced(world, x,
			// y, z, metadata);
		}
		if (stack != null && stack.hasTagCompound()) {
			((TileQuantumTank) world.getTileEntity(pos))
				.readFromNBTWithoutCoords(stack.getTagCompound().getCompoundTag("tileEntity"));
		}
		return true;
	}
}
