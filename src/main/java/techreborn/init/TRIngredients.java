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
		 Arrays.stream(Gems.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		 Arrays.stream(Ingots.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		 Arrays.stream(Nuggets.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		 Arrays.stream(Parts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		 Arrays.stream(Plates.values()).forEach(value -> RebornRegistry.registerItem(value.item));
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation dustsRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/dusts");		
		Arrays.stream(Dusts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(dustsRL, "type=" + value.name)));
		
		ResourceLocation dustsSmallRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/dustssmall");
		Arrays.stream(DustsSmall.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(dustsSmallRL, "type=" + value.name)));
		
		ResourceLocation gemsRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/gems");
		Arrays.stream(Gems.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(gemsRL, "type=" + value.name)));
		
		ResourceLocation ingotsRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/ingots");
		Arrays.stream(Ingots.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(ingotsRL, "type=" + value.name)));
		
		ResourceLocation nuggetsRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/nuggets");
		Arrays.stream(Nuggets.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(nuggetsRL, "type=" + value.name)));
		
		ResourceLocation partsRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/parts");
		Arrays.stream(Parts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(partsRL, "type=" + value.name)));
		
		ResourceLocation platesRL = new ResourceLocation(ModInfo.MOD_ID, "items/materials/plates");
		Arrays.stream(Plates.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
				new ModelResourceLocation(platesRL, "type=" + value.name)));
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

	public static enum DustsSmall implements IStringSerializable {
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
	
	public static enum Gems implements IStringSerializable {
		PERIDOT, RED_GARNET, RUBY, SAPPHIRE, YELLOW_GARNET;

		public final String name;
		public final Item item;
		
		private Gems() {
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "GEM_" + this.toString());
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
	
	public static enum Ingots implements IStringSerializable {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM_ALLOY, IRIDIUM,
		LEAD, MIXED_METAL, NICKEL, PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;

		private Ingots() {
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "INGOT_" + this.toString());
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
	
	public static enum Nuggets implements IStringSerializable {
		ALUMINUM, BRASS, BRONZE, CHROME, COPPER, DIAMOND, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM, LEAD, NICKEL,
		PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;
		
		private Nuggets() {
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "NUGGET_" + this.toString());
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
	
	public static enum Parts implements IStringSerializable {
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

		private Parts() {
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.toString());
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
	
	public static enum Plates implements IStringSerializable {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CARBON, COAL, COPPER, DIAMOND, ELECTRUM, EMERALD, GOLD, INVAR,
		IRIDIUM_ALLOY, IRIDIUM, IRON, LAPIS, LAZURITE, LEAD, MAGNALIUM, NICKEL, OBSIDIAN, PERIDOT, PLATINUM, RED_GARNET,
		REDSTONE, REFINED_IRON, RUBY, SAPPHIRE, SILICON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, WOOD,
		YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		private Plates() {
			name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "PLATE_" + this.toString());
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
