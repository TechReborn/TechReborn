package techreborn.items.tools;

import techreborn.config.ConfigTechReborn;

public class ItemDiamondJackhammer extends ItemJackhammer {

	public ItemDiamondJackhammer() {
		super(ToolMaterial.DIAMOND, "techreborn.diamondJackhammer", ConfigTechReborn.DiamondJackhammerCharge,
			ConfigTechReborn.DiamondJackhammerTier);
		this.cost = 100;
		this.efficiencyOnProperMaterial = 16F;
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/diamondJackhammer";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
