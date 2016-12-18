package techreborn.blocks;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileTechStorageBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockQuantumChest extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockQuantumChest() {
		super();
		setUnlocalizedName("techreborn.quantumChest");
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2.0F);
	}

	@Override
	protected void dropInventory(World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null) {
			return;
		}
		if (!(tileEntity instanceof TileTechStorageBase)) {
			return;
		}

		TileTechStorageBase inventory = (TileTechStorageBase) tileEntity;

		List<ItemStack> items = new ArrayList<ItemStack>();

		List<ItemStack> droppables = inventory.getContentDrops();
		for (int i = 0; i < droppables.size(); i++) {
			ItemStack itemStack = droppables.get(i);

			if (itemStack == ItemStack.EMPTY) {
				continue;
			}
			if (itemStack != ItemStack.EMPTY && itemStack.getCount() > 0) {
				if (itemStack.getItem() instanceof ItemBlock) {
					if (((ItemBlock) itemStack.getItem()).block instanceof BlockFluidBase
							|| ((ItemBlock) itemStack.getItem()).block instanceof BlockStaticLiquid
							|| ((ItemBlock) itemStack.getItem()).block instanceof BlockDynamicLiquid) {
						continue;
					}
				}
			}
			items.add(itemStack.copy());
		}

		for (ItemStack itemStack : items) {
			Random rand = new Random();

			float dX = rand.nextFloat() * 0.8F + 0.1F;
			float dY = rand.nextFloat() * 0.8F + 0.1F;
			float dZ = rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ,
					itemStack.copy());

			if (itemStack.hasTagCompound()) {
				entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
			}

			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntity(entityItem);
			itemStack.setCount(0);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileQuantumChest();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
	                                EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking())
			playerIn.openGui(Core.INSTANCE, GuiHandler.quantumChestID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public String getFront(boolean isActive) {
		return prefix + "quantum_chest";
	}

	@Override
	public String getSide(boolean isActive) {
		return prefix + "qchest_side";
	}

	@Override
	public String getTop(boolean isActive) {
		return prefix + "quantum_top";
	}

	@Override
	public String getBottom(boolean isActive) {
		return prefix + "machine_bottom";
	}
}
