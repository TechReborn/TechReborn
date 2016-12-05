package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileWaterMill;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class BlockWaterMill extends BaseTileBlock {

	private final String prefix = "techreborn:blocks/machine/generators/";

	public BlockWaterMill() {
		super(Material.IRON);
		setUnlocalizedName("techreborn.watermill");
		setCreativeTab(TechRebornCreativeTab.instance);
		setHardness(2.0F);
		RebornCore.jsonDestroyer.registerObject(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileWaterMill();
	}

}