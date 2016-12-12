package techreborn.items.tools;

import net.minecraft.item.ItemSpade;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemTRSpade extends ItemSpade {
	private ToolMaterial material = ToolMaterial.WOOD;

	public ItemTRSpade(ToolMaterial material) {
		super(material);
		setUnlocalizedName(material.name().toLowerCase() + "Spade");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.material = material;
	}
}
