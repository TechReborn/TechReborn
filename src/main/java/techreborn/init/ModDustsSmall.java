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
import techreborn.items.ItemDusts;
import techreborn.lib.ModInfo;

/**
 * @author drcrazy
 *
 */
public enum ModDustsSmall implements IStringSerializable {
	ALMANDINE, ALUMINUM, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME,
	CINNABAR, CLAY, COAL, COPPER, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
	FLINT, GALENA, GLOWSTONE, GOLD, GRANITE, GROSSULAR, INVAR, IRON, LAZURITE, LEAD, MAGNESIUM, MANGANESE, MARBLE,
	NETHERRACK, NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, REDSTONE, RED_GARNET, RUBY,
	SALTPETER, SAPPHIRE, SAW, SILVER, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TIN, TITANIUM, TUNGSTEN,
	UVAROVITE, YELLOW_GARNET, ZINC;

	public final String name;
	public final Item item;

	private ModDustsSmall() {
		name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "DUST_SMALL_" + this.toString());
		item = new ItemDusts();
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
		Arrays.stream(ModDustsSmall.values()).forEach(dustSmall -> RebornRegistry.registerItem(dustSmall.item));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation blockstateJson = new ResourceLocation(ModInfo.MOD_ID, "items/materials/dustssmall");
		Arrays.stream(ModDustsSmall.values())
				.forEach(dustSmall -> ModelLoader.setCustomModelResourceLocation(dustSmall.item, 0,
						new ModelResourceLocation(blockstateJson, "type=" + dustSmall.name)));
	}

	@Override
	public String getName() {
		return name;
	}

}
