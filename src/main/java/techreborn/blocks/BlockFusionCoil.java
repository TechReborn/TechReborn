package techreborn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import techreborn.client.TechRebornCreativeTab;

public class BlockFusionCoil extends Block {

	public BlockFusionCoil(Material material) {
		super(Material.IRON);
		setHardness(2f);
		setSoundType(SoundType.METAL);
		setUnlocalizedName("techreborn.fusioncoil");
		setCreativeTab(TechRebornCreativeTab.instance);
	}
}
