package techreborn.items;

import com.google.common.base.CaseFormat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;

import java.security.InvalidParameterException;

public class ItemParts extends ItemTRNoDestroy {
	public static final String[] types = new String[] { "energy_flow_circuit", "data_control_circuit", "data_storage_circuit",
		"data_orb", "diamond_grinding_head", "diamond_saw_blade", "tungsten_grinding_head", "helium_coolant_simple",
		"helium_coolant_triple", "helium_coolant_six", "nak_coolant_simple", "nak_coolant_triple", "nak_coolant_six",
		"cupronickel_heating_coil", "nichrome_heating_coil", "kanthal_heating_coil", ModItems.META_PLACEHOLDER, "super_conductor",
		"thorium_cell", "double_thorium_cell", "quad_thorium_cell", "plutonium_cell", "double_plutonium_cell",
		"quad_plutonium_cell", "computer_monitor", "machine_parts", "neutron_reflector", "iridium_neutron_reflector",
		"thick_neutron_reflector", "electronic_circuit", "advanced_circuit", "sap", "rubber", "scrap",
		"carbon_mesh", "carbon_fiber", "coolant_simple", "coolant_triple", "coolant_six" };

	public ItemParts() {
		setCreativeTab(TechRebornCreativeTab.instance);
		setHasSubtypes(true);
		setUnlocalizedName("techreborn.part");
	}

	public static ItemStack getPartByName(String name, int count) {
		//TODO: Change all recipes n' shit to use proper snake_case names so I don't have to do this bullshit
		if (name.equals("NaKCoolantSimple"))
			name = "nak_coolant_simple";
		if (name.equals("NaKCoolantTriple"))
			name = "nak_coolant_triple";
		if (name.equals("NaKCoolantSix"))
			name = "nak_coolant_six";
		if (name.equals("superconductor"))
			name = "super_conductor";
		if (name.equals("carbonfiber"))
			name = "carbon_fiber";
		if (name.equals("carbonmesh"))
			name = "carbon_mesh";
		if (name.equals("rubberSap"))
			name = "sap";
		name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
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
	public void getSubItems(Item item, CreativeTabs creativeTabs, NonNullList list) {
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
}
