package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.init.ModBlocks;
import techreborn.tiles.TileAesu;

import java.util.List;

public class ItemBlockAesu extends ItemBlock {

	public ItemBlockAesu(Block p_i45328_1_) {
		super(p_i45328_1_);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (stack != null && stack.hasTagCompound()) {
			if (stack.getTagCompound().getCompoundTag("tileEntity") != null)
				list.add(PowerSystem
					.getLocaliszedPower(stack.getTagCompound().getCompoundTag("tileEntity").getInteger("energy")));
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
		if (stack != null && stack.hasTagCompound()) {
			((TileAesu) world.getTileEntity(pos))
				.readFromNBTWithoutCoords(stack.getTagCompound().getCompoundTag("tileEntity"));
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		itemList.add(getDropWithNBT(0));
		itemList.add(getDropWithNBT(1000000000));
	}

	public ItemStack getDropWithNBT(double energy) {
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(ModBlocks.aesu, 1);
		writeToNBTWithoutCoords(tileEntity, energy);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		return dropStack;
	}

	public void writeToNBTWithoutCoords(NBTTagCompound tagCompound, double energy) {
		NBTTagCompound data = new NBTTagCompound();
		data.setDouble("energy", energy);
		tagCompound.setTag("TilePowerAcceptor", data);
		tagCompound.setDouble("energy", energy);
		tagCompound.setDouble("euChange", 0);
		tagCompound.setDouble("euLastTick", 0);
		tagCompound.setBoolean("active", false);
	}
}
