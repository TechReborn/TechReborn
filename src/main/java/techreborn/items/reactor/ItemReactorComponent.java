/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.items.reactor;

import net.minecraft.item.ItemStack;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import net.minecraftforge.fml.common.Optional;

import techreborn.items.ItemTR;

/**
 * @author estebes
 */
@Optional.Interface(iface = "ic2.api.reactor.IReactor", modid = "ic2")
@Optional.Interface(iface = "ic2.api.reactor.IReactorComponent", modid = "ic2")
public abstract class ItemReactorComponent extends ItemTR implements IReactorComponent {
//	public static final String[] types = new String[] { "energy_flow_circuit", "data_control_circuit", "data_storage_circuit",
//		"data_orb", "diamond_grinding_head", "diamond_saw_blade", "tungsten_grinding_head", "helium_coolant_simple",
//		"helium_coolant_triple", "helium_coolant_six", "nak_coolant_simple", "nak_coolant_triple", "nak_coolant_six",
//		"cupronickel_heating_coil", "nichrome_heating_coil", "kanthal_heating_coil", ModItems.META_PLACEHOLDER, "super_conductor",
//		"thorium_cell", "double_thorium_cell", "quad_thorium_cell", "plutonium_cell", "double_plutonium_cell",
//		"quad_plutonium_cell", "computer_monitor", "machine_parts", "neutron_reflector", "iridium_neutron_reflector",
//		"thick_neutron_reflector", "electronic_circuit", "advanced_circuit", "sap", "rubber", "scrap",
//		"carbon_mesh", "carbon_fiber", "coolant_simple", "coolant_triple", "coolant_six", "enhanced_super_conductor", "basic_circuit_board",
//		"advanced_circuit_board", "advanced_circuit_parts", "processor_circuit_board" };

	// Constructor >>
	public ItemReactorComponent(String name) {
		setTranslationKey("techreborn." + name);
	}
	// << Constructor

	// IReactorComponent >>
	@Optional.Method(modid = "ic2")
	@Override
	public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatrun) {
		return;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY,
	                                  int pulseX, int pulseY, boolean heatrun) {
		return false;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return false;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return 0;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return 0;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
		return heat;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public float influenceExplosion(ItemStack stack, IReactor reactor) {
		return 0;
	}

	@Optional.Method(modid = "ic2")
	@Override
	public boolean canBePlacedIn(ItemStack itemStack, IReactor reactor) {
		return true;
	}
	// << IReactorComponent
}
