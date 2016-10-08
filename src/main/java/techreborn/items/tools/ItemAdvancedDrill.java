package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import techreborn.config.ConfigTechReborn;

public class ItemAdvancedDrill extends ItemDrill {

	public ItemAdvancedDrill() {
		super(ToolMaterial.DIAMOND, "techreborn.advancedDrill", ConfigTechReborn.AdvancedDrillCharge,
			ConfigTechReborn.AdvancedDrillTier, 4.0F, 20F);
		this.cost = 250;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return Items.DIAMOND_PICKAXE.canHarvestBlock(blockIn) || Items.DIAMOND_SHOVEL.canHarvestBlock(blockIn);
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/advancedDrill";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
