package techreborn.items.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import techreborn.config.ConfigTechReborn;

public class ItemIronDrill extends ItemDrill
{

	public ItemIronDrill()
	{
		super(ToolMaterial.IRON, "techreborn.ironDrill", ConfigTechReborn.IronDrillCharge,
				ConfigTechReborn.IronDrillTier, 2.0F);
		this.cost = 50;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state)
	{
		return Items.iron_pickaxe.canHarvestBlock(state) || Items.iron_shovel.canHarvestBlock(state);
	}

	@Override
	public String getTextureName(int damage)
	{
		return "techreborn:items/tool/ironDrill";
	}

	@Override
	public int getMaxMeta()
	{
		return 1;
	}
}
