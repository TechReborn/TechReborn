package techreborn.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.multiblock.TileIndustrialBlastFurnace;
import techreborn.utils.ItemStackUtils;

import java.util.List;

public class ItemBlockIndustrialBlastFurnace extends ItemBlock {
	public ItemBlockIndustrialBlastFurnace(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
		if (!ItemStackUtils.isEmpty(stack)) {
			NBTTagCompound nbt = ItemStackUtils.getStackNbtData(stack);
			if (nbt.hasKey("coils")) {
				byte coils = nbt.getByte("coils");
				switch (coils) {
					case 1:
						list.add("Is equipped with Kanthal Heating Coils");
						break;
					case 2:
						list.add("Is equipped with Kanthal Heating Coils");
						list.add("Is equipped with Nichrome Heating Coils");
						break;
				}
			}
		}
	}

//	@Override
//	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
//		if (!world.setBlockState(pos, newState)) return false;
//
//		if (world.getBlockState(pos).getBlock() == block) world.getBlockState(pos).getBlock().onBlockPlacedBy(world, pos, newState, player, stack);
//
//		if (!ItemStackUtils.isEmpty(stack) && stack.hasTagCompound()) {
//			TileEntity tileEntity = world.getTileEntity(pos);
//			if (tileEntity instanceof TileIndustrialBlastFurnace) {
//				NBTTagCompound nbt = ItemStackUtils.getStackNbtData(stack);
//				((TileIndustrialBlastFurnace) tileEntity).coils = nbt.getByte("coils");
//			}
//		}
//
//		return true;
//	}
}
