package techreborn.blocks.generator;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileSolarPanel;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class BlockSolarPanel extends BaseTileBlock  {

	public static PropertyBool ACTIVE = PropertyBool.create("active");
	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockSolarPanel() {
		super(Material.IRON);
		setUnlocalizedName("techreborn.solarpanel");
		setCreativeTab(TechRebornCreativeTab.instance);
		this.setDefaultState(this.getDefaultState().withProperty(ACTIVE, false));
		setHardness(2.0F);
	}

	protected BlockStateContainer createBlockState() {
		ACTIVE = PropertyBool.create("active");
		return new BlockStateContainer(this, ACTIVE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ACTIVE, meta != 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ACTIVE) ? 1 : 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSolarPanel();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos p_189540_5_) {
		if (worldIn.canBlockSeeSky(pos.up()) && !worldIn.isRaining() && !worldIn.isThundering()
			&& worldIn.isDaytime()) {
			worldIn.setBlockState(pos,
				worldIn.getBlockState(pos).withProperty(BlockSolarPanel.ACTIVE, true));
		} else {

			worldIn.setBlockState(pos,
				worldIn.getBlockState(pos).withProperty(BlockSolarPanel.ACTIVE, false));
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
	                                        float hitZ, int meta, EntityLivingBase placer) {
		if (!worldIn.isRemote) {
			if (worldIn.canBlockSeeSky(pos.up()) && !worldIn.isRaining() && !worldIn.isThundering() && worldIn.isDaytime()) {
				return this.getDefaultState().withProperty(ACTIVE, true);

			} else {
				return this.getDefaultState().withProperty(ACTIVE, false);
			}
		} else {
			return this.getDefaultState().withProperty(ACTIVE, false);
		}
	}

}
