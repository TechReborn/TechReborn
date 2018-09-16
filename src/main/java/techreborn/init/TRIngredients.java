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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornRegistry;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockStorage;
import techreborn.events.TRRecipeHandler;
import techreborn.items.ItemTR;
import techreborn.lib.ModInfo;

import java.util.Arrays;

/**
 * @author drcrazy
 */
public class TRIngredients {

	public static void registerBlocks() {
		Arrays.stream(Ores.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(StorageBlocks.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
	}

	public static void registerItems() {
		Arrays.stream(Dusts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(SmallDusts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Gems.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Ingots.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Nuggets.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Parts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Plates.values()).forEach(value -> RebornRegistry.registerItem(value.item));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation dustsRL = new ResourceLocation(ModInfo.MOD_ID, "items/material/dust");
		Arrays.stream(Dusts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(dustsRL, "type=" + value.unsuffixedName)));

		ResourceLocation dustsSmallRL = new ResourceLocation(ModInfo.MOD_ID, "items/material/small_dust");
		Arrays.stream(SmallDusts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(dustsSmallRL, "type=" + value.unsuffixedName)));

		ResourceLocation gemsRL = new ResourceLocation(ModInfo.MOD_ID, "items/material/gem");
		Arrays.stream(Gems.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(gemsRL, "type=" + value.unsuffixedName)));

		ResourceLocation ingotsRL = new ResourceLocation(ModInfo.MOD_ID, "items/material/ingot");
		Arrays.stream(Ingots.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(ingotsRL, "type=" + value.unsuffixedName)));

		ResourceLocation nuggetsRL = new ResourceLocation(ModInfo.MOD_ID, "items/material/nugget");
		Arrays.stream(Nuggets.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(nuggetsRL, "type=" + value.unsuffixedName)));

		ResourceLocation partsRL = new ResourceLocation(ModInfo.MOD_ID, "items/material/part");
		Arrays.stream(Parts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(partsRL, "type=" + value.name)));

		ResourceLocation platesRL = new ResourceLocation(ModInfo.MOD_ID, "items/material/plate");
		Arrays.stream(Plates.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(platesRL, "type=" + value.unsuffixedName)));

		ResourceLocation oresRL = new ResourceLocation(ModInfo.MOD_ID, "ore");
		for (Ores value : Ores.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(value.block), 0, new ModelResourceLocation(oresRL, "type=" + value.unsuffixedName));
			ModelLoader.setCustomStateMapper(value.block, new DefaultStateMapper() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation(oresRL, "type=" + value.unsuffixedName);
				}
			});
		}

		ResourceLocation storageRL = new ResourceLocation(ModInfo.MOD_ID, "storage_block");
		for (StorageBlocks value : StorageBlocks.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(value.block), 0, new ModelResourceLocation(storageRL, "type=" + value.unsuffixedName));
			ModelLoader.setCustomStateMapper(value.block, new DefaultStateMapper() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation(storageRL, "type=" + value.unsuffixedName);
				}
			});
		}
	}

	public static enum Ores implements IStringSerializable {
		BAUXITE, CINNABAR, COPPER, GALENA, IRIDIUM, LEAD, PERIDOT, PYRITE, RUBY, SAPPHIRE, SHELDONITE, SILVER, SODALITE,
		SPHALERITE, TIN, TUNGSTEN;

		public final String unsuffixedName;
		public final String name;
		public final Block block;

		private Ores() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_ore";
			block = new BlockOre();
			block.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			block.setTranslationKey(ModInfo.MOD_ID + ".ore." + this.toString().toLowerCase());
			TRRecipeHandler.hideEntry(block);
		}

		@Override
		public String getName() {
			return name;
		}

	}

	public static enum StorageBlocks implements IStringSerializable {
		ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, INVAR, IRIDIUM, IRIDIUM_REINFORCED_STONE,
		IRIDIUM_REINFORCED_TUNGSTENSTEEL, LEAD, NICKEL, OSMIUM, PERIDOT, PLATINUM, RED_GARNET, REFINED_IRON, RUBY,
		SAPPHIRE, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, YELLOW_GARNET, ZINC;

		public final String unsuffixedName;
		public final String name;
		public final Block block;

		private StorageBlocks() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_block";
			block = new BlockStorage();
			block.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			block.setTranslationKey(ModInfo.MOD_ID + ".storage_block." + this.toString().toLowerCase());
			TRRecipeHandler.hideEntry(block);
		}

		@Override
		public String getName() {
			return name;
		}
	}

	public static enum Dusts implements IStringSerializable {
		ALMANDINE, ALUMINUM, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME,
		CINNABAR, CLAY, COAL, COPPER, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GOLD, GRANITE, GROSSULAR, INVAR, IRON, LAZURITE, LEAD, MAGNESIUM, MANGANESE, MARBLE, NETHERRACK,
		NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, RED_GARNET, RUBY, SALTPETER,
		SAPPHIRE, SAW, SILVER, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TIN, TITANIUM, TUNGSTEN, UVAROVITE,
		YELLOW_GARNET, ZINC;

		public final String unsuffixedName;
		public final String name;
		public final Item item;

		private Dusts() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_dust";
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + ".dust." + this.toString().toLowerCase());
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

	public static enum SmallDusts implements IStringSerializable {
		ALMANDINE, ALUMINUM, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME,
		CINNABAR, CLAY, COAL, COPPER, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GLOWSTONE, GOLD, GRANITE, GROSSULAR, INVAR, IRON, LAZURITE, LEAD, MAGNESIUM, MANGANESE, MARBLE,
		NETHERRACK, NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, REDSTONE, RED_GARNET,
		RUBY, SALTPETER, SAPPHIRE, SAW, SILVER, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TIN, TITANIUM,
		TUNGSTEN, UVAROVITE, YELLOW_GARNET, ZINC;

		public final String unsuffixedName;
		public final String name;
		public final Item item;

		private SmallDusts() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_small_dust";
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + ".small_dust." + this.toString().toLowerCase());
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

		public final String unsuffixedName;
		public final String name;
		public final Item item;

		private Gems() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_gem";
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + ".gem." + this.toString().toLowerCase());
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

		public final String unsuffixedName;
		public final String name;
		public final Item item;

		private Ingots() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_ingot";
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + ".ingot." + this.toString().toLowerCase());
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

		public final String unsuffixedName;
		public final String name;
		public final Item item;

		private Nuggets() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_nugget";
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + ".nugget." + this.toString().toLowerCase());
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
		CARBON_FIBER,
		CARBON_MESH,

		ELECTRONIC_CIRCUIT,
		ADVANCED_CIRCUIT,
		INDUSTRIAL_CIRCUIT,

		MACHINE_PARTS,
		DIGITAL_DISPLAY,

		DATA_STORAGE_CORE,
		DATA_STORAGE_CHIP,
		ENERGY_FLOW_CHIP,
		SUPERCONDUCTOR,

		DIAMOND_SAW_BLADE,
		DIAMOND_GRINDING_HEAD,
		TUNGSTEN_GRINDING_HEAD,

		CUPRONICKEL_HEATING_COIL,
		KANTHAL_HEATING_COIL,
		NICHROME_HEATING_COIL,

		NEUTRON_REFLECTOR,
		THICK_NEUTRON_REFLECTOR,
		IRIDIUM_NEUTRON_REFLECTOR

		//java vars can't start with numbers, so these get suffixes
		, WATER_COOLANT_CELL_10K,
		WATER_COOLANT_CELL_30K,
		WATER_COOLANT_CELL_60K,
		HELIUM_COOLANT_CELL_60K,
		HELIUM_COOLANT_CELL_360K,
		HELIUM_COOLANT_CELL_180K,
		NAK_COOLANT_CELL_60K,
		NAK_COOLANT_CELL_180K,
		NAK_COOLANT_CELL_360K,

		RUBBER,
		SAP,
		SCRAP,
		UU_MATTER;

		public final String name;
		public final Item item;

		private Parts() {
			name = this.toString().toLowerCase();
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + ".part." + this.toString().toLowerCase());
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

		public final String unsuffixedName;
		public final String name;
		public final Item item;

		private Plates() {
			unsuffixedName = this.toString().toLowerCase();
			name = this.toString().toLowerCase() + "_plate";
			item = new ItemTR();
			item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
			item.setTranslationKey(ModInfo.MOD_ID + ".plate." + this.toString().toLowerCase());
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
