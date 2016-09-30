package techreborn.items.tools;

import techreborn.config.ConfigTechReborn;

public class ItemSteelJackhammer extends ItemJackhammer {

	public ItemSteelJackhammer() {
		super(ToolMaterial.DIAMOND, "techreborn.steelJackhammer", ConfigTechReborn.SteelJackhammerCharge,
			ConfigTechReborn.SteelJackhammerTier);
		this.cost = 50;
		this.efficiencyOnProperMaterial = 12F;
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/steelJackhammer";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
