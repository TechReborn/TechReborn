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

package techreborn.events;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;



import reborncore.client.models.ModelCompound;
import reborncore.client.models.RebornModelRegistry;
import techreborn.TechReborn;
import techreborn.client.render.ModelDynamicCell;
import techreborn.config.ConfigTechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRContent.*;

import java.util.Arrays;

/**
 * @author drcrazy
 *
 */
@Environment(EnvType.CLIENT)
@Mod.EventBusSubscriber(modid = TechReborn.MOD_ID)
public class ModelRegistryEventHandler {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerBlocks();
		registerFluidBlocks();
		registerItems();
		ModelDynamicCell.init();
		//RebornModelRegistry.registerModels(TechReborn.MOD_ID);
	}
	
	private static void registerBlocks() {
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, TRContent.REFINED_IRON_FENCE));
		RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, TRContent.RUBBER_SAPLING));

		for (Ores value : Ores.values()) {
			RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, value.block).setFileName("ore").setInvVariant("type=" + value.name));
		}

		for (StorageBlocks value : StorageBlocks.values()) {
			RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, value.block).setFileName("storage_block").setInvVariant("type=" + value.name));
		}

		for (MachineBlocks value : MachineBlocks.values()) {
			RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, value.frame).setFileName("machine_block").setInvVariant("type=" + value.name + "_machine_frame"));
			RebornModelRegistry.registerModel(new ModelCompound(TechReborn.MOD_ID, value.casing).setFileName("machine_block").setInvVariant("type=" + value.name + "_machine_casing"));
		}

		/*

		ResourceLocation cableRL = new ResourceLocation(TechReborn.MOD_ID, "cable_inv");
		for (Cables value : Cables.values()) {
			registerBlockstateMultiItem(cableRL, Item.getItemFromBlock(value.block), value.name);			
			ModelLoader.setCustomStateMapper(value.block, new DefaultStateMapper() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
					String property = this.getPropertyString(map) + ",ztype=" + value.name;

					return new ModelResourceLocation(new ResourceLocation(TechReborn.MOD_ID,
							"cable_" + (value.cableThickness == 5 ? "thick" : "thin")), property);
				}
			});
		}

		*/
	}


	private static void registerFluidBlocks() {
		/*
		registerFluidBlockModel(ModFluids.BLOCK_BERYLLIUM);
		registerFluidBlockModel(ModFluids.BLOCK_CALCIUM);
		registerFluidBlockModel(ModFluids.BLOCK_CALCIUM_CARBONATE);
		registerFluidBlockModel(ModFluids.BLOCK_CHLORITE);
		registerFluidBlockModel(ModFluids.BLOCK_DEUTERIUM);
		registerFluidBlockModel(ModFluids.BLOCK_GLYCERYL);
		registerFluidBlockModel(ModFluids.BLOCK_HELIUM);
		registerFluidBlockModel(ModFluids.BLOCK_HELIUM_3);
		registerFluidBlockModel(ModFluids.BLOCK_HELIUMPLASMA);
		registerFluidBlockModel(ModFluids.BLOCK_HYDROGEN);
		registerFluidBlockModel(ModFluids.BLOCK_LITHIUM);
		registerFluidBlockModel(ModFluids.BLOCK_MERCURY);
		registerFluidBlockModel(ModFluids.BLOCK_METHANE);
		registerFluidBlockModel(ModFluids.BLOCK_NITROCOAL_FUEL);
		registerFluidBlockModel(ModFluids.BLOCK_NITROFUEL);
		registerFluidBlockModel(ModFluids.BLOCK_NITROGEN);
		registerFluidBlockModel(ModFluids.BLOCK_NITROGENDIOXIDE);
		registerFluidBlockModel(ModFluids.BLOCK_POTASSIUM);
		registerFluidBlockModel(ModFluids.BLOCK_SILICON);
		registerFluidBlockModel(ModFluids.BLOCK_SODIUM);
		registerFluidBlockModel(ModFluids.BLOCK_SODIUMPERSULFATE);
		registerFluidBlockModel(ModFluids.BLOCK_TRITIUM);
		registerFluidBlockModel(ModFluids.BLOCK_WOLFRAMIUM);
		registerFluidBlockModel(ModFluids.BLOCK_CARBON);
		registerFluidBlockModel(ModFluids.BLOCK_CARBON_FIBER);
		registerFluidBlockModel(ModFluids.BLOCK_NITRO_CARBON);
		registerFluidBlockModel(ModFluids.BLOCK_SULFUR);
		registerFluidBlockModel(ModFluids.BLOCK_SODIUM_SULFIDE);
		registerFluidBlockModel(ModFluids.BLOCK_DIESEL);
		registerFluidBlockModel(ModFluids.BLOCK_NITRO_DIESEL);
		registerFluidBlockModel(ModFluids.BLOCK_OIL);
		registerFluidBlockModel(ModFluids.BLOCK_SULFURIC_ACID);
		registerFluidBlockModel(ModFluids.BLOCK_COMPRESSED_AIR);
		registerFluidBlockModel(ModFluids.BLOCK_ELECTROLYZED_WATER);
		*/
	}

	
	private static void registerItems() {
		// Armor
		register(TRContent.CLOAKING_DEVICE, "armor/cloaking_device");
		register(TRContent.LAPOTRONIC_ORBPACK, "armor/lapotronic_orbpack");
		register(TRContent.LITHIUM_ION_BATPACK, "armor/lithium_batpack");

		// Battery
		register(TRContent.RED_CELL_BATTERY, "battery/re_battery");
		register(TRContent.LITHIUM_ION_BATTERY, "battery/lithium_battery");
		register(TRContent.ENERGY_CRYSTAL, "battery/energy_crystal");
		register(TRContent.LAPOTRON_CRYSTAL, "battery/lapotron_crystal");
		register(TRContent.LAPOTRONIC_ORB, "battery/lapotronic_orb");

		// Tools
		register(TRContent.INDUSTRIAL_CHAINSAW, "tool/industrial_chainsaw");
		register(TRContent.INDUSTRIAL_DRILL, "tool/industrial_drill");
		register(TRContent.INDUSTRIAL_JACKHAMMER, "tool/industrial_jackhammer");
		register(TRContent.DEBUG_TOOL, "tool/debug");
		register(TRContent.ADVANCED_CHAINSAW, "tool/advanced_chainsaw");
		register(TRContent.ADVANCED_DRILL, "tool/advanced_drill");
		register(TRContent.ADVANCED_JACKHAMMER, "tool/advanced_jackhammer");
		register(TRContent.ELECTRIC_TREE_TAP, "tool/electric_treetap");
		register(TRContent.NANOSABER, "tool/nanosaber");
		register(TRContent.OMNI_TOOL, "tool/omni_tool");
		register(TRContent.ROCK_CUTTER, "tool/rock_cutter");
		register(TRContent.BASIC_CHAINSAW, "tool/basic_chainsaw");
		register(TRContent.BASIC_DRILL, "tool/basic_drill");
		register(TRContent.BASIC_JACKHAMMER, "tool/basic_jackhammer");
		register(TRContent.TREE_TAP, "tool/treetap");
		register(TRContent.WRENCH, "tool/wrench");

		// Other
		register(TRContent.FREQUENCY_TRANSMITTER, "misc/frequency_transmitter");
		register(TRContent.SCRAP_BOX, "misc/scrapbox");
		register(TRContent.MANUAL, "misc/manual");
		
		// Gem armor & tools
		if (ConfigTechReborn.enableGemArmorAndTools) {
			Identifier armourRL = new Identifier(TechReborn.MOD_ID, "items/armour");
			registerBlockstateMultiItem(armourRL, TRContent.RUBY_HELMET, "ruby_helmet");
			registerBlockstateMultiItem(armourRL, TRContent.RUBY_CHESTPLATE, "ruby_chestplate");
			registerBlockstateMultiItem(armourRL, TRContent.RUBY_LEGGINGS, "ruby_leggings");
			registerBlockstateMultiItem(armourRL, TRContent.RUBY_BOOTS, "ruby_boots");
			registerBlockstateMultiItem(armourRL, TRContent.SAPPHIRE_HELMET, "sapphire_helmet");
			registerBlockstateMultiItem(armourRL, TRContent.SAPPHIRE_CHESTPLATE, "sapphire_chestplate");
			registerBlockstateMultiItem(armourRL, TRContent.SAPPHIRE_LEGGINGS, "sapphire_leggings");
			registerBlockstateMultiItem(armourRL, TRContent.SAPPHIRE_BOOTS, "sapphire_boots");
			registerBlockstateMultiItem(armourRL, TRContent.PERIDOT_HELMET, "peridot_helmet");
			registerBlockstateMultiItem(armourRL, TRContent.PERIDOT_CHESTPLATE, "peridot_chestplate");
			registerBlockstateMultiItem(armourRL, TRContent.PERIDOT_LEGGINGS, "peridot_leggings");
			registerBlockstateMultiItem(armourRL, TRContent.PERIDOT_BOOTS, "peridot_boots");
			registerBlockstateMultiItem(armourRL, TRContent.BRONZE_HELMET, "bronze_helmet");
			registerBlockstateMultiItem(armourRL, TRContent.BRONZE_CHESTPLATE, "bronze_chestplate");
			registerBlockstateMultiItem(armourRL, TRContent.BRONZE_LEGGINGS, "bronze_leggings");
			registerBlockstateMultiItem(armourRL, TRContent.BRONZE_BOOTS, "bronze_boots");

			Identifier toolRL = new Identifier(TechReborn.MOD_ID, "items/tool");
			registerBlockstateMultiItem(toolRL, TRContent.RUBY_PICKAXE, "ruby_pickaxe");
			registerBlockstateMultiItem(toolRL, TRContent.RUBY_SWORD, "ruby_sword");
			registerBlockstateMultiItem(toolRL, TRContent.RUBY_AXE, "ruby_axe");
			registerBlockstateMultiItem(toolRL, TRContent.RUBY_SPADE, "ruby_spade");
			registerBlockstateMultiItem(toolRL, TRContent.RUBY_HOE, "ruby_hoe");
			registerBlockstateMultiItem(toolRL, TRContent.SAPPHIRE_PICKAXE, "sapphire_pickaxe");
			registerBlockstateMultiItem(toolRL, TRContent.SAPPHIRE_SWORD, "sapphire_sword");
			registerBlockstateMultiItem(toolRL, TRContent.SAPPHIRE_AXE, "sapphire_axe");
			registerBlockstateMultiItem(toolRL, TRContent.SAPPHIRE_SPADE, "sapphire_spade");
			registerBlockstateMultiItem(toolRL, TRContent.SAPPHIRE_HOE, "sapphire_hoe");
			registerBlockstateMultiItem(toolRL, TRContent.PERIDOT_PICKAXE, "peridot_pickaxe");
			registerBlockstateMultiItem(toolRL, TRContent.PERIDOT_SWORD, "peridot_sword");
			registerBlockstateMultiItem(toolRL, TRContent.PERIDOT_AXE, "peridot_axe");
			registerBlockstateMultiItem(toolRL, TRContent.PERIDOT_SPADE, "peridot_spade");
			registerBlockstateMultiItem(toolRL, TRContent.PERIDOT_HOE, "peridot_hoe");
			registerBlockstateMultiItem(toolRL, TRContent.BRONZE_PICKAXE, "bronze_pickaxe");
			registerBlockstateMultiItem(toolRL, TRContent.BRONZE_SWORD, "bronze_sword");
			registerBlockstateMultiItem(toolRL, TRContent.BRONZE_AXE, "bronze_axe");
			registerBlockstateMultiItem(toolRL, TRContent.BRONZE_SPADE, "bronze_spade");
			registerBlockstateMultiItem(toolRL, TRContent.BRONZE_HOE, "bronze_hoe");
		}
		
		Identifier dustsRL = new Identifier(TechReborn.MOD_ID, "items/material/dust");
		Arrays.stream(Dusts.values()).forEach(value -> registerBlockstateMultiItem(dustsRL, value.item, value.name));

		Identifier dustsSmallRL = new Identifier(TechReborn.MOD_ID, "items/material/small_dust");
		Arrays.stream(SmallDusts.values()).forEach(value -> registerBlockstateMultiItem(dustsSmallRL, value.item, value.name));

		Identifier gemsRL = new Identifier(TechReborn.MOD_ID, "items/material/gem");
		Arrays.stream(Gems.values()).forEach(value -> registerBlockstateMultiItem(gemsRL, value.item, value.name));

		Identifier ingotsRL = new Identifier(TechReborn.MOD_ID, "items/material/ingot");
		Arrays.stream(Ingots.values()).forEach(value -> registerBlockstateMultiItem(ingotsRL, value.item, value.name));

		Identifier nuggetsRL = new Identifier(TechReborn.MOD_ID, "items/material/nugget");
		Arrays.stream(Nuggets.values()).forEach(value -> registerBlockstateMultiItem(nuggetsRL, value.item, value.name));

		Identifier partsRL = new Identifier(TechReborn.MOD_ID, "items/material/part");
		Arrays.stream(Parts.values()).forEach(value -> registerBlockstateMultiItem(partsRL, value.item, value.name));

		Identifier platesRL = new Identifier(TechReborn.MOD_ID, "items/material/plate");
		Arrays.stream(Plates.values()).forEach(value -> registerBlockstateMultiItem(platesRL, value.item, value.name));
		
		Identifier upgradeRL = new Identifier(TechReborn.MOD_ID, "items/misc/upgrades");
		Arrays.stream(Upgrades.values()).forEach(value -> registerBlockstateMultiItem(upgradeRL, value.item, value.name));
		
	}

	/*

	private static void registerFluidBlockModel(BlockFluidTechReborn block) {
		String name = block.getTranslationKey().substring(5).toLowerCase();
		Item item = Item.getItemFromBlock(block);
		ModelResourceLocation location = new ModelResourceLocation(
				new ResourceLocation(TechReborn.MOD_ID, "fluids"), name);
		
		ModelLoader.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return location;
			}
		});
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return location;
			}
		});
	}

	*/
	
	private static void register(Item item, String modelPath) {
		RebornModelRegistry.registerItemModel(item, modelPath);
	}
	
	private static void registerBlockstateMultiItem(Identifier RL, Item item, String variantName) {
		//ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RL, "type=" + variantName));
	}

}
