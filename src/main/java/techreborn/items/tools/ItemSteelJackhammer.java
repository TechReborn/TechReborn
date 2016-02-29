package techreborn.items.tools;

import techreborn.config.ConfigTechReborn;

public class ItemSteelJackhammer extends ItemJackhammer {

	public ItemSteelJackhammer() {
		super(ToolMaterial.EMERALD, "techreborn.steelJackhammer", ConfigTechReborn.SteelJackhammerCharge, ConfigTechReborn.SteelJackhammerTier);
		this.cost = 100;
		this.efficiencyOnProperMaterial = 16F;
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
