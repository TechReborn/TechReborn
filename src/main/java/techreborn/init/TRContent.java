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

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import reborncore.api.blockentity.IUpgrade;
import reborncore.common.fluid.FluidValue;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.blockentity.machine.misc.ChargeOMatBlockEntity;
import techreborn.blockentity.machine.misc.DrainBlockEntity;
import techreborn.blockentity.storage.fluid.CreativeQuantumTankBlockEntity;
import techreborn.blockentity.storage.fluid.QuantumTankBlockEntity;
import techreborn.blockentity.machine.tier3.IndustrialCentrifugeBlockEntity;
import techreborn.blockentity.generator.LightningRodBlockEntity;
import techreborn.blockentity.generator.PlasmaGeneratorBlockEntity;
import techreborn.blockentity.generator.advanced.*;
import techreborn.blockentity.generator.basic.SolidFuelGeneratorBlockEntity;
import techreborn.blockentity.generator.basic.WaterMillBlockEntity;
import techreborn.blockentity.generator.basic.WindMillBlockEntity;
import techreborn.blockentity.machine.multiblock.*;
import techreborn.blockentity.machine.tier1.*;
import techreborn.blockentity.machine.tier3.*;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.item.CreativeQuantumChestBlockEntity;
import techreborn.blockentity.storage.item.DigitalChestBlockEntity;
import techreborn.blockentity.storage.item.QuantumChestBlockEntity;
import techreborn.blocks.*;
import techreborn.blocks.cable.CableBlock;
import techreborn.blocks.generator.*;
import techreborn.blocks.lighting.BlockLamp;
import techreborn.blocks.misc.BlockAlarm;
import techreborn.blocks.misc.BlockMachineCasing;
import techreborn.blocks.misc.BlockMachineFrame;
import techreborn.blocks.misc.BlockStorage;
import techreborn.blocks.storage.OldBlock;
import techreborn.blocks.storage.energy.*;
import techreborn.blocks.machine.tier0.IronAlloyFurnaceBlock;
import techreborn.blocks.machine.tier0.IronFurnaceBlock;
import techreborn.blocks.machine.tier1.BlockPlayerDetector;
import techreborn.blocks.storage.item.StorageUnitBlock;
import techreborn.blocks.storage.fluid.TankUnitBlock;
import techreborn.blocks.transformers.BlockEVTransformer;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.client.EGui;
import techreborn.config.TechRebornConfig;
import techreborn.entities.EntityNukePrimed;
import techreborn.items.ItemDynamicCell;
import techreborn.items.ItemUpgrade;
import techreborn.items.armor.ItemQuantumSuit;
import techreborn.utils.InitUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

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
	public static Block RUBBER_FENCE;
	public static Block RUBBER_FENCE_GATE;
	public static Block RUBBER_TRAPDOOR;
	public static Block RUBBER_BUTTON;
	public static Block RUBBER_PRESSURE_PLATE;
	public static Block RUBBER_DOOR;
	public static Block RUBBER_LOG_STRIPPED;
	
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
	public static Item PAINTING_TOOL;

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
	public static ItemDynamicCell CELL;

	//Quantum Suit
	public static ItemQuantumSuit QUANTUM_HELMET;
	public static ItemQuantumSuit QUANTUM_CHESTPLATE;
	public static ItemQuantumSuit QUANTUM_LEGGINGS;
	public static ItemQuantumSuit QUANTUM_BOOTS;

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
		
	public enum SolarPanels implements ItemConvertible {
		BASIC(EnergyTier.MICRO, TechRebornConfig.basicGenerationRateD, TechRebornConfig.basicGenerationRateN),
		ADVANCED(EnergyTier.LOW, TechRebornConfig.advancedGenerationRateD, TechRebornConfig.advancedGenerationRateN),
		INDUSTRIAL(EnergyTier.MEDIUM, TechRebornConfig.industrialGenerationRateD, TechRebornConfig.industrialGenerationRateN),
		ULTIMATE(EnergyTier.HIGH, TechRebornConfig.ultimateGenerationRateD, TechRebornConfig.ultimateGenerationRateN),
		QUANTUM(EnergyTier.EXTREME, TechRebornConfig.quantumGenerationRateD, TechRebornConfig.quantumGenerationRateN),
		CREATIVE(EnergyTier.INFINITE, Integer.MAX_VALUE / 100, Integer.MAX_VALUE / 100);
		
		public final String name;
		public final Block block;
		
		// Generation of EU during Day
		public int generationRateD;
		// Generation of EU during Night
		public int generationRateN;
		// Internal EU storage of solar panel
		public int internalCapacity;
		public final EnergyTier powerTier;
		
		SolarPanels(EnergyTier tier, int generationRateD, int generationRateN) {
			name = this.toString().toLowerCase(Locale.ROOT);
			powerTier = tier;
			block = new BlockSolarPanel(this);
			this.generationRateD = generationRateD;
			this.generationRateN = generationRateN;

			internalCapacity = generationRateD * TechRebornConfig.solarInternalCapacityMultiplier;
			
			InitUtils.setup(block, name + "_solar_panel");
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}
	}

	public enum StorageUnit implements ItemConvertible {
		CRUDE(TechRebornConfig.crudeStorageUnitMaxStorage),
		BASIC(TechRebornConfig.basicStorageUnitMaxStorage),
		ADVANCED(TechRebornConfig.advancedStorageUnitMaxStorage),
		INDUSTRIAL(TechRebornConfig.industrialStorageUnitMaxStorage),
		QUANTUM(TechRebornConfig.quantumStorageUnitMaxStorage),
		CREATIVE(Integer.MAX_VALUE);

		public final String name;
		public final Block block;

		// How many blocks it can hold
		public int capacity;


		StorageUnit(int capacity) {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new StorageUnitBlock(this);
			this.capacity = capacity;

			InitUtils.setup(block, name + "_storage_unit");
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}
	}

	public enum TankUnit implements ItemConvertible {
		BASIC(TechRebornConfig.basicTankUnitCapacity),
		ADVANCED(TechRebornConfig.advancedTankUnitMaxStorage),
		INDUSTRIAL(TechRebornConfig.industrialTankUnitCapacity),
		QUANTUM(TechRebornConfig.quantumTankUnitCapacity),
		CREATIVE(Integer.MAX_VALUE / 1000);

		public final String name;
		public final Block block;

		// How many blocks it can hold
		public FluidValue capacity;


		TankUnit(int capacity) {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new TankUnitBlock(this);
			this.capacity = FluidValue.BUCKET.multiply(capacity);

			InitUtils.setup(block, name + "_tank_unit");
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}
	}

	public enum Cables implements ItemConvertible {
		COPPER(128, 12.0, true, EnergyTier.MEDIUM),
		TIN(32, 12.0, true, EnergyTier.LOW),
		GOLD(512, 12.0, true, EnergyTier.HIGH),
		HV(2048, 12.0, true, EnergyTier.EXTREME),
		GLASSFIBER(8192, 12.0, false, EnergyTier.INSANE),
		INSULATED_COPPER(128, 10.0, false, EnergyTier.MEDIUM),
		INSULATED_GOLD(512, 10.0, false, EnergyTier.HIGH),
		INSULATED_HV(2048, 10.0, false, EnergyTier.EXTREME),
		SUPERCONDUCTOR(Integer.MAX_VALUE / 4, 10.0, false, EnergyTier.INFINITE);
		

		public final String name;
		public final CableBlock block;

		public int transferRate;
		public int defaultTransferRate;
		public double cableThickness;
		public boolean canKill;
		public boolean defaultCanKill;
		public EnergyTier tier;
		
		
		Cables(int transferRate, double cableThickness, boolean canKill, EnergyTier tier) {
			name = this.toString().toLowerCase(Locale.ROOT);
			this.transferRate = transferRate;
			this.defaultTransferRate = transferRate;
			this.cableThickness = cableThickness / 2;
			this.canKill = canKill;
			this.defaultCanKill = canKill;
			this.tier = tier;
			this.block = new CableBlock(this);
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

	public enum Ores implements ItemConvertible {
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

		Ores(int veinSize, int veinsPerChunk, int minY, int maxY) {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new OreBlock(FabricBlockSettings.of(Material.STONE).strength(2f, 2f).build());
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

	public enum StorageBlocks implements ItemConvertible {
		ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, INVAR, IRIDIUM, IRIDIUM_REINFORCED_STONE,
		IRIDIUM_REINFORCED_TUNGSTENSTEEL, LEAD, NICKEL, PERIDOT, PLATINUM, RED_GARNET, REFINED_IRON, RUBY,
		SAPPHIRE, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, YELLOW_GARNET, ZINC;

		public final String name;
		public final Block block;

		StorageBlocks() {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new BlockStorage();
			InitUtils.setup(block, name + "_storage_block");
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}
	}

	public enum MachineBlocks {
		BASIC(1020 / 25),
		ADVANCED(1700 / 25),
		INDUSTRIAL(2380 / 25);

		public final String name;
		public final Block frame;
		public final Block casing;

		MachineBlocks(int casingHeatCapacity) {
			name = this.toString().toLowerCase(Locale.ROOT);
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
	
	
	public enum Machine implements ItemConvertible {
		ALLOY_SMELTER(new GenericMachineBlock(EGui.ALLOY_SMELTER, AlloySmelterBlockEntity::new)),
		ASSEMBLY_MACHINE(new GenericMachineBlock(EGui.ASSEMBLING_MACHINE, AssemblingMachineBlockEntity::new)),
		AUTO_CRAFTING_TABLE(new GenericMachineBlock(EGui.AUTO_CRAFTING_TABLE, AutoCraftingTableBlockEntity::new)),
		CHEMICAL_REACTOR(new GenericMachineBlock(EGui.CHEMICAL_REACTOR, ChemicalReactorBlockEntity::new)),
		COMPRESSOR(new GenericMachineBlock(EGui.COMPRESSOR, CompressorBlockEntity::new)),
		DISTILLATION_TOWER(new GenericMachineBlock(EGui.DISTILLATION_TOWER, DistillationTowerBlockEntity::new)),
		EXTRACTOR(new GenericMachineBlock(EGui.EXTRACTOR, ExtractorBlockEntity::new)),
		FLUID_REPLICATOR(new GenericMachineBlock(EGui.FLUID_REPLICATOR, FluidReplicatorBlockEntity::new)),
		GRINDER(new DataDrivenMachineBlock("techreborn:grinder")),
		ELECTRIC_FURNACE(new GenericMachineBlock(EGui.ELECTRIC_FURNACE, ElectricFurnaceBlockEntity::new)),
		IMPLOSION_COMPRESSOR(new GenericMachineBlock(EGui.IMPLOSION_COMPRESSOR, ImplosionCompressorBlockEntity::new)),
		INDUSTRIAL_BLAST_FURNACE(new GenericMachineBlock(EGui.BLAST_FURNACE, IndustrialBlastFurnaceBlockEntity::new)),
		INDUSTRIAL_CENTRIFUGE(new GenericMachineBlock(EGui.CENTRIFUGE, IndustrialCentrifugeBlockEntity::new)),
		INDUSTRIAL_ELECTROLYZER(new GenericMachineBlock(EGui.INDUSTRIAL_ELECTROLYZER, IndustrialElectrolyzerBlockEntity::new)),
		INDUSTRIAL_GRINDER(new GenericMachineBlock(EGui.INDUSTRIAL_GRINDER, IndustrialGrinderBlockEntity::new)),
		INDUSTRIAL_SAWMILL(new GenericMachineBlock(EGui.SAWMILL, IndustrialSawmillBlockEntity::new)),
		IRON_ALLOY_FURNACE(new IronAlloyFurnaceBlock()),
		IRON_FURNACE(new IronFurnaceBlock()),
		MATTER_FABRICATOR(new GenericMachineBlock(EGui.MATTER_FABRICATOR, MatterFabricatorBlockEntity::new)),
		RECYCLER(new GenericMachineBlock(EGui.RECYCLER, RecyclerBlockEntity::new)),
		ROLLING_MACHINE(new GenericMachineBlock(EGui.ROLLING_MACHINE, RollingMachineBlockEntity::new)),
		SCRAPBOXINATOR(new GenericMachineBlock(EGui.SCRAPBOXINATOR, ScrapboxinatorBlockEntity::new)),
		VACUUM_FREEZER(new GenericMachineBlock(EGui.VACUUM_FREEZER, VacuumFreezerBlockEntity::new)),
		SOLID_CANNING_MACHINE(new GenericMachineBlock(EGui.SOLID_CANNING_MACHINE, SoildCanningMachineBlockEntity::new)),
		WIRE_MILL(new GenericMachineBlock(EGui.WIRE_MILL, WireMillBlockEntity::new)),
		GREENHOUSE_CONTROLLER(new GenericMachineBlock(EGui.GREENHOUSE_CONTROLLER, GreenhouseControllerBlockEntity::new)),
		
		DIESEL_GENERATOR(new GenericGeneratorBlock(EGui.DIESEL_GENERATOR, DieselGeneratorBlockEntity::new)),
		DRAGON_EGG_SYPHON(new GenericGeneratorBlock(null, DragonEggSyphonBlockEntity::new)),
		FUSION_COIL(new BlockFusionCoil()),
		FUSION_CONTROL_COMPUTER(new BlockFusionControlComputer()),
		GAS_TURBINE(new GenericGeneratorBlock(EGui.GAS_TURBINE, GasTurbineBlockEntity::new)),
		LIGHTNING_ROD(new GenericGeneratorBlock(null, LightningRodBlockEntity::new)),
		PLASMA_GENERATOR(new GenericGeneratorBlock(EGui.PLASMA_GENERATOR, PlasmaGeneratorBlockEntity::new)),
		SEMI_FLUID_GENERATOR(new GenericGeneratorBlock(EGui.SEMIFLUID_GENERATOR, SemiFluidGeneratorBlockEntity::new)),
		SOLID_FUEL_GENERATOR(new GenericGeneratorBlock(EGui.GENERATOR, SolidFuelGeneratorBlockEntity::new)),
		THERMAL_GENERATOR(new GenericGeneratorBlock(EGui.THERMAL_GENERATOR, ThermalGeneratorBlockEntity::new)),
		WATER_MILL(new GenericGeneratorBlock(null, WaterMillBlockEntity::new)),
		WIND_MILL(new GenericGeneratorBlock(null, WindMillBlockEntity::new)),

		DRAIN(new GenericMachineBlock(null, DrainBlockEntity::new)),

		//TODO DEPRECATED
		DIGITAL_CHEST(new OldBlock(null, DigitalChestBlockEntity::new)),
		QUANTUM_CHEST(new OldBlock(null, QuantumChestBlockEntity::new)),
		QUANTUM_TANK(new OldBlock(null, QuantumTankBlockEntity::new)),
		CREATIVE_QUANTUM_CHEST(new OldBlock(null, CreativeQuantumChestBlockEntity::new)),
		CREATIVE_QUANTUM_TANK(new OldBlock(null, CreativeQuantumTankBlockEntity::new)),


		ADJUSTABLE_SU(new AdjustableSUBlock()),
		CHARGE_O_MAT(new GenericMachineBlock(EGui.CHARGEBENCH, ChargeOMatBlockEntity::new)),
		INTERDIMENSIONAL_SU(new InterdimensionalSUBlock()),
		LAPOTRONIC_SU(new LapotronicSUBlock()),
		LSU_STORAGE(new LSUStorageBlock()),
		LOW_VOLTAGE_SU(new LowVoltageSUBlock()),
		MEDIUM_VOLTAGE_SU(new MediumVoltageSUBlock()),
		HIGH_VOLTAGE_SU(new HighVoltageSUBlock()),
		LV_TRANSFORMER(new BlockLVTransformer()),
		MV_TRANSFORMER(new BlockMVTransformer()),
		HV_TRANSFORMER(new BlockHVTransformer()),
		EV_TRANSFORMER(new BlockEVTransformer()),
		
		ALARM(new BlockAlarm()),
		CHUNK_LOADER(new GenericMachineBlock(EGui.CHUNK_LOADER, ChunkLoaderBlockEntity::new)),
		LAMP_INCANDESCENT(new BlockLamp(4, 10, 8)),
		LAMP_LED(new BlockLamp(1, 1, 12)),
		PLAYER_DETECTOR(new BlockPlayerDetector());
		
		public final String name;
		public final Block block;

		<B extends Block> Machine(B block) {
			this.name = this.toString().toLowerCase(Locale.ROOT);
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
	
	public enum Dusts implements ItemConvertible {
		ALMANDINE, ALUMINUM, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME,
		CINNABAR, CLAY, COAL, COPPER, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GOLD, GRANITE, GROSSULAR, INVAR, IRON, LAZURITE, LEAD, MAGNESIUM, MANGANESE, MARBLE, NETHERRACK,
		NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, RED_GARNET, RUBY, SALTPETER,
		SAPPHIRE, SAW, SILVER, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TIN, TITANIUM, TUNGSTEN, UVAROVITE,
		YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		Dusts() {
			name = this.toString().toLowerCase(Locale.ROOT);
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

	public enum SmallDusts implements ItemConvertible {
		ALMANDINE, ALUMINUM, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME,
		CINNABAR, CLAY, COAL, COPPER, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GLOWSTONE, GOLD, GRANITE, GROSSULAR, INVAR, IRON, LAZURITE, LEAD, MAGNESIUM, MANGANESE, MARBLE,
		NETHERRACK, NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, REDSTONE, RED_GARNET,
		RUBY, SALTPETER, SAPPHIRE, SAW, SILVER, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TIN, TITANIUM,
		TUNGSTEN, UVAROVITE, YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		SmallDusts() {
			name = this.toString().toLowerCase(Locale.ROOT);
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

	public enum Gems implements ItemConvertible {
		PERIDOT, RED_GARNET, RUBY, SAPPHIRE, YELLOW_GARNET;

		public final String name;
		public final Item item;

		Gems() {
			name = this.toString().toLowerCase(Locale.ROOT);
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

	public enum Ingots implements ItemConvertible {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM_ALLOY, IRIDIUM,
		LEAD, MIXED_METAL, NICKEL, PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;

		Ingots() {
			name = this.toString().toLowerCase(Locale.ROOT);
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

	public enum Nuggets implements ItemConvertible {
		ALUMINUM, BRASS, BRONZE, CHROME, COPPER, DIAMOND, ELECTRUM, EMERALD, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM, LEAD, NICKEL,
		PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;

		Nuggets() {
			name = this.toString().toLowerCase(Locale.ROOT);
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

	public enum Parts implements ItemConvertible {
		CARBON_FIBER,
		CARBON_MESH,

		ELECTRONIC_CIRCUIT,
		ADVANCED_CIRCUIT,
		INDUSTRIAL_CIRCUIT,

		MACHINE_PARTS,
		BASIC_DISPLAY,
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
		UU_MATTER,
		PLANTBALL,
		COMPRESSED_PLANTBALL;

		public final String name;
		public final Item item;

		Parts() {
			name = this.toString().toLowerCase(Locale.ROOT);
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

	public enum Plates implements ItemConvertible {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CARBON, CHROME, COAL, COPPER, DIAMOND, ELECTRUM, EMERALD, GOLD, INVAR,
		IRIDIUM_ALLOY, IRIDIUM, IRON, LAPIS, LAZURITE, LEAD, MAGNALIUM, NICKEL, OBSIDIAN, PERIDOT, PLATINUM, RED_GARNET,
		REDSTONE, REFINED_IRON, RUBY, SAPPHIRE, SILICON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, WOOD,
		YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		Plates() {
			name = this.toString().toLowerCase(Locale.ROOT);
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

	public enum Upgrades implements ItemConvertible {
		OVERCLOCKER((blockEntity, handler, stack) -> {
			PowerAcceptorBlockEntity powerAcceptor = null;
			if (blockEntity instanceof PowerAcceptorBlockEntity) {
				powerAcceptor = (PowerAcceptorBlockEntity) blockEntity;
			}
			handler.addSpeedMulti(TechRebornConfig.overclockerSpeed);
			handler.addPowerMulti(TechRebornConfig.overclockerPower);
			if (powerAcceptor != null) {
				powerAcceptor.extraPowerInput += powerAcceptor.getMaxInput(EnergySide.UNKNOWN);
				powerAcceptor.extraPowerStorage += powerAcceptor.getBaseMaxPower();
			}
		}),
		TRANSFORMER((blockEntity, handler, stack) -> {
			PowerAcceptorBlockEntity powerAcceptor = null;
			if (blockEntity instanceof PowerAcceptorBlockEntity) {
				powerAcceptor = (PowerAcceptorBlockEntity) blockEntity;
			}
			if (powerAcceptor != null) {
				powerAcceptor.extraTier += 1;
			}
		}),
		ENERGY_STORAGE((blockEntity, handler, stack) -> {
			PowerAcceptorBlockEntity powerAcceptor = null;
			if (blockEntity instanceof PowerAcceptorBlockEntity) {
				powerAcceptor = (PowerAcceptorBlockEntity) blockEntity;
			}
			if (powerAcceptor != null) {
				powerAcceptor.extraPowerStorage += TechRebornConfig.energyStoragePower;
			}
		}),
		SUPERCONDUCTOR((blockEntity, handler, stack) -> {
			AdjustableSUBlockEntity aesu = null;
			if (blockEntity instanceof AdjustableSUBlockEntity) {
				aesu = (AdjustableSUBlockEntity) blockEntity;
			}
			if (aesu != null) {
				aesu.superconductors += TechRebornConfig.superConductorCount;
			}
		});

		public String name;
		public Item item;

		Upgrades(IUpgrade upgrade) {
			name = this.toString().toLowerCase(Locale.ROOT);
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
