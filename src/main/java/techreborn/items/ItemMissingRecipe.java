package techreborn.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemMissingRecipe extends ItemTextureBase implements ITexturedItem {
	public ItemMissingRecipe() {
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.missingrecipe");
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/misc/missing_recipe";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}