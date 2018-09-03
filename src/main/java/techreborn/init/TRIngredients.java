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
import techreborn.events.TRRecipeHandler;
import techreborn.items.ItemTR;
import techreborn.lib.ModInfo;

/**
 * @author drcrazy
 *
 */
public class TRIngredients {

	public static void register() {
		 Arrays.stream(Dusts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		 Arrays.stream(DustsSmall.values()).forEach(value -> RebornRegistry.registerItem(value.item));
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation dustsRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/dusts");		
		Arrays.stream(Dusts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(dustsRL, "type=" + value.name)));
		
		ResourceLocation dustsSmallRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/dustssmall");
		Arrays.stream(DustsSmall.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(dustsSmallRL, "type=" + value.name)));
		
	}

	public static enum Dusts implements IStringSerializable {
		ALMANDINE, ALUMINUM, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME,
		CINNABAR, CLAY, COAL, COPPER, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GOLD, GRANITE, GROSSULAR, INVAR, IRON, LAZURITE, LEAD, MAGNESIUM, MANGANESE, MARBLE, NETHERRACK,
		NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, RED_GARNET, RUBY, SALTPETER,
		SAPPHIRE, SAW, SILVER, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TIN, TITANIUM, TUNGSTEN, UVAROVITE,
		YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		private Dusts() {
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "DUST_" + this.toString());
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + "." + name);
			TRRecipeHandler.hideEntry(item);
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public String getName() {
			return name;
		}
	}

	public enum DustsSmall implements IStringSerializable {
		ALMANDINE, ALUMINUM, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME,
		CINNABAR, CLAY, COAL, COPPER, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GLOWSTONE, GOLD, GRANITE, GROSSULAR, INVAR, IRON, LAZURITE, LEAD, MAGNESIUM, MANGANESE, MARBLE,
		NETHERRACK, NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, REDSTONE, RED_GARNET,
		RUBY, SALTPETER, SAPPHIRE, SAW, SILVER, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TIN, TITANIUM,
		TUNGSTEN, UVAROVITE, YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		private DustsSmall() {
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "DUST_SMALL_" + this.toString());
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + "." + name);
			TRRecipeHandler.hideEntry(item);
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public String getName() {
			return name;
		}
	}

}
