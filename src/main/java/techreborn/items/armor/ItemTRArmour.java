package techreborn.items.armor;

import me.modmuss50.jsonDestroyer.api.ITexturedItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornCore;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 26/02/2016.
 */
public class ItemTRArmour extends ItemArmor implements ITexturedItem {

	private ArmorMaterial material = ArmorMaterial.LEATHER;
	private EntityEquipmentSlot slot = EntityEquipmentSlot.HEAD;

	public ItemTRArmour(ArmorMaterial material, EntityEquipmentSlot slot) {
		super(material, material.getDamageReductionAmount(slot), slot);
		if (slot == EntityEquipmentSlot.HEAD)
			setUnlocalizedName(material.name().toLowerCase() + "Helmet");
		if (slot == EntityEquipmentSlot.CHEST)
			setUnlocalizedName(material.name().toLowerCase() + "Chestplate");
		if (slot == EntityEquipmentSlot.LEGS)
			setUnlocalizedName(material.name().toLowerCase() + "Leggings");
		if (slot == EntityEquipmentSlot.FEET)
			setUnlocalizedName(material.name().toLowerCase() + "Boots");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		RebornCore.jsonDestroyer.registerObject(this);
		this.material = material;
		this.slot = slot;
	}

	@Override
	public String getTextureName(int damage) {
		if (slot == EntityEquipmentSlot.HEAD)
			return "techreborn:items/tool/" + material.name().toLowerCase() + "_helmet";
		if (slot == EntityEquipmentSlot.CHEST)
			return "techreborn:items/tool/" + material.name().toLowerCase() + "_chestplate";
		if (slot == EntityEquipmentSlot.LEGS)
			return "techreborn:items/tool/" + material.name().toLowerCase() + "_leggings";
		if (slot == EntityEquipmentSlot.FEET)
			return "techreborn:items/tool/" + material.name().toLowerCase() + "_boots";
		return "techreborn:items/tool/" + material.name().toLowerCase() + "_error";
	}

	@Override
	public int getMaxMeta() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		return new ModelResourceLocation(ModInfo.MOD_ID + ":" + getUnlocalizedName(stack).substring(5), "inventory");
	}

	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
}
