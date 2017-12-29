package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.lib.ModInfo;
import techreborn.tiles.TileAlarm;

import javax.annotation.Nullable;

//TODO: On/off variants, Placeable on walls/roofs(faces), Visible when holding in hand, and recipe.

public class BlockAlarm extends BaseTileBlock {
	public static PropertyDirection FACING;
	public static PropertyBool ACTIVE;

	private static AxisAlignedBB[] GenBoundingBoxes(double depth, double width) {
		AxisAlignedBB[] bb = {
			new AxisAlignedBB(width, 1.0 - depth, width, 1.0 - width, 1.0D, 1.0 - width),
			new AxisAlignedBB(width, 0.0D, width, 1.0 - width, depth, 1.0 - width),
			new AxisAlignedBB(width, width, 1.0 - depth, 1.0 - width, 1.0 - width, 1.0D),
			new AxisAlignedBB(width, width, 0.0D, 1.0 - width, 1.0 - width, depth),
			new AxisAlignedBB(1.0 - depth, width, width, 1.0D, 1.0 - width, 1.0 - width),
			new AxisAlignedBB(0.0D, width, width, depth, 1.0 - width, 1.0 - width),
		};
		return bb;
	}

	private AxisAlignedBB[] bbs;

	public BlockAlarm() {
		super(Material.ROCK);
		setUnlocalizedName("techreborn.alarm");
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
		this.bbs = GenBoundingBoxes(0.19, 0.81);
		setCreativeTab(TechRebornCreativeTab.instance);
		ShootingStar.registerModel(new ModelCompound(ModInfo.MOD_ID, this, "machines/lighting"));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		FACING = PropertyDirection.create("facing");
		ACTIVE = PropertyBool.create("active");
		return new BlockStateContainer(this, FACING, ACTIVE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int facingInt = state.getValue(FACING).getIndex();
		int activeInt = state.getValue(ACTIVE) ? 8 : 0;
		return facingInt + activeInt;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		Boolean active = (meta&8)==8;
		EnumFacing facing = EnumFacing.getFront(meta&7);
		return this.getDefaultState().withProperty(FACING, facing).withProperty(ACTIVE, active);
	}

	public static boolean isActive(IBlockState state) {
		return state.getValue(ACTIVE);
	}

	public static EnumFacing getFacing(IBlockState state) {
		return (EnumFacing)state.getValue(FACING);
	}

	public static void setFacing(EnumFacing facing, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, facing));
	}

	public static void setActive(Boolean active, World world, BlockPos pos) {
		EnumFacing facing = (EnumFacing)world.getBlockState(pos).getValue(FACING);
		IBlockState state = world.getBlockState(pos).withProperty(ACTIVE, active).withProperty(FACING, facing);
		world.setBlockState(pos, state, 3);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileAlarm();
	}

	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
	                                        float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.bbs[getFacing(state).getIndex()];
	}

}
