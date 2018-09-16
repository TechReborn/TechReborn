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
import reborncore.api.tile.IUpgrade;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockStorage;
import techreborn.items.ItemUpgrade;
import techreborn.utils.InitUtils;

import java.util.Arrays;

@RebornRegister(modID = TechReborn.MOD_ID)
public class TRContent {

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

		Arrays.stream(Upgrades.values()).forEach(value -> RebornRegistry.registerItem(value.item));
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel() {
		ResourceLocation dustsRL = new ResourceLocation(TechReborn.MOD_ID, "items/material/dust");
		Arrays.stream(Dusts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(dustsRL, "type=" + value.name)));

		ResourceLocation dustsSmallRL = new ResourceLocation(TechReborn.MOD_ID, "items/material/small_dust");
		Arrays.stream(SmallDusts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(dustsSmallRL, "type=" + value.name)));

		ResourceLocation gemsRL = new ResourceLocation(TechReborn.MOD_ID, "items/material/gem");
		Arrays.stream(Gems.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(gemsRL, "type=" + value.name)));

		ResourceLocation ingotsRL = new ResourceLocation(TechReborn.MOD_ID, "items/material/ingot");
		Arrays.stream(Ingots.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(ingotsRL, "type=" + value.name)));

		ResourceLocation nuggetsRL = new ResourceLocation(TechReborn.MOD_ID, "items/material/nugget");
		Arrays.stream(Nuggets.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(nuggetsRL, "type=" + value.name)));

		ResourceLocation partsRL = new ResourceLocation(TechReborn.MOD_ID, "items/material/part");
		Arrays.stream(Parts.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(partsRL, "type=" + value.name)));

		ResourceLocation platesRL = new ResourceLocation(TechReborn.MOD_ID, "items/material/plate");
		Arrays.stream(Plates.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(platesRL, "type=" + value.name)));

		ResourceLocation oresRL = new ResourceLocation(TechReborn.MOD_ID, "ore");
		for (Ores value : Ores.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(value.block), 0, new ModelResourceLocation(oresRL, "type=" + value.name));
			ModelLoader.setCustomStateMapper(value.block, new DefaultStateMapper() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation(oresRL, "type=" + value.name);
				}
			});
		}

		ResourceLocation storageRL = new ResourceLocation(TechReborn.MOD_ID, "storage_block");
		for (StorageBlocks value : StorageBlocks.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(value.block), 0, new ModelResourceLocation(storageRL, "type=" + value.name));
			ModelLoader.setCustomStateMapper(value.block, new DefaultStateMapper() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation(storageRL, "type=" + value.name);
				}
			});
		}

		ResourceLocation upgradeRL = new ResourceLocation(TechReborn.MOD_ID, "items/misc/upgrades");
		Arrays.stream(Upgrades.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(upgradeRL, "type=" + value.name)));
	}

	public static enum Ores implements IStringSerializable {
		BAUXITE, CINNABAR, COPPER, GALENA, IRIDIUM, LEAD, PERIDOT, PYRITE, RUBY, SAPPHIRE, SHELDONITE, SILVER, SODALITE,
		SPHALERITE, TIN, TUNGSTEN;

		public final String name;
		public final Block block;

		private Ores() {
			name = this.toString().toLowerCase();
			block = new BlockOre();
			InitUtils.setupIngredient(block, name, "ore");
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

		public final String name;
		public final Block block;

		private StorageBlocks() {
			name = this.toString().toLowerCase();
			block = new BlockStorage();
			InitUtils.setupIngredient(block, name, "storage_block");
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

		public final String name;
		public final Item item;

		private Dusts() {
			name = this.toString().toLowerCase();
			item = new Item();
			InitUtils.setupIngredient(item, name, "dust");
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

		public final String name;
		public final Item item;

		private SmallDusts() {
			name = this.toString().toLowerCase();
			item = new Item();
			item.setRegistryName(new ResourceLocation(TechReborn.MOD_ID, name));
			item.setTranslationKey(TechReborn.MOD_ID + ".small_dust." + this.toString().toLowerCase());
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
			name = this.toString().toLowerCase();
			item = new Item();
			InitUtils.setupIngredient(item, name, "gem");
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
			name = this.toString().toLowerCase();
			item = new Item();
			InitUtils.setupIngredient(item, name, "ingot");
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
			name = this.toString().toLowerCase();
			item = new Item();
			InitUtils.setupIngredient(item, name, "nugget");
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
			item = new Item();
			InitUtils.setup(item, name);
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
			name = this.toString().toLowerCase();
			item = new Item();
			InitUtils.setupIngredient(item, name, "plate");
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

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclcoker_speed", comment = "Overclocker behavior speed multipiler")
	public static double overclockerSpeed = 0.25;

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclcoker_power", comment = "Overclocker behavior power multipiler")
	public static double overclockerPower = 0.75;

	@ConfigRegistry(config = "items", category = "upgrades", key = "energy_storage", comment = "Energy storage behavior extra power")
	public static double energyStoragePower = 40_000;

	public static enum Upgrades {
		OVERCLOCKER((tile, handler, stack) -> {
			TilePowerAcceptor powerAcceptor = null;
			if (tile instanceof TilePowerAcceptor) {
				powerAcceptor = (TilePowerAcceptor) tile;
			}
			handler.addSpeedMulti(overclockerSpeed);
			handler.addPowerMulti(overclockerPower);
			if (powerAcceptor != null) {
				powerAcceptor.extraPowerInput += powerAcceptor.getMaxInput();
				powerAcceptor.extraPowerStoage += powerAcceptor.getBaseMaxPower();
			}
		}),
		TRANSFORMER((tile, handler, stack) -> {
			TilePowerAcceptor powerAcceptor = null;
			if (tile instanceof TilePowerAcceptor) {
				powerAcceptor = (TilePowerAcceptor) tile;
			}
			if (powerAcceptor != null) {
				powerAcceptor.extraTier += 1;
			}
		}),
		ENERGY_STORAGE((tile, handler, stack) -> {
			TilePowerAcceptor powerAcceptor = null;
			if (tile instanceof TilePowerAcceptor) {
				powerAcceptor = (TilePowerAcceptor) tile;
			}
			if (powerAcceptor != null) {
				powerAcceptor.extraPowerStoage += energyStoragePower;
			}
		});

		public String name;
		public Item item;

		Upgrades(IUpgrade upgrade) {
			name = this.toString().toLowerCase();
			item = new ItemUpgrade(name, upgrade);
			InitUtils.setupIngredient(item, name, "_upgrade");
		}
	}
}
