package techreborn.blocks.generator;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileWindMill;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class BlockWindMill extends BlockMachineBase {

	public BlockWindMill() {
		super(false);
		setUnlocalizedName("techreborn.windmill");
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileWindMill();
	}

}
