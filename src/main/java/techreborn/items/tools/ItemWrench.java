package techreborn.items.tools;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.context.BlockPosContext;
import reborncore.common.IWrenchable;
import reborncore.common.tile.TileMachineBase;
import reborncore.common.util.RebornPermissions;
import techreborn.blocks.fluid.BlockFluidBase;
import techreborn.blocks.storage.BlockEnergyStorage;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.compat.CompatManager;
import techreborn.init.ModSounds;
import techreborn.items.ItemTRNoDestroy;
import techreborn.utils.IC2WrenchHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 26/02/2016.
 */
public class ItemWrench extends ItemTRNoDestroy {

	public ItemWrench() {
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.wrench");
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos,
	                                       EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (!PermissionAPI.hasPermission(player.getGameProfile(), RebornPermissions.WRENCH_BLOCK, new BlockPosContext(player, pos, world.getBlockState(pos), side))) {
			return EnumActionResult.FAIL;
		}
		if (CompatManager.isIC2Loaded) {
			EnumActionResult result = IC2WrenchHelper.onItemUseFirst(player.getHeldItem(hand), player, world, pos, side, hitX, hitY, hitZ, hand);
			if (result == EnumActionResult.SUCCESS) {
				return result;
			}
		}
		if (world.isAirBlock(pos)) {
			return EnumActionResult.FAIL;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (tile == null) {
			return EnumActionResult.FAIL;
		}

		if (!player.isSneaking()) {
			if (tile instanceof TileMachineBase) {
				if (side != EnumFacing.DOWN && side != EnumFacing.UP) {
					((TileMachineBase) tile).setFacing(side);
					return EnumActionResult.SUCCESS;
				}
			}
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof BlockEnergyStorage) {
				EnumFacing facing = state.getValue(BlockEnergyStorage.FACING);
				if (facing.getOpposite() == side) {
					facing = side;
				} else {
					facing = side.getOpposite();
				}
				world.setBlockState(pos, state.withProperty(BlockEnergyStorage.FACING, facing));
				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
	                                  EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!PermissionAPI.hasPermission(player.getGameProfile(), RebornPermissions.WRENCH_BLOCK, new BlockPosContext(player, pos, world.getBlockState(pos), facing))) {
			return EnumActionResult.FAIL;
		}
		if (CompatManager.isIC2Loaded) {
			EnumActionResult result = IC2WrenchHelper.onItemUse(player.getHeldItem(hand), player, world, pos, hand, facing, hitX, hitY, hitZ);
			if (result == EnumActionResult.SUCCESS) {
				return result;
			}
		}
		if (world.isAirBlock(pos)) {
			return EnumActionResult.FAIL;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (tile == null) {
			return EnumActionResult.FAIL;
		}
		if (!world.isRemote) {
			if (player.isSneaking()) {
				List<ItemStack> items = new ArrayList<>();
				if (tile instanceof IInventory) {
					IInventory inventory = (IInventory) tile;
					for (int i = 0; i < inventory.getSizeInventory(); i++) {
						ItemStack itemStack = inventory.getStackInSlot(i);

						if (itemStack != ItemStack.EMPTY) {
							if (itemStack.getCount() > 0) {
								if (itemStack.getItem() instanceof ItemBlock)

									if (!(((ItemBlock) itemStack.getItem()).block instanceof BlockFluidBase) || !(((ItemBlock) itemStack.getItem()).block instanceof BlockStaticLiquid)
										|| !(((ItemBlock) itemStack.getItem()).block instanceof BlockDynamicLiquid)) {
										items.add(itemStack.copy());
									}
							}
						}
					}
					if (tile instanceof IWrenchable) {
						if (((IWrenchable) tile).wrenchCanRemove(player)) {
							ItemStack itemStack = ((IWrenchable) tile).getWrenchDrop(player);
							if (itemStack == null) {
								return EnumActionResult.FAIL;
							}
							items.add(itemStack);
						}
						if (!items.isEmpty()) {
							for (ItemStack itemStack : items) {

								Random rand = new Random();

								float dX = rand.nextFloat() * 0.8F + 0.1F;
								float dY = rand.nextFloat() * 0.8F + 0.1F;
								float dZ = rand.nextFloat() * 0.8F + 0.1F;

								EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY,
									pos.getZ() + dZ, itemStack.copy());

								if (itemStack.hasTagCompound()) {
									entityItem.getEntityItem()
										.setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
								}

								float factor = 0.05F;
								entityItem.motionX = rand.nextGaussian() * factor;
								entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
								entityItem.motionZ = rand.nextGaussian() * factor;
								if (!world.isRemote) {
									world.spawnEntity(entityItem);
								}
							}
						}
						world.playSound(null, player.posX, player.posY,
							player.posZ, ModSounds.dismantle,
							SoundCategory.BLOCKS, 0.6F, 1F);
						if (!world.isRemote) {
							world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
						}
						return EnumActionResult.SUCCESS;
					}

				}
			}
			return EnumActionResult.FAIL;
		} else {
			return EnumActionResult.FAIL;
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
}
