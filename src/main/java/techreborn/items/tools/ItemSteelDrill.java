package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import techreborn.config.ConfigTechReborn;

public class ItemSteelDrill extends ItemDrill {

	public ItemSteelDrill() {
		super(ToolMaterial.IRON, "techreborn.ironDrill", ConfigTechReborn.IronDrillCharge,
			ConfigTechReborn.IronDrillTier, 0.5F, 10F);
		this.cost = 50;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return Items.IRON_PICKAXE.canHarvestBlock(state) || Items.IRON_SHOVEL.canHarvestBlock(state);
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/steelDrill";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
