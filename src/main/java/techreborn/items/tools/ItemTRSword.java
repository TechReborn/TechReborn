package techreborn.items.tools;

import net.minecraft.item.ItemSword;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemTRSword extends ItemSword {
	private ToolMaterial material = ToolMaterial.WOOD;

	public ItemTRSword(ToolMaterial material) {
		super(material);
		setUnlocalizedName(material.name().toLowerCase() + "Sword");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.material = material;
	}
}
