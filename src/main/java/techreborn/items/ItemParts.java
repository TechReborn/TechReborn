package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;

import java.security.InvalidParameterException;
import java.util.List;

public class ItemParts extends ItemTextureBase {
	public static final String[] types = new String[] { "energyFlowCircuit", "dataControlCircuit", "dataStorageCircuit",
		"dataOrb", "diamondGrindingHead", "diamondSawBlade", "tungstenGrindingHead", "heliumCoolantSimple",
		"heliumCoolantTriple", "heliumCoolantSix", "NaKCoolantSimple", "NaKCoolantTriple", "NaKCoolantSix",
		"cupronickelHeatingCoil", "nichromeHeatingCoil", "kanthalHeatingCoil", ModItems.META_PLACEHOLDER, "superConductor",
		"thoriumCell", "doubleThoriumCell", "quadThoriumCell", "plutoniumCell", "doublePlutoniumCell",
		"quadPlutoniumCell", "computerMonitor", "machineParts", "neutronReflector", "iridiumNeutronReflector",
		"thickNeutronReflector", "electronicCircuit", "advancedCircuit", "rubberSap", "rubber", "scrap",
		"carbonmesh", "carbonfiber", "coolantSimple", "coolantTriple", "coolantSix" };

	public ItemParts() {
		setCreativeTab(TechRebornCreativeTab.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.part");
	}

	public static ItemStack getPartByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.parts, count, i);
			}
		}
		throw new InvalidParameterException("The part " + name + " could not be found.");
	}

	public static ItemStack getPartByName(String name) {
		return getPartByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		for (int meta = 0; meta < types.length; ++meta) {
			if (!types[meta].equals(ModItems.META_PLACEHOLDER)) {
				list.add(new ItemStack(item, 1, meta));
			}
		}
	}

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		switch (itemStack.getItemDamage()) {
			case 37: // Destructo pack
				player.openGui(Core.INSTANCE, GuiHandler.destructoPackID, world, (int) player.posX, (int) player.posY,
					(int) player.posY);
				break;
		}
		return itemStack;
	}

	@Override
	public String getTextureName(int damage) {
		return ModInfo.MOD_ID + ":items/part/" + types[damage];
	}

	@Override
	public int getMaxMeta() {
		return types.length;
	}
}
