package techreborn.items.tools;

import techreborn.config.ConfigTechReborn;

import net.minecraft.item.Item.ToolMaterial;
public class ItemDiamondJackhammer extends ItemJackhammer
{

	public ItemDiamondJackhammer()
	{
		super(ToolMaterial.DIAMOND, "techreborn.diamondJackhammer", ConfigTechReborn.DiamondJackhammerCharge,
				ConfigTechReborn.DiamondJackhammerTier);
		this.cost = 250;
		this.efficiencyOnProperMaterial = 60F;
	}

	@Override
	public String getTextureName(int damage)
	{
		return "techreborn:items/tool/diamondJackhammer";
	}

	@Override
	public int getMaxMeta()
	{
		return 1;
	}
}
