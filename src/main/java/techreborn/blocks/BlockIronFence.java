package techreborn.blocks;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import techreborn.client.TechRebornCreativeTabMisc;

public class BlockIronFence extends BlockFence {

	public BlockIronFence() {
		super(Material.IRON, BlockPlanks.EnumType.OAK.getMapColor());
		setUnlocalizedName("techreborn.ironfence");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setHardness(2.0F);
		setHarvestLevel("pickaxe", 2);
	}

}
