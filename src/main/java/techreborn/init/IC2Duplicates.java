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

package techreborn.init;

import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;
import techreborn.api.TechRebornAPI;
import techreborn.blocks.BlockMachineFrames;
import techreborn.blocks.cable.EnumCableType;
import techreborn.config.ConfigTechReborn;
import techreborn.items.ingredients.ItemIngots;
import techreborn.items.ingredients.ItemParts;
import techreborn.items.ingredients.ItemPlates;

/**
 * Created by Mark on 18/12/2016.
 */
public enum IC2Duplicates {

	GRINDER(new ItemStack(ModBlocks.GRINDER)),
	ELECTRICAL_FURNACE(new ItemStack(ModBlocks.ELECTRIC_FURNACE)),
	IRON_FURNACE(new ItemStack(ModBlocks.IRON_FURNACE)),
	GENERATOR(new ItemStack(ModBlocks.SOLID_FUEL_GENEREATOR)),
	WATER_MILL(new ItemStack(ModBlocks.WATER_MILL)),
	EXTRACTOR(new ItemStack(ModBlocks.EXTRACTOR)),
	RECYCLER(new ItemStack(ModBlocks.RECYCLER)),
	COMPRESSOR(new ItemStack(ModBlocks.COMPRESSOR)),
	BAT_BOX(new ItemStack(ModBlocks.LOW_VOLTAGE_SU)),
	MFE(new ItemStack(ModBlocks.MEDIUM_VOLTAGE_SU)),
	MFSU(new ItemStack(ModBlocks.HIGH_VOLTAGE_SU)),
	LVT(new ItemStack(ModBlocks.LV_TRANSFORMER)),
	MVT(new ItemStack(ModBlocks.MV_TRANSFORMER)),
	HVT(new ItemStack(ModBlocks.HV_TRANSFORMER)),
	CABLE_COPPER(EnumCableType.COPPER.getStack()),
	CABLE_GLASSFIBER(EnumCableType.GLASSFIBER.getStack()),
	CABLE_GOLD(EnumCableType.GOLD.getStack()),
	CABLE_HV(EnumCableType.HV.getStack()),
	CABLE_ICOPPER(EnumCableType.ICOPPER.getStack()),
	CABLE_IGOLD(EnumCableType.IGOLD.getStack()),
	CABLE_IHV(EnumCableType.IHV.getStack()),
	CABLE_IIHV(EnumCableType.TIN.getStack()),
	CABLE_TIN(EnumCableType.TIN.getStack()),
	MIXED_METAL(ItemIngots.getIngotByName("mixed_metal")),
	CARBON_FIBER(ItemParts.getPartByName("carbon_fiber")),
	CARBON_MESH(ItemParts.getPartByName("carbon_mesh")),
	NEUTRON_REFLECTOR(ItemParts.getPartByName("neutron_reflector")),
	THICK_NEUTRON_REFLECTOR(ItemParts.getPartByName("thick_neutron_reflector")),
	IRIDIUM_NEUTRON_REFLECTOR(new ItemStack(ModItems.IRIDIUM_NEUTRON_REFLECTOR)),
	SCRAP(ItemParts.getPartByName("scrap")),
	SCRAP_BOX(new ItemStack(ModItems.SCRAP_BOX)),
	FREQ_TRANSMITTER(new ItemStack(ModItems.FREQUENCY_TRANSMITTER)),
	RUBBER_WOOD(new ItemStack(ModBlocks.RUBBER_LOG)),
	RE_BATTERY(new ItemStack(ModItems.RE_BATTERY)),
	ENERGY_CRYSTAL(new ItemStack(ModItems.ENERGY_CRYSTAL)),
	LAPATRON_CRYSTAL(new ItemStack(ModItems.LAPOTRONIC_CRYSTAL)),
	CARBON_PLATE(ItemPlates.getPlateByName("carbon")),
	IRON_FENCE(new ItemStack(ModBlocks.REFINED_IRON_FENCE)),
	
	REFINED_IRON(ItemIngots.getIngotByName("refined_iron"), true),
	BASIC_MACHINE_FRAME(BlockMachineFrames.getFrameByName("basic"), true),
	ADVANCED_MACHINE_FRAME(BlockMachineFrames.getFrameByName("advanced"), true),

	ADVANCED_ALLOY(ItemPlates.getPlateByName("advanced_alloy"), true);
	

	ItemStack ic2Stack;
	ItemStack trStack;
	boolean classicOnly;

	IC2Duplicates(ItemStack trStack) {
		this(trStack, false);
	}

	IC2Duplicates(ItemStack ic2Stack, ItemStack trStack) {
		this(ic2Stack, trStack, false);
	}

	IC2Duplicates(ItemStack ic2Stack, ItemStack trStack, boolean classicOnly) {
		Validate.notNull(trStack);
		Validate.isTrue(!trStack.isEmpty());
		Validate.notNull(ic2Stack);
		Validate.isTrue(!ic2Stack.isEmpty());
		this.ic2Stack = ic2Stack;
		this.trStack = trStack;
		this.classicOnly = classicOnly;
	}

	IC2Duplicates(ItemStack trStack, boolean classicOnly) {
		Validate.notNull(trStack);
		Validate.isTrue(!trStack.isEmpty());
		this.trStack = trStack;
		this.classicOnly = classicOnly;
	}

	public static boolean deduplicate() {
		if (TechRebornAPI.ic2Helper == null) {
			return false;
		}
		return ConfigTechReborn.REMOVE_DUPLICATES;
	}

	public ItemStack getIc2Stack() {
		if (TechRebornAPI.ic2Helper == null) {
			throw new RuntimeException("IC2 API isnt populated");
		}
		if (ic2Stack.isEmpty()) {
			throw new RuntimeException("IC2 stack wasnt set ");
		}
		return ic2Stack;
	}

	public void setIc2Stack(ItemStack ic2Stack) {
		Validate.notNull(ic2Stack);
		Validate.isTrue(!ic2Stack.isEmpty());
		this.ic2Stack = ic2Stack;
	}

	public boolean hasIC2Stack() {
		if (ic2Stack == null) {
			return false;
		}
		return !ic2Stack.isEmpty();
	}

	public ItemStack getTrStack() {
		return trStack;
	}

	public ItemStack getStackBasedOnConfig() {
		//Used only for when de-duping classic only items
		if(!isClassicMode() && classicOnly){
			return getTrStack();
		}
		if (deduplicate()) {
			return getIc2Stack();
		}
		return getTrStack();
	}

	public static boolean isClassicMode(){
		return ConfigTechReborn.IC2_PROFILE.equals("Classic");
	}

	public static boolean isClassicalDedupe(){
		return deduplicate() && isClassicMode();
	}

}
