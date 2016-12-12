package techreborn.items.tools;

import net.minecraft.item.ItemPickaxe;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemTRPickaxe extends ItemPickaxe {
	private ToolMaterial material = ToolMaterial.WOOD;

	public ItemTRPickaxe(ToolMaterial material) {
		super(material);
		setUnlocalizedName(material.name().toLowerCase() + "Pickaxe");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.material = material;
	}
}
