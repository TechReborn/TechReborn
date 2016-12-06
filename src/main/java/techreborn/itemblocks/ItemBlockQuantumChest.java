package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.tiles.TileQuantumChest;

import java.util.List;

public class ItemBlockQuantumChest extends ItemBlock {

	public ItemBlockQuantumChest(Block p_i45328_1_) {
		super(p_i45328_1_);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (stack != ItemStack.EMPTY && stack.hasTagCompound()) {
			if (stack.getTagCompound().getCompoundTag("tileEntity") != null)
				list.add(stack.getTagCompound().getCompoundTag("tileEntity").getInteger("storedQuantity") + " items");
		}
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
		if (stack != ItemStack.EMPTY && stack.hasTagCompound()) {
			((TileQuantumChest) world.getTileEntity(pos))
				.readFromNBTWithoutCoords(stack.getTagCompound().getCompoundTag("tileEntity"));
		}
		return true;
	}
}
