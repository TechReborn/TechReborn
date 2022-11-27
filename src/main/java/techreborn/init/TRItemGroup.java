/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import reborncore.common.fluid.FluidUtils;
import reborncore.common.powerSystem.RcEnergyItem;
import techreborn.items.DynamicCellItem;
import techreborn.items.tool.basic.RockCutterItem;
import techreborn.items.tool.industrial.NanosaberItem;

public class TRItemGroup {
	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("techreborn", "item_group"))
		.icon(() -> new ItemStack(TRContent.NUKE))
		.build();

	static {
		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(TRItemGroup::entries);
	}

	// TODO improve ordering and also add to vanilla groups as needed
	private static void entries(FabricItemGroupEntries entries) {
		addContent(TRContent.Dusts.values(), entries);
		addContent(TRContent.RawMetals.values(), entries);
		addContent(TRContent.SmallDusts.values(), entries);
		addContent(TRContent.Gems.values(), entries);
		addContent(TRContent.Ingots.values(), entries);
		addContent(TRContent.Nuggets.values(), entries);
		addContent(TRContent.Parts.values(), entries);
		addContent(TRContent.Plates.values(), entries);

		addContent(ModFluids.values(), entries);
		addCells(entries);

		entries.add(TRContent.FREQUENCY_TRANSMITTER);

		addPoweredItem(TRContent.BASIC_CHAINSAW, entries);
		addPoweredItem(TRContent.BASIC_JACKHAMMER, entries);
		addPoweredItem(TRContent.BASIC_DRILL, entries);

		addPoweredItem(TRContent.ADVANCED_CHAINSAW, entries);
		addPoweredItem(TRContent.ADVANCED_JACKHAMMER, entries);
		addPoweredItem(TRContent.ADVANCED_DRILL, entries);

		addPoweredItem(TRContent.INDUSTRIAL_CHAINSAW, entries);
		addPoweredItem(TRContent.INDUSTRIAL_JACKHAMMER, entries);
		addPoweredItem(TRContent.INDUSTRIAL_DRILL, entries);

		addRockCutter(entries);

		for (TRContent.StorageUnit storageUnit : TRContent.StorageUnit.values()) {
			entries.add(storageUnit.upgrader);
		}
		
		entries.add(TRContent.BRONZE_HELMET);
		entries.add(TRContent.BRONZE_CHESTPLATE);
		entries.add(TRContent.BRONZE_LEGGINGS);
		entries.add(TRContent.BRONZE_BOOTS);
		entries.add(TRContent.BRONZE_SWORD);
		entries.add(TRContent.BRONZE_PICKAXE);
		entries.add(TRContent.BRONZE_AXE);
		entries.add(TRContent.BRONZE_HOE);
		entries.add(TRContent.BRONZE_SPADE);

		entries.add(TRContent.RUBY_HELMET);
		entries.add(TRContent.RUBY_CHESTPLATE);
		entries.add(TRContent.RUBY_LEGGINGS);
		entries.add(TRContent.RUBY_BOOTS);
		entries.add(TRContent.RUBY_SWORD);
		entries.add(TRContent.RUBY_PICKAXE);
		entries.add(TRContent.RUBY_AXE);
		entries.add(TRContent.RUBY_HOE);
		entries.add(TRContent.RUBY_SPADE);

		entries.add(TRContent.SAPPHIRE_HELMET);
		entries.add(TRContent.SAPPHIRE_CHESTPLATE);
		entries.add(TRContent.SAPPHIRE_LEGGINGS);
		entries.add(TRContent.SAPPHIRE_BOOTS);
		entries.add(TRContent.SAPPHIRE_SWORD);
		entries.add(TRContent.SAPPHIRE_PICKAXE);
		entries.add(TRContent.SAPPHIRE_AXE);
		entries.add(TRContent.SAPPHIRE_HOE);
		entries.add(TRContent.SAPPHIRE_SPADE);

		entries.add(TRContent.PERIDOT_HELMET);
		entries.add(TRContent.PERIDOT_CHESTPLATE);
		entries.add(TRContent.PERIDOT_LEGGINGS);
		entries.add(TRContent.PERIDOT_BOOTS);
		entries.add(TRContent.PERIDOT_SWORD);
		entries.add(TRContent.PERIDOT_AXE);
		entries.add(TRContent.PERIDOT_HOE);
		entries.add(TRContent.PERIDOT_SPADE);

		entries.add(TRContent.SILVER_HELMET);
		entries.add(TRContent.SILVER_CHESTPLATE);
		entries.add(TRContent.SILVER_LEGGINGS);
		entries.add(TRContent.SILVER_BOOTS);

		entries.add(TRContent.STEEL_HELMET);
		entries.add(TRContent.STEEL_CHESTPLATE);
		entries.add(TRContent.STEEL_LEGGINGS);
		entries.add(TRContent.STEEL_BOOTS);

		addPoweredItem(TRContent.QUANTUM_HELMET, entries);
		addPoweredItem(TRContent.QUANTUM_CHESTPLATE, entries);
		addPoweredItem(TRContent.QUANTUM_LEGGINGS, entries);
		addPoweredItem(TRContent.QUANTUM_BOOTS, entries);

		addNanosabre(entries);

		entries.add(TRContent.SCRAP_BOX);
		addContent(TRContent.Upgrades.values(), entries);

		addPoweredItem(TRContent.LITHIUM_ION_BATPACK, entries);
		addPoweredItem(TRContent.LAPOTRONIC_ORBPACK, entries);
		addPoweredItem(TRContent.CLOAKING_DEVICE, entries);

		addPoweredItem(TRContent.RED_CELL_BATTERY, entries);
		addPoweredItem(TRContent.LITHIUM_ION_BATTERY, entries);
		addPoweredItem(TRContent.ENERGY_CRYSTAL, entries);
		addPoweredItem(TRContent.LAPOTRON_CRYSTAL, entries);
		addPoweredItem(TRContent.LAPOTRONIC_ORB, entries);

		entries.add(TRContent.WRENCH);
		addPoweredItem(TRContent.OMNI_TOOL, entries);
		entries.add(TRContent.PAINTING_TOOL);

		entries.add(TRContent.GPS);
		entries.add(TRContent.TREE_TAP);
		addPoweredItem(TRContent.ELECTRIC_TREE_TAP, entries);

		entries.add(TRContent.MANUAL);
		entries.add(TRContent.DEBUG_TOOL);
	}

	private static void addContent(ItemConvertible[] items, FabricItemGroupEntries entries) {
		for (ItemConvertible item : items) {
			entries.add(item);
		}
	}

	private static void addCells(FabricItemGroupEntries entries) {
		entries.add(DynamicCellItem.getEmptyCell(1));
		for (Fluid fluid : FluidUtils.getAllFluids()) {
			if (fluid.isStill(fluid.getDefaultState())) {
				entries.add(DynamicCellItem.getCellWithFluid(fluid));
			}
		}
	}

	private static void addPoweredItem(Item item, FabricItemGroupEntries entries) {
		ItemStack uncharged = new ItemStack(item);
		ItemStack charged = new ItemStack(item);
		RcEnergyItem energyItem = (RcEnergyItem) item;

		energyItem.setStoredEnergy(charged, energyItem.getEnergyCapacity());

		entries.add(uncharged);
		entries.add(charged);
	}

	private static void addRockCutter(FabricItemGroupEntries entries) {
		RockCutterItem rockCutter = (RockCutterItem) TRContent.ROCK_CUTTER;

		ItemStack uncharged = new ItemStack(rockCutter);
		uncharged.addEnchantment(Enchantments.SILK_TOUCH, 1);
		ItemStack charged = new ItemStack(rockCutter);
		charged.addEnchantment(Enchantments.SILK_TOUCH, 1);
		rockCutter.setStoredEnergy(charged, rockCutter.getEnergyCapacity());

		entries.add(uncharged);
		entries.add(charged);
	}

	private static void addNanosabre(FabricItemGroupEntries entries) {
		NanosaberItem nanosaber = (NanosaberItem) TRContent.NANOSABER;

		ItemStack inactiveUncharged = new ItemStack(nanosaber);
		inactiveUncharged.setNbt(new NbtCompound());
		inactiveUncharged.getOrCreateNbt().putBoolean("isActive", false);

		ItemStack inactiveCharged = new ItemStack(TRContent.NANOSABER);
		inactiveCharged.setNbt(new NbtCompound());
		inactiveCharged.getOrCreateNbt().putBoolean("isActive", false);
		nanosaber.setStoredEnergy(inactiveCharged, nanosaber.getEnergyCapacity());

		ItemStack activeCharged = new ItemStack(TRContent.NANOSABER);
		activeCharged.setNbt(new NbtCompound());
		activeCharged.getOrCreateNbt().putBoolean("isActive", true);
		nanosaber.setStoredEnergy(activeCharged, nanosaber.getEnergyCapacity());

		entries.add(inactiveUncharged);
		entries.add(inactiveCharged);
		entries.add(activeCharged);
	}
}
