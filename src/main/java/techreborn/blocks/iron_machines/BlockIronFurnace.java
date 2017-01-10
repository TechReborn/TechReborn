package techreborn.blocks.iron_machines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IRotationTexture;

import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileIronFurnace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockIronFurnace extends BlockMachineBase implements IRotationTexture {

	private final String prefix = "techreborn:blocks/machine/iron_machines/";

	public BlockIronFurnace() {
		super();
		this.setUnlocalizedName("techreborn.ironfurnace");
		this.setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int p_149915_2_) {
		return new TileIronFurnace();
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX,
			final float hitY, final float hitZ) {
		if (!player.isSneaking())
			player.openGui(Core.INSTANCE, EGui.IRON_FURNACE.ordinal(), world, x, y, z);
		return true;
	}

	@Override
	public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		final List<ItemStack> items = new ArrayList<>();
		items.add(new ItemStack(this));
		return items;
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings("incomplete-switch")
	public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
		if (this.isActive(state)) {
			final EnumFacing enumfacing = state.getValue(BlockMachineBase.FACING);
			final double d0 = pos.getX() + 0.5D;
			final double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			final double d2 = pos.getZ() + 0.5D;
			final double d3 = 0.52D;
			final double d4 = rand.nextDouble() * 0.6D - 0.3D;

			switch (enumfacing) {
				case WEST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D,
							new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case EAST:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D,
							new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case NORTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D,
							new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
					break;
				case SOUTH:
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D,
							new int[0]);
					worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}

	@Override
	public String getFrontOff() {
		return this.prefix + "iron_furnace_front_off";
	}

	@Override
	public String getFrontOn() {
		return this.prefix + "iron_furnace_front_on";
	}

	@Override
	public String getSide() {
		return this.prefix + "iron_machine_side";
	}

	@Override
	public String getTop() {
		return this.prefix + "iron_machine_top";
	}

	@Override
	public String getBottom() {
		return this.prefix + "iron_machine_bottom";
	}
}
