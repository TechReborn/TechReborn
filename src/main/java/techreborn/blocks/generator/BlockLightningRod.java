package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileLightningRod;

public class BlockLightningRod extends BlockMachineBase {

	public BlockLightningRod(Material material) {
		super();
		setUnlocalizedName("techreborn.lightningrod");
		setCreativeTab(TechRebornCreativeTab.instance);
	}

	@Override
	public TileEntity createNewTileEntity(World worldObj, int meta) {
		return new TileLightningRod();
	}

}
