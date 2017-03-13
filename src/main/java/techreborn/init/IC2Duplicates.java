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

package techreborn.init;

import net.minecraft.item.ItemStack;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.items.ItemUpgrades;
import techreborn.parts.powerCables.EnumStandaloneCableType;

/**
 * Created by Mark on 18/12/2016.
 */
public enum IC2Duplicates {

	GRINDER(new ItemStack(ModBlocks.GRINDER)),
	ELECTRICAL_FURNACE(new ItemStack(ModBlocks.ELECTRIC_FURNACE)),
	IRON_FURNACE(new ItemStack(ModBlocks.IRON_FURNACE)),
	GENERATOR(new ItemStack(ModBlocks.SOLID_FUEL_GENEREATOR)),
	EXTRACTOR(new ItemStack(ModBlocks.EXTRACTOR)),
	SOLAR_PANEL(new ItemStack(ModBlocks.SOLAR_PANEL)),
	RECYCLER(new ItemStack(ModBlocks.RECYCLER)),
	COMPRESSOR(new ItemStack(ModBlocks.COMPRESSOR)),
	BAT_BOX(new ItemStack(ModBlocks.BATTERY_BOX)),
	MFE(new ItemStack(ModBlocks.MVSU)),
	MFSU(new ItemStack(ModBlocks.HVSU)),
	LVT(new ItemStack(ModBlocks.LV_TRANSFORMER)),
	MVT(new ItemStack(ModBlocks.MV_TRANSFORMER)),
	HVT(new ItemStack(ModBlocks.HV_TRANSFORMER)),
	CABLE_COPPER(EnumStandaloneCableType.COPPER.getStack()),
	CABLE_GLASSFIBER(EnumStandaloneCableType.GLASSFIBER.getStack()),
	CABLE_GOLD(EnumStandaloneCableType.GOLD.getStack()),
	CABLE_HV(EnumStandaloneCableType.HV.getStack()),
	CABLE_ICOPPER(EnumStandaloneCableType.ICOPPER.getStack()),
	CABLE_IGOLD(EnumStandaloneCableType.IGOLD.getStack()),
	CABLE_IHV(EnumStandaloneCableType.IHV.getStack()),
	CABLE_IIHV(EnumStandaloneCableType.TIN.getStack()),
	UPGRADE_OVERCLOCKER(ItemUpgrades.getUpgradeByName("overclock")),
	UPGRADE_TRANSFORMER(ItemUpgrades.getUpgradeByName("transformer")),
	UPGRADE_STORAGE(ItemUpgrades.getUpgradeByName("energy_storage")),
	MIXED_METAL(ItemIngots.getIngotByName("mixed_metal")),
	CARBON_FIBER(ItemParts.getPartByName("carbon_fiber")),
	CARBON_MESH(ItemParts.getPartByName("carbon_mesh")),
	REFINED_IRON(ItemIngots.getIngotByName("refined_iron")),
	SCRAP(ItemParts.getPartByName("scrap")),
	FREQ_TRANSMITTER(new ItemStack(ModItems.FREQUENCY_TRANSMITTER));

	ItemStack ic2Stack;
	ItemStack trStack;

	IC2Duplicates(ItemStack trStack) {
		this.trStack = trStack;
	}

	IC2Duplicates(ItemStack ic2Stack, ItemStack trStack) {
		this.ic2Stack = ic2Stack;
		this.trStack = trStack;
	}

	public static boolean deduplicate() {
		if (!CompatManager.isIC2Loaded || CompatManager.isIC2ClassicLoaded) {
			return false;
		}
		return ConfigTechReborn.REMOVE_DUPLICATES;
	}

	public ItemStack getIc2Stack() {
		if (!CompatManager.isIC2Loaded) {
			throw new RuntimeException("IC2 isnt loaded");
		}
		if (ic2Stack == null) {
			throw new RuntimeException("IC2 stack wasnt set ");
		}
		return ic2Stack;
	}

	public void setIc2Stack(ItemStack ic2Stack) {
		this.ic2Stack = ic2Stack;
	}

	public boolean hasIC2Stack() {
		return ic2Stack != null;
	}

	public ItemStack getTrStack() {
		return trStack;
	}

	public ItemStack getStackBasedOnConfig() {
		if (deduplicate()) {
			return getIc2Stack();
		}
		return getTrStack();
	}

}
