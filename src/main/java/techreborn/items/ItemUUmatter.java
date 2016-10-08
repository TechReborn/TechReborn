package techreborn.items;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemUUmatter extends ItemTextureBase implements ITexturedItem {

	public ItemUUmatter() {
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		setUnlocalizedName("techreborn.uuMatter");
	}

	@Override
	public String getTextureName(int damage) {
		return "techreborn:items/misc/itemMatter";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}
}
