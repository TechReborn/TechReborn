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

import java.util.Arrays;

import com.google.common.base.CaseFormat;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornRegistry;
import techreborn.items.ItemParts;
import techreborn.lib.ModInfo;

/**
 * @author drcrazy
 *
 */
public enum ModParts implements IStringSerializable {
	CARBON_FIBER, CARBON_MESH, CIRCUIT_ADVANCED, CIRCUIT_BASIC, CIRCUIT_ELITE, COMPUTER_MONITOR, COOLANT_SIMPLE, COOLANT_SIX, COOLANT_TRIPLE,
	CUPRONICKEL_HEATING_COIL, DATA_ORB, DATA_STORAGE_CIRCUIT, DEPLETED_CELL,
	DIAMOND_GRINDING_HEAD, DIAMOND_SAW_BLADE, DOUBLE_DEPLETED_CELL, DOUBLE_PLUTONIUM_CELL, DOUBLE_THORIUM_CELL,
	DOUBLE_URANIUM_CELL, ENERGY_FLOW_CIRCUIT, HELIUM_COOLANT_SIMPLE, HELIUM_COOLANT_SIX,
	HELIUM_COOLANT_TRIPLE, IRIDIUM_NEUTRON_REFLECTOR, KANTHAL_HEATING_COIL, MACHINE_PARTS, NAK_COOLANT_SIMPLE,
	NAK_COOLANT_SIX, NAK_COOLANT_TRIPLE, NEUTRON_REFLECTOR, NICHROME_HEATING_COIL, PLUTONIUM_CELL, QUAD_DEPLETED_CELL,
	QUAD_PLUTONIUM_CELL, QUAD_THORIUM_CELL, QUAD_URANIUM_CELL, RUBBER, SAP, SCRAP, SUPER_CONDUCTOR,
	THICK_NEUTRON_REFLECTOR, THORIUM_CELL, TUNGSTEN_GRINDING_HEAD, URANIUM_CELL, UU_MATTER;

	public final String name;
	public final Item item;

	private ModParts() {
		name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.toString());
		item = new ItemParts();
		item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
		item.setTranslationKey(ModInfo.MOD_ID + "." + name);
	}

	public ItemStack getStack() {
		return new ItemStack(item);
	}

	public ItemStack getStack(int amount) {
		return new ItemStack(item, amount);
	}

	public static void register() {
		Arrays.stream(ModParts.values()).forEach(part -> RebornRegistry.registerItem(part.item));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation blockstateJson = new ResourceLocation(ModInfo.MOD_ID, "items/materials/parts");
		Arrays.stream(ModParts.values()).forEach(part -> ModelLoader.setCustomModelResourceLocation(part.item, 0,
				new ModelResourceLocation(blockstateJson, "type=" + part.name)));
	}

	@Override
	public String getName() {
		return name;
	}

}
