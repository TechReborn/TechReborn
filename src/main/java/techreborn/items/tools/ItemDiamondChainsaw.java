package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import techreborn.config.ConfigTechReborn;

public class ItemDiamondChainsaw extends ItemChainsaw {

	public ItemDiamondChainsaw() {
		super(ToolMaterial.DIAMOND, "techreborn.diamondChainsaw", ConfigTechReborn.DiamondChainsawCharge,
			ConfigTechReborn.DiamondChainsawTier, 2.5F);
		this.cost = 250;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return Items.DIAMOND_AXE.canHarvestBlock(blockIn);
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/diamondChainsaw";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
