package techreborn.blocks.generator;

import me.modmuss50.jsonDestroyer.api.ITexturedBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileWaterMill;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class BlockWaterMill extends BaseTileBlock {

	private final String prefix = "techreborn:blocks/machines/generators/";

	public BlockWaterMill() {
		super(Material.IRON);
		setUnlocalizedName("techreborn.watermill");
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileWaterMill();
	}

}