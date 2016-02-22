package techreborn.blocks;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import techreborn.client.TechRebornCreativeTabMisc;

public class BlockIronFence extends BlockFence {

	public BlockIronFence() {
		super(Material.iron);
        setUnlocalizedName("techreborn.ironfence");
        setCreativeTab(TechRebornCreativeTabMisc.instance);
		setStepSound(soundTypeMetal);
        setHardness(2.0F);
        setHarvestLevel("pickaxe", 2);
	}

}
