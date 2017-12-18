package techreborn.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileAlarm;

import javax.annotation.Nullable;

public class BlockAlarm extends BlockContainer {
	public BlockAlarm() {
		super(Material.ROCK);
		setUnlocalizedName("techreborn.alarm");
		setCreativeTab(TechRebornCreativeTab.instance);

	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileAlarm();
	}
}
