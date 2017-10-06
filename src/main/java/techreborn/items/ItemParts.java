/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.items;

import com.google.common.base.CaseFormat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.EGui;
import techreborn.client.TechRebornCreativeTab;
import techreborn.init.ModItems;

import java.security.InvalidParameterException;

public class ItemParts extends ItemTR {
	public static final String[] types = new String[] { "energy_flow_circuit", "data_control_circuit", "data_storage_circuit",
		"data_orb", "diamond_grinding_head", "diamond_saw_blade", "tungsten_grinding_head", "helium_coolant_simple",
		"helium_coolant_triple", "helium_coolant_six", "nak_coolant_simple", "nak_coolant_triple", "nak_coolant_six",
		"cupronickel_heating_coil", "nichrome_heating_coil", "kanthal_heating_coil", ModItems.META_PLACEHOLDER, "super_conductor",
		"thorium_cell", "double_thorium_cell", "quad_thorium_cell", "plutonium_cell", "double_plutonium_cell",
		"quad_plutonium_cell", "computer_monitor", "machine_parts", "neutron_reflector", "iridium_neutron_reflector",
		"thick_neutron_reflector", "electronic_circuit", "advanced_circuit", "sap", "rubber", "scrap",
		"carbon_mesh", "carbon_fiber", "coolant_simple", "coolant_triple", "coolant_six", "solar_cell","compact_solar_cell","quantum_singularity" };

	public ItemParts() {
		this.setCreativeTab(TechRebornCreativeTab.instance);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("techreborn.part");
	}

	public static ItemStack getPartByName(String name, final int count) {
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
		for (int i = 0; i < ItemParts.types.length; i++) {
			if (ItemParts.types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.PARTS, count, i);
			}
		}
		throw new InvalidParameterException("The part " + name + " could not be found.");
	}

	public static ItemStack getPartByName(final String name) {
		return ItemParts.getPartByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(final ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= ItemParts.types.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + ItemParts.types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	@Override
	public void getSubItems(final CreativeTabs creativeTabs, final NonNullList<ItemStack> list) {
		if (!isInCreativeTab(creativeTabs)) {
			return;
		}
		for (int meta = 0; meta < ItemParts.types.length; ++meta) {
			if (!ItemParts.types[meta].equals(ModItems.META_PLACEHOLDER)) {
				list.add(new ItemStack(this, 1, meta));
			}
		}
	}

	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
		switch (itemStack.getItemDamage()) {
			case 37: // Destructo pack
				player.openGui(Core.INSTANCE, EGui.DESTRUCTOPACK.ordinal(), world, (int) player.posX, (int) player.posY,
					(int) player.posY);
				break;
		}
		return itemStack;
	}
}
