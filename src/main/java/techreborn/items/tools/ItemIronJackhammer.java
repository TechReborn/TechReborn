package techreborn.items.tools;

import techreborn.config.ConfigTechReborn;

import net.minecraft.item.Item.ToolMaterial;
public class ItemIronJackhammer extends ItemJackhammer
{

	public ItemIronJackhammer()
	{
		super(ToolMaterial.IRON, "techreborn.ironJackhammer", ConfigTechReborn.IronJackhammerCharge,
				ConfigTechReborn.IronJackhammerTier);
		this.cost = 50;
		this.efficiencyOnProperMaterial = 12F;
	}

	@Override
	public String getTextureName(int damage)
	{
		return "techreborn:items/tool/ironJackhammer";
	}

	@Override
	public int getMaxMeta()
	{
		return 1;
	}
}
