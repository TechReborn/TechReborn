package techreborn.items.tools;

import techreborn.config.ConfigTechReborn;

public class ItemAdvancedJackhammer extends ItemJackhammer {

	public ItemAdvancedJackhammer() {
		super(ToolMaterial.IRON, "techreborn.advancedJackhammer", ConfigTechReborn.AdvancedJackhammerCharge,
			ConfigTechReborn.AdvancedJackhammerTier);
		this.cost = 250;
		this.efficiencyOnProperMaterial = 60F;
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/tool/advancedJackhammer";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
