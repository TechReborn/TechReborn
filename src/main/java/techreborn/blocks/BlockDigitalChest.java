package techreborn.blocks;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileDigitalChest;
import techreborn.tiles.TileTechStorageBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockDigitalChest extends BlockMachineBase implements IAdvancedRotationTexture {

	private final String prefix = "techreborn:blocks/machine/greg_machines/";

	public BlockDigitalChest() {
		super();
		this.setUnlocalizedName("techreborn.digitalChest");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	protected void dropInventory(final World world, final BlockPos pos) {
		final TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity == null) {
			return;
		}
		if (!(tileEntity instanceof TileTechStorageBase)) {
			return;
		}

		final TileTechStorageBase inventory = (TileTechStorageBase) tileEntity;

		final List<ItemStack> items = new ArrayList<>();

		final List<ItemStack> droppables = inventory.getContentDrops();
		for (int i = 0; i < droppables.size(); i++) {
			final ItemStack itemStack = droppables.get(i);

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

		for (final ItemStack itemStack : items) {
			final Random rand = new Random();

			final float dX = rand.nextFloat() * 0.8F + 0.1F;
			final float dY = rand.nextFloat() * 0.8F + 0.1F;
			final float dZ = rand.nextFloat() * 0.8F + 0.1F;

			final EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ,
					itemStack.copy());

			if (itemStack.hasTagCompound()) {
				entityItem.getEntityItem().setTagCompound(itemStack.getTagCompound().copy());
			}

			final float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntity(entityItem);
			itemStack.setCount(0);
		}
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return new TileDigitalChest();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.DIGITAL_CHEST.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public String getFront(final boolean isActive) {
		return this.prefix + "quantum_chest";
	}

	@Override
	public String getSide(final boolean isActive) {
		return this.prefix + "qchest_side";
	}

	@Override
	public String getTop(final boolean isActive) {
		return this.prefix + "quantum_top";
	}

	@Override
	public String getBottom(final boolean isActive) {
		return this.prefix + "machine_bottom";
	}
}
