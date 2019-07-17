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
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IUpgrade;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.blocks.*;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.generator.*;
import techreborn.blocks.lighting.BlockLamp;
import techreborn.blocks.storage.*;
import techreborn.blocks.tier0.BlockIronAlloyFurnace;
import techreborn.blocks.tier0.BlockIronFurnace;
import techreborn.blocks.tier1.*;
import techreborn.blocks.tier2.*;
import techreborn.blocks.tier3.*;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.config.ConfigTechReborn;
import techreborn.entities.EntityNukePrimed;
import techreborn.items.DynamicCell;
import techreborn.items.ItemUpgrade;
import techreborn.utils.InitUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Function;

@RebornRegister(TechReborn.MOD_ID)
public class TRContent {
	
	// Misc Blocks
	public static Block COMPUTER_CUBE;
	public static Block NUKE;
	public static Block REFINED_IRON_FENCE;
	public static Block REINFORCED_GLASS;
	public static Block RUBBER_LEAVES;
	public static Block RUBBER_LOG;
	public static Block RUBBER_PLANK_SLAB;
	public static Block RUBBER_PLANK_STAIR;
	public static Block RUBBER_PLANKS;
	public static Block RUBBER_SAPLING;
	
	// Armor
	public static Item CLOAKING_DEVICE;
	public static Item LAPOTRONIC_ORBPACK;
	public static Item LITHIUM_ION_BATPACK;

	// Battery
	public static Item ENERGY_CRYSTAL;
	public static Item LAPOTRON_CRYSTAL;
	public static Item LAPOTRONIC_ORB;
	public static Item LITHIUM_ION_BATTERY;
	public static Item RED_CELL_BATTERY;

	// Tools
	public static Item TREE_TAP;
	public static Item WRENCH;

	public static Item BASIC_CHAINSAW;
	public static Item BASIC_DRILL;
	public static Item BASIC_JACKHAMMER;
	public static Item ELECTRIC_TREE_TAP;

	public static Item ADVANCED_CHAINSAW;
	public static Item ADVANCED_DRILL;
	public static Item ADVANCED_JACKHAMMER;
	public static Item ROCK_CUTTER;

	public static Item INDUSTRIAL_CHAINSAW;
	public static Item INDUSTRIAL_DRILL;
	public static Item INDUSTRIAL_JACKHAMMER;
	public static Item NANOSABER;
	public static Item OMNI_TOOL;

	public static Item DEBUG_TOOL;

	// Other
	public static Item FREQUENCY_TRANSMITTER;
	public static Item SCRAP_BOX;
	public static Item MANUAL;
	public static DynamicCell CELL;

	// Gem armor & tools
	@Nullable
	public static Item BRONZE_SWORD;
	@Nullable
	public static Item BRONZE_PICKAXE;
	@Nullable
	public static Item BRONZE_SPADE;
	@Nullable
	public static Item BRONZE_AXE;
	@Nullable
	public static Item BRONZE_HOE;
	@Nullable
	public static Item BRONZE_HELMET;
	@Nullable
	public static Item BRONZE_CHESTPLATE;
	@Nullable
	public static Item BRONZE_LEGGINGS;
	@Nullable
	public static Item BRONZE_BOOTS;
	@Nullable
	public static Item RUBY_SWORD;
	@Nullable
	public static Item RUBY_PICKAXE;
	@Nullable
	public static Item RUBY_SPADE;
	@Nullable
	public static Item RUBY_AXE;
	@Nullable
	public static Item RUBY_HOE;
	@Nullable
	public static Item RUBY_HELMET;
	@Nullable
	public static Item RUBY_CHESTPLATE;
	@Nullable
	public static Item RUBY_LEGGINGS;
	@Nullable
	public static Item RUBY_BOOTS;
	@Nullable
	public static Item SAPPHIRE_SWORD;
	@Nullable
	public static Item SAPPHIRE_PICKAXE;
	@Nullable
	public static Item SAPPHIRE_SPADE;
	@Nullable
	public static Item SAPPHIRE_AXE;
	@Nullable
	public static Item SAPPHIRE_HOE;
	@Nullable
	public static Item SAPPHIRE_HELMET;
	@Nullable
	public static Item SAPPHIRE_CHESTPLATE;
	@Nullable
	public static Item SAPPHIRE_LEGGINGS;
	@Nullable
	public static Item SAPPHIRE_BOOTS;
	@Nullable
	public static Item PERIDOT_SWORD;
	@Nullable
	public static Item PERIDOT_PICKAXE;
	@Nullable
	public static Item PERIDOT_SPADE;
	@Nullable
	public static Item PERIDOT_AXE;
	@Nullable
	public static Item PERIDOT_HOE;
	@Nullable
	public static Item PERIDOT_HELMET;
	@Nullable
	public static Item PERIDOT_CHESTPLATE;
	@Nullable
	public static Item PERIDOT_LEGGINGS;
	@Nullable
	public static Item PERIDOT_BOOTS;
		
	public static enum SolarPanels implements ItemConvertible {
		BASIC(EnumPowerTier.MICRO, ConfigTechReborn.basicGenerationRateD, ConfigTechReborn.basicGenerationRateN), 
		ADVANCED(EnumPowerTier.LOW, ConfigTechReborn.advancedGenerationRateD, ConfigTechReborn.advancedGenerationRateN), 
		INDUSTRIAL(EnumPowerTier.MEDIUM, ConfigTechReborn.industrialGenerationRateD, ConfigTechReborn.industrialGenerationRateN),
		ULTIMATE(EnumPowerTier.HIGH, ConfigTechReborn.ultimateGenerationRateD, ConfigTechReborn.ultimateGenerationRateN), 
		QUANTUM(EnumPowerTier.EXTREME, ConfigTechReborn.quantumGenerationRateD, ConfigTechReborn.quantumGenerationRateN), 
		CREATIVE(EnumPowerTier.INFINITE, Integer.MAX_VALUE / 100, Integer.MAX_VALUE / 100);
		
		public final String name;
		public final Block block;
		
		// Generation of EU during Day
		public int generationRateD = 1;
		// Generation of EU during Night
		public int generationRateN = 0;
		// Internal EU storage of solar panel
		public int internalCapacity = 1000;
		public final EnumPowerTier powerTier;
		
		private SolarPanels(EnumPowerTier tier, int generationRateD, int generationRateN) {
			name = this.toString().toLowerCase();
			powerTier = tier;
			block = new BlockSolarPanel(this);
			this.generationRateD = generationRateD;
			this.generationRateN = generationRateN;
			// Buffer for 2 mins of work
			internalCapacity = generationRateD * 2_400;
			
			InitUtils.setup(block, name + "_solar_panel");
		}

		@Override
		public Item asItem() {
			return Item.fromBlock(block);
		}
	}

	public static enum Cables implements ItemConvertible {
		COPPER(128, 12.0, true, EnumPowerTier.MEDIUM),
		TIN(32, 12.0, true, EnumPowerTier.LOW),
		GOLD(512, 12.0, true, EnumPowerTier.HIGH),
		HV(2048, 12.0, true, EnumPowerTier.EXTREME),
		GLASSFIBER(8192, 12.0, false, EnumPowerTier.INSANE),
		INSULATED_COPPER(128, 10.0, false, EnumPowerTier.MEDIUM),
		INSULATED_GOLD(512, 10.0, false, EnumPowerTier.HIGH),
		INSULATED_HV(2048, 10.0, false, EnumPowerTier.EXTREME),
		SUPERCONDUCTOR(Integer.MAX_VALUE / 4, 10.0, false, EnumPowerTier.INFINITE);
		

		public final String name;
		public final BlockCable block;

		public int transferRate;
		public int defaultTransferRate;
		public double cableThickness;
		public boolean canKill;
		public boolean defaultCanKill;
		public EnumPowerTier tier;
		
		
		Cables(int transferRate, double cableThickness, boolean canKill, EnumPowerTier tier) {
			name = this.toString().toLowerCase();
			this.transferRate = transferRate;
			this.defaultTransferRate = transferRate;
			this.cableThickness = cableThickness / 2;
			this.canKill = canKill;
			this.defaultCanKill = canKill;
			this.tier = tier;
			this.block = new BlockCable(this);
			InitUtils.setup(block, name + "_cable");
		}
		
		public ItemStack getStack() {
			return new ItemStack(block);
		}
		
		@Override
		public Item asItem() {
			return block.asItem();
		}
	}

	public static enum Ores implements ItemConvertible {
		BAUXITE(6, 10, 10, 60), 
		CINNABAR(6, 3, 10, 126), 
		COPPER(8, 16, 20, 60), 
		GALENA(8, 16, 10, 60), 
		IRIDIUM(3, 3, 5, 60), 
		LEAD(6, 16, 20, 60), 
		PERIDOT(6, 3, 10, 250), 
		PYRITE(6, 3, 10, 126), 
		RUBY(6, 3, 10, 60), 
		SAPPHIRE(6, 3, 10, 60), 
		SHELDONITE(6, 3, 10, 250), 
		SILVER(6, 16, 20, 60), 
		SODALITE(6, 3, 10, 250),
		SPHALERITE(6, 3, 10, 126), 
		TIN(8, 16, 20, 60), 
		TUNGSTEN(6, 3, 10, 250);

		public final String name;
		public final Block block;
		public final int veinSize;
		public final int veinsPerChunk;
		public final int minY;
		public final int maxY;

		private Ores(int veinSize, int veinsPerChunk, int minY, int maxY) {
			name = this.toString().toLowerCase();
			block = new BlockOre();
			this.veinSize = veinSize;
			this.veinsPerChunk = veinsPerChunk;
			this.minY = minY;
			this.maxY = maxY;
			InitUtils.setup(block, name + "_ore");
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}
	}

	public static enum StorageBlocks implements ItemConvertible {
		ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, INVAR, IRIDIUM, IRIDIUM_REINFORCED_STONE,
		IRIDIUM_REINFORCED_TUNGSTENSTEEL, LEAD, NICKEL, OSMIUM, PERIDOT, PLATINUM, RED_GARNET, REFINED_IRON, RUBY,
		SAPPHIRE, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, YELLOW_GARNET, ZINC;

		public final String name;
		public final Block block;

		private StorageBlocks() {
			name = this.toString().toLowerCase();
			block = new BlockStorage();
			InitUtils.setup(block, name + "_storage_block");
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}
	}

	public static enum MachineBlocks {
		BASIC(1020 / 25),
		ADVANCED(1700 / 25),
		INDUSTRIAL(2380 / 25);

		public final String name;
		public final Block frame;
		public final Block casing;

		private MachineBlocks(int casingHeatCapacity) {
			name = this.toString().toLowerCase();
			frame = new BlockMachineFrame();
			InitUtils.setup(frame, name + "_machine_frame");
			casing = new BlockMachineCasing(casingHeatCapacity);
			InitUtils.setup(casing, name + "_machine_casing");
		}

		public Block getFrame() {
			return frame;
		}

		public Block getCasing() {
			return casing;
		}

		public static ItemConvertible[] getCasings(){
			return Arrays.stream(MachineBlocks.values()).map((Function<MachineBlocks, ItemConvertible>) machineBlocks -> () -> Item.fromBlock(machineBlocks.casing)).toArray(ItemConvertible[]::new);
		}
	}
	
	
	public static enum Machine implements ItemConvertible {
		ALLOY_SMELTER(new BlockAlloySmelter()),
		ASSEMBLY_MACHINE(new BlockAssemblingMachine()),
		AUTO_CRAFTING_TABLE(new BlockAutoCraftingTable()),
		CHEMICAL_REACTOR(new BlockChemicalReactor()),
		COMPRESSOR(new BlockCompressor()),
		DISTILLATION_TOWER(new BlockDistillationTower()),
		EXTRACTOR(new BlockExtractor()),
		FLUID_REPLICATOR(new BlockFluidReplicator()),
		GRINDER(new BlockGrinder()),
		ELECTRIC_FURNACE(new BlockElectricFurnace()),
		IMPLOSION_COMPRESSOR(new BlockImplosionCompressor()),
		INDUSTRIAL_BLAST_FURNACE(new BlockIndustrialBlastFurnace()),
		INDUSTRIAL_CENTRIFUGE(new BlockIndustrialCentrifuge()),
		INDUSTRIAL_ELECTROLYZER(new BlockIndustrialElectrolyzer()),
		INDUSTRIAL_GRINDER(new BlockIndustrialGrinder()),
		INDUSTRIAL_SAWMILL(new BlockIndustrialSawmill()),
		IRON_ALLOY_FURNACE(new BlockIronAlloyFurnace()),
		IRON_FURNACE(new BlockIronFurnace()),
		MATTER_FABRICATOR(new BlockMatterFabricator()),
		RECYCLER(new BlockRecycler()),
		ROLLING_MACHINE(new BlockRollingMachine()),
		SCRAPBOXINATOR(new BlockScrapboxinator()),
		VACUUM_FREEZER(new BlockVacuumFreezer()),
		
		DIESEL_GENERATOR(new BlockDieselGenerator()),
		DRAGON_EGG_SYPHON(new BlockDragonEggSyphon()),
		FUSION_COIL(new BlockFusionCoil()),
		FUSION_CONTROL_COMPUTER(new BlockFusionControlComputer()),
		GAS_TURBINE(new BlockGasTurbine()),
		LIGHTNING_ROD(new BlockLightningRod()),
		MAGIC_ENERGY_ABSORBER (new BlockMagicEnergyAbsorber()),
		MAGIC_ENERGY_CONVERTER(new BlockMagicEnergyConverter()),
		PLASMA_GENERATOR(new BlockPlasmaGenerator()),
		SEMI_FLUID_GENERATOR(new BlockSemiFluidGenerator()),
		SOLID_FUEL_GENERATOR(new BlockSolidFuelGenerator()),
		THERMAL_GENERATOR(new BlockThermalGenerator()),
		WATER_MILL(new BlockWaterMill()),
		WIND_MILL(new BlockWindMill()),
		
		CREATIVE_QUANTUM_CHEST(new BlockCreativeQuantumChest()),
		CREATIVE_QUANTUM_TANK(new BlockCreativeQuantumTank()),
		DIGITAL_CHEST(new BlockDigitalChest()),
		QUANTUM_CHEST(new BlockQuantumChest()),
		QUANTUM_TANK(new BlockQuantumTank()),
		
		ADJUSTABLE_SU(new BlockAdjustableSU()),
		CHARGE_O_MAT(new BlockChargeOMat()),
		INTERDIMENSIONAL_SU(new BlockInterdimensionalSU()),
		LAPOTRONIC_SU(new BlockLapotronicSU()),
		LSU_STORAGE(new BlockLSUStorage()),
		LOW_VOLTAGE_SU(new BlockLowVoltageSU()),
		MEDIUM_VOLTAGE_SU(new BlockMediumVoltageSU()),
		HIGH_VOLTAGE_SU(new BlockHighVoltageSU()),
		LV_TRANSFORMER(new BlockLVTransformer()),
		MV_TRANSFORMER(new BlockMVTransformer()),
		HV_TRANSFORMER(new BlockHVTransformer()),
		
		ALARM(new BlockAlarm()),
		CHUNK_LOADER(new BlockChunkLoader()),
		LAMP_INCANDESCENT(new BlockLamp(14, 4, 10, 8)),
		LAMP_LED(new BlockLamp(15, 1, 1, 12)),
		PLAYER_DETECTOR(new BlockPlayerDetector());
		
		public final String name;
		public final Block block;

		private <B extends Block> Machine(B block) {
			this.name = this.toString().toLowerCase();
			this.block = block;
			InitUtils.setup(block, name);
		}
		
		public ItemStack getStack() {
			return new ItemStack(block);
		}
		
		@Override
		public Item asItem() {
			return block.asItem();
		}
	}
	
	public static enum Dusts implements ItemConvertible {
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
			item = new Item(new Item.Settings().group(TechReborn.ITEMGROUP));
			InitUtils.setup(item, name + "_dust");
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	public static enum SmallDusts implements ItemConvertible {
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
			item = new Item(new Item.Settings().group(TechReborn.ITEMGROUP));
			InitUtils.setup(item, name + "_small_dust");
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	public static enum Gems implements ItemConvertible {
		PERIDOT, RED_GARNET, RUBY, SAPPHIRE, YELLOW_GARNET;

		public final String name;
		public final Item item;

		private Gems() {
			name = this.toString().toLowerCase();
			item = new Item(new Item.Settings().group(TechReborn.ITEMGROUP));
			InitUtils.setup(item, name + "_gem");
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	public static enum Ingots implements ItemConvertible {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM_ALLOY, IRIDIUM,
		LEAD, MIXED_METAL, NICKEL, PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;

		private Ingots() {
			name = this.toString().toLowerCase();
			item = new Item(new Item.Settings().group(TechReborn.ITEMGROUP));
			InitUtils.setup(item, name + "_ingot");
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	public static enum Nuggets implements ItemConvertible {
		ALUMINUM, BRASS, BRONZE, CHROME, COPPER, DIAMOND, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM, LEAD, NICKEL,
		PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;

		private Nuggets() {
			name = this.toString().toLowerCase();
			item = new Item(new Item.Settings().group(TechReborn.ITEMGROUP));
			InitUtils.setup(item, name + "_nugget");
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	public static enum Parts implements ItemConvertible {
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
		HELIUM_COOLANT_CELL_180K,
		HELIUM_COOLANT_CELL_360K,

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
			item = new Item(new Item.Settings().group(TechReborn.ITEMGROUP));
			InitUtils.setup(item, name);
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	public static enum Plates implements ItemConvertible {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CARBON, COAL, COPPER, DIAMOND, ELECTRUM, EMERALD, GOLD, INVAR,
		IRIDIUM_ALLOY, IRIDIUM, IRON, LAPIS, LAZURITE, LEAD, MAGNALIUM, NICKEL, OBSIDIAN, PERIDOT, PLATINUM, RED_GARNET,
		REDSTONE, REFINED_IRON, RUBY, SAPPHIRE, SILICON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, WOOD,
		YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		private Plates() {
			name = this.toString().toLowerCase();
			item = new Item(new Item.Settings().group(TechReborn.ITEMGROUP));
			InitUtils.setup(item, name + "_plate");
		}

		public ItemStack getStack() {
			return new ItemStack(item);
		}

		public ItemStack getStack(int amount) {
			return new ItemStack(item, amount);
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclcoker_speed", comment = "Overclocker behavior speed multipiler")
	public static double overclockerSpeed = 0.25;

	@ConfigRegistry(config = "items", category = "upgrades", key = "overclcoker_power", comment = "Overclocker behavior power multipiler")
	public static double overclockerPower = 0.75;

	@ConfigRegistry(config = "items", category = "upgrades", key = "energy_storage", comment = "Energy storage behavior extra power")
	public static double energyStoragePower = 40_000;

	public static enum Upgrades implements ItemConvertible {
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
			InitUtils.setup(item, name + "_upgrade");
		}

		@Override
		public Item asItem() {
			return item;
		}
	}
	
	public static EntityType<EntityNukePrimed> ENTITY_NUKE;
}
