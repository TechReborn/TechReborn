package techreborn.items.tools;

import net.minecraft.item.ItemHoe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;

public class ItemTRHoe extends ItemHoe {
	private ToolMaterial material = ToolMaterial.WOOD;

	public ItemTRHoe(ToolMaterial material) {
		super(material);
		setUnlocalizedName(material.name().toLowerCase() + "Hoe");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.material = material;
	}

	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
}
