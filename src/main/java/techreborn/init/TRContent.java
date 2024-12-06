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

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import reborncore.api.blockentity.IUpgrade;
import reborncore.common.fluid.FluidValue;
import reborncore.common.misc.TagConvertible;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.TechReborn;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.generator.LightningRodBlockEntity;
import techreborn.blockentity.generator.PlasmaGeneratorBlockEntity;
import techreborn.blockentity.generator.advanced.*;
import techreborn.blockentity.generator.basic.SolidFuelGeneratorBlockEntity;
import techreborn.blockentity.generator.basic.WaterMillBlockEntity;
import techreborn.blockentity.generator.basic.WindMillBlockEntity;
import techreborn.blockentity.machine.misc.ChargeOMatBlockEntity;
import techreborn.blockentity.machine.misc.DrainBlockEntity;
import techreborn.blockentity.machine.multiblock.*;
import techreborn.blockentity.machine.tier0.block.BlockBreakerBlockEntity;
import techreborn.blockentity.machine.tier0.block.BlockPlacerBlockEntity;
import techreborn.blockentity.machine.tier1.*;
import techreborn.blockentity.machine.tier2.FishingStationBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
import techreborn.blockentity.machine.tier2.MinerBlockEntity;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.blockentity.machine.tier3.IndustrialCentrifugeBlockEntity;
import techreborn.blockentity.machine.tier3.MatterFabricatorBlockEntity;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blocks.GenericMachineBlock;
import techreborn.blocks.cable.CableBlock;
import techreborn.blocks.generator.BlockFusionCoil;
import techreborn.blocks.generator.BlockFusionControlComputer;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.blocks.generator.GenericGeneratorBlock;
import techreborn.blocks.lighting.LampBlock;
import techreborn.blocks.machine.tier0.IronAlloyFurnaceBlock;
import techreborn.blocks.machine.tier0.IronFurnaceBlock;
import techreborn.blocks.machine.tier1.PlayerDetectorBlock;
import techreborn.blocks.machine.tier1.ResinBasinBlock;
import techreborn.blocks.misc.*;
import techreborn.blocks.storage.energy.*;
import techreborn.blocks.storage.fluid.TankUnitBlock;
import techreborn.blocks.storage.item.StorageUnitBlock;
import techreborn.blocks.transformers.BlockEVTransformer;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.config.TechRebornConfig;
import techreborn.entities.EntityNukePrimed;
import techreborn.events.ModRegistry;
import techreborn.items.DynamicCellItem;
import techreborn.items.UpgradeItem;
import techreborn.items.UpgraderItem;
import techreborn.items.armor.NanoSuitItem;
import techreborn.items.armor.QuantumSuitItem;
import techreborn.utils.InitUtils;
import techreborn.world.OreDistribution;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TRContent {
	public static final Marker DATAGEN = MarkerFactory.getMarker("datagen");
	public static final BlockSetType RUBBER_WOOD_SET_TYPE = BlockSetTypeBuilder.copyOf(BlockSetType.OAK).build(Identifier.of(TechReborn.MOD_ID, "rubber_wood"));
	public static final WoodType RUBBER_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.OAK).register(Identifier.of(TechReborn.MOD_ID, "rubber_wood"), RUBBER_WOOD_SET_TYPE);

	// Misc Blocks
	public static Block COMPUTER_CUBE;
	public static Block NUKE;
	public static Block REFINED_IRON_FENCE;
	public static Block REINFORCED_GLASS;
	public static Block RUBBER_LEAVES;
	public static Block RUBBER_LOG;
	public static Block RUBBER_SLAB;
	public static Block RUBBER_STAIR;
	public static Block RUBBER_PLANKS;
	public static Block RUBBER_SAPLING;
	public static Block RUBBER_FENCE;
	public static Block RUBBER_FENCE_GATE;
	public static Block RUBBER_TRAPDOOR;
	public static Block RUBBER_BUTTON;
	public static Block RUBBER_PRESSURE_PLATE;
	public static Block RUBBER_DOOR;
	public static Block RUBBER_LOG_STRIPPED;
	public static Block RUBBER_WOOD;
	public static Block STRIPPED_RUBBER_WOOD;
	public static Block POTTED_RUBBER_SAPLING;
	public static Block COPPER_WALL;

	public static Block MINING_PIPE;

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
	public static Item GPS;
	public static Item SCRAP_BOX;
	public static Item MANUAL;
	public static DynamicCellItem CELL;

	//Quantum Suit
	public static QuantumSuitItem QUANTUM_HELMET;
	public static QuantumSuitItem QUANTUM_CHESTPLATE;
	public static QuantumSuitItem QUANTUM_LEGGINGS;
	public static QuantumSuitItem QUANTUM_BOOTS;

	public static NanoSuitItem NANO_HELMET;
	public static NanoSuitItem NANO_CHESTPLATE;
	public static NanoSuitItem NANO_LEGGINGS;
	public static NanoSuitItem NANO_BOOTS;
	// Gem armor & tools
	public static Item BRONZE_SWORD;
	public static Item BRONZE_PICKAXE;
	public static Item BRONZE_SPADE;
	public static Item BRONZE_AXE;
	public static Item BRONZE_HOE;
	public static Item BRONZE_HELMET;
	public static Item BRONZE_CHESTPLATE;
	public static Item BRONZE_LEGGINGS;
	public static Item BRONZE_BOOTS;
	public static Item RUBY_SWORD;
	public static Item RUBY_PICKAXE;
	public static Item RUBY_SPADE;
	public static Item RUBY_AXE;
	public static Item RUBY_HOE;
	public static Item RUBY_HELMET;
	public static Item RUBY_CHESTPLATE;
	public static Item RUBY_LEGGINGS;
	public static Item RUBY_BOOTS;
	public static Item SAPPHIRE_SWORD;
	public static Item SAPPHIRE_PICKAXE;
	public static Item SAPPHIRE_SPADE;
	public static Item SAPPHIRE_AXE;
	public static Item SAPPHIRE_HOE;
	public static Item SAPPHIRE_HELMET;
	public static Item SAPPHIRE_CHESTPLATE;
	public static Item SAPPHIRE_LEGGINGS;
	public static Item SAPPHIRE_BOOTS;
	public static Item PERIDOT_SWORD;
	public static Item PERIDOT_PICKAXE;
	public static Item PERIDOT_SPADE;
	public static Item PERIDOT_AXE;
	public static Item PERIDOT_HOE;
	public static Item PERIDOT_HELMET;
	public static Item PERIDOT_CHESTPLATE;
	public static Item PERIDOT_LEGGINGS;
	public static Item PERIDOT_BOOTS;
	public static Item SILVER_HELMET;
	public static Item SILVER_CHESTPLATE;
	public static Item SILVER_LEGGINGS;
	public static Item SILVER_BOOTS;
	public static Item STEEL_HELMET;
	public static Item STEEL_CHESTPLATE;
	public static Item STEEL_LEGGINGS;
	public static Item STEEL_BOOTS;

	public final static class BlockTags {
		public static final TagKey<Block> RUBBER_LOGS = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TechReborn.MOD_ID, "rubber_logs"));
		public static final TagKey<Block> OMNI_TOOL_MINEABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TechReborn.MOD_ID, "mineable/omni_tool"));
		public static final TagKey<Block> JACKHAMMER_MINEABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TechReborn.MOD_ID, "mineable/jackhammer"));
		public static final TagKey<Block> DRILL_MINEABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TechReborn.MOD_ID, "mineable/drill"));
		public static final TagKey<Block> NONE_SOLID_COVERS = TagKey.of(RegistryKeys.BLOCK, Identifier.of(TechReborn.MOD_ID, "none_solid_covers"));

		private BlockTags() {
		}
	}

	public final static class ItemTags {
		public static final TagKey<Item> RUBBER_LOGS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "rubber_logs"));
		public static final TagKey<Item> INGOTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "ingots"));
		public static final TagKey<Item> ORES = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "ores"));
		public static final TagKey<Item> STORAGE_BLOCK = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "storage_blocks"));
		public static final TagKey<Item> DUSTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "dusts"));
		public static final TagKey<Item> RAW_METALS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "raw_metals"));
		public static final TagKey<Item> SMALL_DUSTS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "small_dusts"));
		public static final TagKey<Item> GEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "gems"));
		public static final TagKey<Item> NUGGETS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "nuggets"));
		public static final TagKey<Item> PLATES = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "plates"));
		public static final TagKey<Item> STORAGE_UNITS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "storage_units"));
		public static final TagKey<Item> MINER_ACCEPTED_TOOLS = TagKey.of(RegistryKeys.ITEM, Identifier.of(TechReborn.MOD_ID, "miner/accepted_tools"));

		private ItemTags() {
		}
	}

	public enum SolarPanels implements ItemConvertible {
		BASIC(RcEnergyTier.MICRO, TechRebornConfig.basicGenerationRateD, TechRebornConfig.basicGenerationRateN),
		ADVANCED(RcEnergyTier.LOW, TechRebornConfig.advancedGenerationRateD, TechRebornConfig.advancedGenerationRateN),
		INDUSTRIAL(RcEnergyTier.MEDIUM, TechRebornConfig.industrialGenerationRateD, TechRebornConfig.industrialGenerationRateN),
		ULTIMATE(RcEnergyTier.HIGH, TechRebornConfig.ultimateGenerationRateD, TechRebornConfig.ultimateGenerationRateN),
		QUANTUM(RcEnergyTier.EXTREME, TechRebornConfig.quantumGenerationRateD, TechRebornConfig.quantumGenerationRateN),
		CREATIVE(RcEnergyTier.INFINITE, Integer.MAX_VALUE / 100, Integer.MAX_VALUE / 100);

		public final String name;
		public final Block block;

		// Generation of EU during Day
		public final int generationRateD;
		// Generation of EU during Night
		public final int generationRateN;
		// Internal EU storage of solar panel
		public final int internalCapacity;
		public final RcEnergyTier powerTier;

		SolarPanels(RcEnergyTier tier, int generationRateD, int generationRateN) {
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
		BUFFER(1, false),
		CRUDE(TechRebornConfig.crudeStorageUnitMaxStorage, true),
		BASIC(TechRebornConfig.basicStorageUnitMaxStorage, true),
		ADVANCED(TechRebornConfig.advancedStorageUnitMaxStorage, true),
		INDUSTRIAL(TechRebornConfig.industrialStorageUnitMaxStorage, true),
		QUANTUM(TechRebornConfig.quantumStorageUnitMaxStorage, false),
		CREATIVE(Integer.MAX_VALUE, false);

		public final String name;
		public final Block block;
		public final Item upgrader;

		// How many items it can hold
		public final int capacity;


		StorageUnit(int capacity, boolean upgradable) {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new StorageUnitBlock(this);
			this.capacity = capacity;

			if (name.equals("buffer"))
				InitUtils.setup(block, "storage_buffer");
			else
				InitUtils.setup(block, name + "_storage_unit");
			if (upgradable) {
				if (name.equals("buffer"))
					upgrader = InitUtils.setup(new UpgraderItem(), "storage_buffer_upgrader");
				else
					upgrader = InitUtils.setup(new UpgraderItem(), name + "_unit_upgrader");
			}
			else
				upgrader = null;
		}

		public Block asBlock() {
			return block;
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}

		public Optional<Item> getUpgrader() {
			return Optional.ofNullable(upgrader);
		}

		public static Optional<StorageUnit> getUpgradableFor(UpgraderItem item) {
			if (item == null)
				return Optional.empty();
			for (StorageUnit unit : StorageUnit.values()) {
				if (item.equals(unit.getUpgrader().orElse(null)))
					return Optional.of(unit);
			}
			return Optional.empty();
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
		public final FluidValue capacity;


		TankUnit(int capacity) {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new TankUnitBlock(this);
			this.capacity = FluidValue.BUCKET.multiply(capacity);

			InitUtils.setup(block, name + "_tank_unit");
		}

		public Block asBlock() {
			return block;
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}

		public Optional<Item> getUpgrader() {
			try {
				return StorageUnit.valueOf(name()).getUpgrader();
			}
			catch (IllegalArgumentException ex) {
				return Optional.empty();
			}
		}

		public static Optional<TankUnit> getUpgradableFor(UpgraderItem item) {
			if (item == null)
				return Optional.empty();
			for (TankUnit unit : TankUnit.values()) {
				if (item.equals(unit.getUpgrader().orElse(null)))
					return Optional.of(unit);
			}
			return Optional.empty();
		}
	}

	public enum Cables implements ItemConvertible {
		COPPER(128, 12.0, true, RcEnergyTier.MEDIUM),
		TIN(32, 12.0, true, RcEnergyTier.LOW),
		GOLD(512, 12.0, true, RcEnergyTier.HIGH),
		HV(2048, 12.0, true, RcEnergyTier.EXTREME),
		GLASSFIBER(8192, 12.0, false, RcEnergyTier.INSANE),
		INSULATED_COPPER(128, 10.0, false, RcEnergyTier.MEDIUM),
		INSULATED_GOLD(512, 10.0, false, RcEnergyTier.HIGH),
		INSULATED_HV(2048, 10.0, false, RcEnergyTier.EXTREME),
		SUPERCONDUCTOR(Integer.MAX_VALUE / 4, 10.0, false, RcEnergyTier.INFINITE);


		public final String name;
		public final CableBlock block;

		public final int transferRate;
		public final int defaultTransferRate;
		public final double cableThickness;
		public final boolean canKill;
		public final boolean defaultCanKill;
		public final RcEnergyTier tier;


		Cables(int transferRate, double cableThickness, boolean canKill, RcEnergyTier tier) {
			name = this.toString().toLowerCase(Locale.ROOT);
			this.transferRate = transferRate;
			this.defaultTransferRate = transferRate;
			this.cableThickness = cableThickness / 2 / 16;
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



	private final static Map<Ores, Ores> deepslateMap = new HashMap<>();

	private final static Map<Ores, Ores> unDeepslateMap = new HashMap<>();

	public enum Ores implements ItemConvertible, TagConvertible<Item> {
		// when changing ores also change data/minecraft/tags/blocks for correct mining level
		BAUXITE(OreDistribution.BAUXITE),
		CINNABAR(OreDistribution.CINNABAR),
		GALENA(OreDistribution.GALENA),
		IRIDIUM(OreDistribution.IRIDIUM, true),
		LEAD(OreDistribution.LEAD),
		PERIDOT(OreDistribution.PERIDOT),
		PYRITE(OreDistribution.PYRITE),
		RUBY(OreDistribution.RUBY),
		SAPPHIRE(OreDistribution.SAPPHIRE),
		SHELDONITE(OreDistribution.SHELDONITE),
		SILVER(OreDistribution.SILVER),
		SODALITE(OreDistribution.SODALITE),
		SPHALERITE(OreDistribution.SPHALERITE),
		TIN(OreDistribution.TIN),
		TUNGSTEN(OreDistribution.TUNGSTEN, true),

		DEEPSLATE_BAUXITE(BAUXITE),
		DEEPSLATE_GALENA(GALENA),
		DEEPSLATE_IRIDIUM(IRIDIUM),
		DEEPSLATE_LEAD(LEAD),
		DEEPSLATE_PERIDOT(PERIDOT),
		DEEPSLATE_RUBY(RUBY),
		DEEPSLATE_SAPPHIRE(SAPPHIRE),
		DEEPSLATE_SHELDONITE(SHELDONITE),
		DEEPSLATE_SILVER(SILVER),
		DEEPSLATE_SODALITE(SODALITE),
		DEEPSLATE_TIN(TIN),
		DEEPSLATE_TUNGSTEN(TUNGSTEN);

		public final String name;
		public final Block block;
		public final OreDistribution distribution;
		private final boolean industrial;
		private final TagKey<Item> tag;

		Ores(OreDistribution distribution, UniformIntProvider experienceDroppedFallback, boolean industrial) {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new ExperienceDroppingBlock(experienceDroppedFallback != null ? experienceDroppedFallback : ConstantIntProvider.create(1), TRBlockSettings.ore(name.startsWith("deepslate")));
			this.industrial = industrial;
			InitUtils.setup(block, name + "_ore");
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "ores/" +
					(name.startsWith("deepslate_") ? name.substring(name.indexOf('_')+1): name)));
			this.distribution = distribution;
		}

		Ores(OreDistribution distribution, boolean industrial) {
			this(distribution, null, industrial);
		}

		Ores(OreDistribution distribution) {
			this(distribution, false);
		}

		Ores(TRContent.Ores stoneOre) {
			this(null, stoneOre.distribution != null ? stoneOre.distribution.experienceDropped : null, stoneOre.industrial);
			deepslateMap.put(stoneOre, this);
			unDeepslateMap.put(this, stoneOre);
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}

		public boolean isIndustrial() {
			return industrial;
		}

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}

		@Nullable
		public TRContent.Ores getDeepslate() {
			Preconditions.checkArgument(!isDeepslate());
			return deepslateMap.get(this);
		}

		public TRContent.Ores getUnDeepslate() {
			Preconditions.checkArgument(isDeepslate());
			return unDeepslateMap.get(this);
		}

		public boolean isDeepslate() {
			return name.startsWith("deepslate_");
		}
	}

	/**
	 * The base tag name for chrome items. "chromium" is proper spelling, but changing the enum constant names or
	 * directly registry names will result in item loss upon updating. Hence, only the base tag is changed, where needed.
	 */
	public static final String CHROME_TAG_NAME_BASE = "chromium";

	public enum StorageBlocks implements ItemConvertible, TagConvertible<Item> {
		ADVANCED_ALLOY(5f, 6f),
		ALUMINUM(),
		BRASS(),
		BRONZE(5f, 6f),
		CHROME(false, 5f, 6f, CHROME_TAG_NAME_BASE),
		ELECTRUM(),
		HOT_TUNGSTENSTEEL(true, 5f, 6f),
		INVAR(),
		IRIDIUM(5f, 6f),
		IRIDIUM_REINFORCED_STONE(30f, 800f),
		IRIDIUM_REINFORCED_TUNGSTENSTEEL(50f, 1200f),
		LEAD(),
		NICKEL(5f, 6f),
		PERIDOT(5f, 6f),
		PLATINUM(5f, 6f),
		RAW_IRIDIUM(2f, 2f),
		RAW_LEAD(2f, 2f),
		RAW_SILVER(2f, 2f),
		RAW_TIN(2f, 2f),
		RAW_TUNGSTEN(2f, 2f),
		RED_GARNET(5f, 6f),
		REFINED_IRON(5f, 6f),
		RUBY(5f, 6f),
		SAPPHIRE(5f, 6f),
		SILVER(5f, 6f),
		STEEL(5f, 6f),
		TIN(),
		TITANIUM(5f, 6f),
		TUNGSTEN(5f, 6f),
		TUNGSTENSTEEL(30f, 800f),
		YELLOW_GARNET(5f, 6f),
		ZINC(5f, 6f);

		private final String name;
		private final Block block;
		private final StairsBlock stairsBlock;
		private final SlabBlock slabBlock;
		private final WallBlock wallBlock;
		private final TagKey<Item> tag;

		StorageBlocks(boolean isHot, float hardness, float resistance, String tagNameBase) {
			name = this.toString().toLowerCase(Locale.ROOT);
			block = new BlockStorage(isHot, hardness, resistance);
			InitUtils.setup(block, name + "_storage_block");
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "storage_blocks/" + Objects.requireNonNullElse(tagNameBase, name)));

			stairsBlock = new TechRebornStairsBlock(block.getDefaultState(), AbstractBlock.Settings.copy(block));
			InitUtils.setup(stairsBlock, name + "_storage_block_stairs");

			slabBlock = new SlabBlock(AbstractBlock.Settings.copy(block));
			InitUtils.setup(slabBlock, name + "_storage_block_slab");

			wallBlock = new WallBlock(AbstractBlock.Settings.copy(block));
			InitUtils.setup(wallBlock, name + "_storage_block_wall");
		}

		StorageBlocks(boolean isHot, float hardness, float resistance) {
			this(isHot, hardness, resistance, null);
		}

		StorageBlocks(float hardness, float resistance) {
			this(false, hardness, resistance, null);
		}

		StorageBlocks() {
			this(false, 3f, 6f);
		}

		@Override
		public Item asItem() {
			return block.asItem();
		}

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}

		public Block getBlock() {
			return block;
		}

		public StairsBlock getStairsBlock() {
			return stairsBlock;
		}

		public SlabBlock getSlabBlock() {
			return slabBlock;
		}

		public WallBlock getWallBlock() {
			return wallBlock;
		}

		public static Stream<Block> blockStream() {
			return Arrays.stream(values())
					.map(StorageBlocks::allBlocks)
					.flatMap(Collection::stream);
		}

		private List<Block> allBlocks() {
			return List.of(block, stairsBlock, slabBlock, wallBlock);
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

		public static ItemConvertible[] getCasings() {
			return Arrays.stream(MachineBlocks.values())
					.map((Function<MachineBlocks, ItemConvertible>) machineBlocks -> machineBlocks.casing)
					.toArray(ItemConvertible[]::new);
		}
	}


	public enum Machine implements ItemConvertible {
		ALLOY_SMELTER(new GenericMachineBlock(GuiType.ALLOY_SMELTER, AlloySmelterBlockEntity::new)),
		ASSEMBLY_MACHINE(new GenericMachineBlock(GuiType.ASSEMBLING_MACHINE, AssemblingMachineBlockEntity::new)),
		AUTO_CRAFTING_TABLE(new GenericMachineBlock(GuiType.AUTO_CRAFTING_TABLE, AutoCraftingTableBlockEntity::new)),
		CHEMICAL_REACTOR(new GenericMachineBlock(GuiType.CHEMICAL_REACTOR, ChemicalReactorBlockEntity::new)),
		COMPRESSOR(new GenericMachineBlock(GuiType.COMPRESSOR, CompressorBlockEntity::new)),
		DISTILLATION_TOWER(new GenericMachineBlock(GuiType.DISTILLATION_TOWER, DistillationTowerBlockEntity::new)),
		EXTRACTOR(new GenericMachineBlock(GuiType.EXTRACTOR, ExtractorBlockEntity::new)),
		RESIN_BASIN(new ResinBasinBlock(ResinBasinBlockEntity::new)),
		FLUID_REPLICATOR(new GenericMachineBlock(GuiType.FLUID_REPLICATOR, FluidReplicatorBlockEntity::new)),
		GRINDER(new GenericMachineBlock(GuiType.GRINDER, GrinderBlockEntity::new)),
		ELECTRIC_FURNACE(new GenericMachineBlock(GuiType.ELECTRIC_FURNACE, ElectricFurnaceBlockEntity::new)),
		IMPLOSION_COMPRESSOR(new GenericMachineBlock(GuiType.IMPLOSION_COMPRESSOR, ImplosionCompressorBlockEntity::new)),
		INDUSTRIAL_BLAST_FURNACE(new GenericMachineBlock(GuiType.BLAST_FURNACE, IndustrialBlastFurnaceBlockEntity::new)),
		INDUSTRIAL_CENTRIFUGE(new GenericMachineBlock(GuiType.CENTRIFUGE, IndustrialCentrifugeBlockEntity::new)),
		INDUSTRIAL_ELECTROLYZER(new GenericMachineBlock(GuiType.INDUSTRIAL_ELECTROLYZER, IndustrialElectrolyzerBlockEntity::new)),
		INDUSTRIAL_GRINDER(new GenericMachineBlock(GuiType.INDUSTRIAL_GRINDER, IndustrialGrinderBlockEntity::new)),
		INDUSTRIAL_SAWMILL(new GenericMachineBlock(GuiType.SAWMILL, IndustrialSawmillBlockEntity::new)),
		IRON_ALLOY_FURNACE(new IronAlloyFurnaceBlock()),
		IRON_FURNACE(new IronFurnaceBlock()),
		MATTER_FABRICATOR(new GenericMachineBlock(GuiType.MATTER_FABRICATOR, MatterFabricatorBlockEntity::new)),
		RECYCLER(new GenericMachineBlock(GuiType.RECYCLER, RecyclerBlockEntity::new)),
		ROLLING_MACHINE(new GenericMachineBlock(GuiType.ROLLING_MACHINE, RollingMachineBlockEntity::new)),
		SCRAPBOXINATOR(new GenericMachineBlock(GuiType.SCRAPBOXINATOR, ScrapboxinatorBlockEntity::new)),
		VACUUM_FREEZER(new GenericMachineBlock(GuiType.VACUUM_FREEZER, VacuumFreezerBlockEntity::new)),
		SOLID_CANNING_MACHINE(new GenericMachineBlock(GuiType.SOLID_CANNING_MACHINE, SolidCanningMachineBlockEntity::new)),
		WIRE_MILL(new GenericMachineBlock(GuiType.WIRE_MILL, WireMillBlockEntity::new)),
		GREENHOUSE_CONTROLLER(new GenericMachineBlock(GuiType.GREENHOUSE_CONTROLLER, GreenhouseControllerBlockEntity::new)),
		BLOCK_BREAKER(new GenericMachineBlock(GuiType.BLOCK_BREAKER, BlockBreakerBlockEntity::new)),
		BLOCK_PLACER(new GenericMachineBlock(GuiType.BLOCK_PLACER, BlockPlacerBlockEntity::new)),
		LAUNCHPAD(new GenericMachineBlock(GuiType.LAUNCHPAD, LaunchpadBlockEntity::new)),
		ELEVATOR(new GenericMachineBlock(GuiType.ELEVATOR, ElevatorBlockEntity::new)),
		FISHING_STATION(new GenericMachineBlock(GuiType.FISHING_STATION, FishingStationBlockEntity::new)),

		DIESEL_GENERATOR(new GenericGeneratorBlock(GuiType.DIESEL_GENERATOR, DieselGeneratorBlockEntity::new)),
		DRAGON_EGG_SYPHON(new GenericGeneratorBlock(null, DragonEggSyphonBlockEntity::new)),
		FUSION_COIL(new BlockFusionCoil()),
		FUSION_CONTROL_COMPUTER(new BlockFusionControlComputer()),
		GAS_TURBINE(new GenericGeneratorBlock(GuiType.GAS_TURBINE, GasTurbineBlockEntity::new)),
		LIGHTNING_ROD(new GenericGeneratorBlock(null, LightningRodBlockEntity::new)),
		PLASMA_GENERATOR(new GenericGeneratorBlock(GuiType.PLASMA_GENERATOR, PlasmaGeneratorBlockEntity::new)),
		SEMI_FLUID_GENERATOR(new GenericGeneratorBlock(GuiType.SEMIFLUID_GENERATOR, SemiFluidGeneratorBlockEntity::new)),
		SOLID_FUEL_GENERATOR(new GenericGeneratorBlock(GuiType.GENERATOR, SolidFuelGeneratorBlockEntity::new)),
		THERMAL_GENERATOR(new GenericGeneratorBlock(GuiType.THERMAL_GENERATOR, ThermalGeneratorBlockEntity::new)),
		WATER_MILL(new GenericGeneratorBlock(null, WaterMillBlockEntity::new)),
		WIND_MILL(new GenericGeneratorBlock(null, WindMillBlockEntity::new)),

		DRAIN(new GenericMachineBlock(null, DrainBlockEntity::new)),
		PUMP(new GenericMachineBlock(GuiType.PUMP, PumpBlockEntity::new)),
		MINER(new GenericMachineBlock(GuiType.MINER, MinerBlockEntity::new)),
		ADJUSTABLE_SU(new AdjustableSUBlock()),
		CHARGE_O_MAT(new GenericMachineBlock(GuiType.CHARGEBENCH, ChargeOMatBlockEntity::new)),
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
		CHUNK_LOADER(new GenericMachineBlock(GuiType.CHUNK_LOADER, ChunkLoaderBlockEntity::new)),
		LAMP_INCANDESCENT(new LampBlock(4, 10, 8)),
		LAMP_LED(new LampBlock(1, 1, 12)),
		PLAYER_DETECTOR(new PlayerDetectorBlock());

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

	public enum Dusts implements ItemConvertible, TagConvertible<Item> {
		ALMANDINE, ALUMINUM, AMETHYST, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, BRASS, BRONZE, CALCITE, CHARCOAL, CHROME(CHROME_TAG_NAME_BASE),
		CINNABAR, CLAY, COAL, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GRANITE, GROSSULAR, INVAR, LAZURITE, MAGNESIUM, MANGANESE, MARBLE, NETHERRACK,
		NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, QUARTZ, RED_GARNET, RUBY, SALTPETER,
		SAPPHIRE, SAW, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TITANIUM, UVAROVITE, YELLOW_GARNET, ZINC;

		private final String name;
		private final Item item;
		private final TagKey<Item> tag;

		Dusts(String tagNameBase) {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
			InitUtils.setup(item, name + "_dust");
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "dusts/" + Objects.requireNonNullElse(tagNameBase, name)));
		}

		Dusts() {
			this(null);
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

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}
	}

	public enum RawMetals implements ItemConvertible, TagConvertible<Item> {
		IRIDIUM, LEAD, SILVER, TIN, TUNGSTEN;

		private final String name;
		private final Item item;
		private final Ores ore;
		private final StorageBlocks storageBlock;
		private final TagKey<Item> tag;

		RawMetals() {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
			Ores oreVariant = null;
			try {
				oreVariant = Ores.valueOf(this.toString());
			}
			catch (IllegalArgumentException ex) {
				if (InitUtils.isDatagenRunning())
					TechReborn.LOGGER.warn(DATAGEN, "Raw metal {} has no ore block equivalent!", name);
			}
			ore = oreVariant;
			StorageBlocks blockVariant = null;
			try {
				blockVariant = StorageBlocks.valueOf("RAW_" + this.toString());
			}
			catch (IllegalArgumentException ex) {
				if (InitUtils.isDatagenRunning())
					TechReborn.LOGGER.warn(DATAGEN, "Raw metal {} has no storage block equivalent!", name);
			}
			storageBlock = blockVariant;
			InitUtils.setup(item, "raw_" + name);
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "raw_materials/" + name));
		}

		@Override
		public Item asItem() {
			return item;
		}

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}

		public StorageBlocks getStorageBlock() {
			return storageBlock;
		}

		public Ores getOre() {
			return ore;
		}

		/**
		 * Returns a map that maps the raw metals to their storage block equivalent.
		 * @return A non {@code null} map mapping the raw metals to their storage block equivalent.
		 * If a storage block equivalent doesn't exist, the raw metal will not be in the keys of this map.
		 */
		public static @NotNull Map<RawMetals, StorageBlocks> getRM2SBMap() {
			return Arrays.stream(values())
					.map(rawMetal -> new Pair<>(rawMetal, rawMetal.getStorageBlock()))
					.filter(entry -> entry.getRight() != null) // ensure storage block equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}

		/**
		 * Returns a map that maps the raw metals to their ore block equivalent.
		 * @return A non {@code null} map mapping the raw metals to their ore block equivalent.
		 * If an ore block equivalent doesn't exist, the raw metal will not be in the keys of this map.
		 */
		public static @NotNull Map<RawMetals, Ores> getRM2OBMap() {
			return Arrays.stream(values())
					.map(rawMetal -> new Pair<>(rawMetal, rawMetal.getOre()))
					.filter(entry -> entry.getRight() != null) // ensure ore block equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}
	}

	public enum SmallDusts implements ItemConvertible, TagConvertible<Item> {
		ALMANDINE, ANDESITE, ANDRADITE, ASHES, BASALT, BAUXITE, CALCITE, CHARCOAL, CHROME(CHROME_TAG_NAME_BASE),
		CINNABAR, CLAY, COAL, DARK_ASHES, DIAMOND, DIORITE, ELECTRUM, EMERALD, ENDER_EYE, ENDER_PEARL, ENDSTONE,
		FLINT, GALENA, GLOWSTONE(Items.GLOWSTONE_DUST), GRANITE, GROSSULAR, INVAR, LAZURITE, MAGNESIUM, MANGANESE, MARBLE,
		NETHERRACK, NICKEL, OBSIDIAN, OLIVINE, PERIDOT, PHOSPHOROUS, PLATINUM, PYRITE, PYROPE, QUARTZ, REDSTONE(Items.REDSTONE),
		RED_GARNET, RUBY, SALTPETER, SAPPHIRE, SAW, SODALITE, SPESSARTINE, SPHALERITE, STEEL, SULFUR, TITANIUM,
		TUNGSTEN(RawMetals.TUNGSTEN), UVAROVITE, YELLOW_GARNET, ZINC;

		private final String name;
		private final Item item;
		private final ItemConvertible dust;
		private final TagKey<Item> tag;

		SmallDusts(String tagNameBase, ItemConvertible dustVariant) {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
			if (dustVariant == null)
				try {
					dustVariant = Dusts.valueOf(this.toString());
				}
				catch (IllegalArgumentException ex) {
					if (InitUtils.isDatagenRunning())
						TechReborn.LOGGER.warn(DATAGEN, "Small dust {} has no dust equivalent!", name);
				}
			dust = dustVariant;
			InitUtils.setup(item, name + "_small_dust");
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "small_dusts/" + Objects.requireNonNullElse(tagNameBase, name)));
		}

		SmallDusts(String tagNameBase) {
			this(tagNameBase, null);
		}

		SmallDusts(ItemConvertible dustVariant) {
			this(null, dustVariant);
		}

		SmallDusts() {
			this(null, null);
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

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}

		public ItemConvertible getDust() {
			return dust;
		}

		/**
		 * Returns a map that maps the small dusts to their dust equivalent,
		 * as it was specified in the enum. Note that the dust equivalent
		 * doesn't have to be a TR dust (see redstone and glowstone dust)
		 * and also not a dust at all (see tungsten).
		 * @return A non {@code null} map mapping the small dusts to their dust equivalent.
		 * If a dust equivalent doesn't exist, the small dust will not be in the keys of this map.
		 */
		public static @NotNull Map<SmallDusts, ItemConvertible> getSD2DMap() {
			return Arrays.stream(values())
					.map(smallDust -> new Pair<>(smallDust, smallDust.getDust()))
					.filter(entry -> entry.getRight() != null) // ensure dust equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}
	}

	public enum Gems implements ItemConvertible, TagConvertible<Item> {
		PERIDOT, RED_GARNET, RUBY, SAPPHIRE, YELLOW_GARNET;

		private final String name;
		private final Item item;
		private final Dusts dust;
		private final Ores ore;
		private final StorageBlocks storageBlock;
		private final TagKey<Item> tag;

		Gems() {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
			Dusts dustVariant = null;
			try {
				dustVariant = Dusts.valueOf(this.toString());
			}
			catch (IllegalArgumentException ex) {
				if (InitUtils.isDatagenRunning())
					TechReborn.LOGGER.warn(DATAGEN, "Gem {} has no dust item equivalent!", name);
			}
			dust = dustVariant;
			Ores oreVariant = null;
			try {
				oreVariant = Ores.valueOf(this.toString());
			}
			catch (IllegalArgumentException ex) {
				if (InitUtils.isDatagenRunning())
					TechReborn.LOGGER.info(DATAGEN, "Gem {} has no ore block equivalent.", name);
			}
			ore = oreVariant;
			StorageBlocks blockVariant = null;
			try {
				blockVariant = StorageBlocks.valueOf(this.toString());
			}
			catch (IllegalArgumentException ex) {
				TechReborn.LOGGER.warn(DATAGEN, "Gem {} has no storage block equivalent!", name);
			}
			storageBlock = blockVariant;
			InitUtils.setup(item, name + "_gem");
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "gems/" + name));
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

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}

		public Dusts getDust() {
			return dust;
		}

		public Ores getOre() {
			return ore;
		}

		public StorageBlocks getStorageBlock() {
			return storageBlock;
		}

		/**
		 * Returns a map that maps the gems to their dust item equivalent.
		 * @return A non {@code null} map mapping the gems to their dust item equivalent.
		 * If a dust item equivalent doesn't exist, the gem will not be in the keys of this map.
		 */
		public static @NotNull Map<Gems, Dusts> getG2DMap() {
			return Arrays.stream(values())
					.map(gem -> new Pair<>(gem, gem.getDust()))
					.filter(entry -> entry.getRight() != null) // ensure dust item equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}

		/**
		 * Returns a map that maps the gems to their storage block equivalent.
		 * @return A non {@code null} map mapping the gems to their storage block equivalent.
		 * If a storage block equivalent doesn't exist, the gem will not be in the keys of this map.
		 */
		public static @NotNull Map<Gems, StorageBlocks> getG2SBMap() {
			return Arrays.stream(values())
					.map(gem -> new Pair<>(gem, gem.getStorageBlock()))
					.filter(entry -> entry.getRight() != null) // ensure storage block equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}
	}


	public enum Ingots implements ItemConvertible, TagConvertible<Item> {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CHROME(CHROME_TAG_NAME_BASE), ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM_ALLOY, IRIDIUM,
		LEAD, MIXED_METAL, NICKEL, PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		private final String name;
		private final Item item;
		private final Dusts dust;
		private final StorageBlocks storageBlock;
		private final TagKey<Item> tag;

		Ingots(String tagNameBase) {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
			Dusts dustVariant = null;
			try {
				dustVariant = Dusts.valueOf(this.toString());
			}
			catch (IllegalArgumentException ex) {
				try {
					RawMetals.valueOf(this.toString());
					if (InitUtils.isDatagenRunning())
						TechReborn.LOGGER.info(DATAGEN, "Ingot {} has no dust item equivalent, but a raw metal.", name);
				}
				catch (IllegalArgumentException ex2) {
					if (InitUtils.isDatagenRunning())
						TechReborn.LOGGER.warn(DATAGEN, "Ingot {} has no dust item equivalent AND no raw metal!", name);
				}
			}
			dust = dustVariant;
			StorageBlocks blockVariant = null;
			try {
				blockVariant = StorageBlocks.valueOf(this.toString());
			}
			catch (IllegalArgumentException ex) {
				if (InitUtils.isDatagenRunning())
					TechReborn.LOGGER.warn(DATAGEN, "Ingot {} has no storage block equivalent!", name);
			}
			storageBlock = blockVariant;
			InitUtils.setup(item, name + "_ingot");
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "ingots/" + Objects.requireNonNullElse(tagNameBase, name)));
		}

		Ingots() {
			this(null);
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

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}

		public Dusts getDust() {
			return dust;
		}

		public StorageBlocks getStorageBlock() {
			return storageBlock;
		}

		/**
		 * Returns a map that maps the ingots to their dust item equivalent.
		 * @return A non {@code null} map mapping the ingots to their dust item equivalent.
		 * If a dust item equivalent doesn't exist, the ingot will not be in the keys of this map.
		 */
		public static @NotNull Map<Ingots, Dusts> getI2DMap() {
			return Arrays.stream(values())
					.map(gem -> new Pair<>(gem, gem.getDust()))
					.filter(entry -> entry.getRight() != null) // ensure dust item equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}

		/**
		 * Returns a map that maps the ingots to their storage block equivalent.
		 * @return A non {@code null} map mapping the ingots to their storage block equivalent.
		 * If a storage block equivalent doesn't exist, the raw metal will not be in the keys of this map.
		 */
		public static @NotNull Map<Ingots, ItemConvertible> getI2SBMap() {
			return Arrays.stream(values())
					.map(ingot -> new Pair<>(ingot, ingot.getStorageBlock()))
					.filter(entry -> entry.getRight() != null) // ensure storage block equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
		}
	}

	public enum Nuggets implements ItemConvertible, TagConvertible<Item> {
		ALUMINUM, BRASS, BRONZE, CHROME(CHROME_TAG_NAME_BASE), COPPER(Items.COPPER_INGOT, false), DIAMOND(Items.DIAMOND, true),
		ELECTRUM, EMERALD(Items.EMERALD, true), HOT_TUNGSTENSTEEL, INVAR, IRIDIUM, LEAD,
		NETHERITE, /* We do NOT link to the netherite ingot here, because we want custom conversion recipes! */
		NICKEL, PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		private final String name;
		private final Item item;
		private final ItemConvertible ingot;
		private final boolean ofGem;
		private final TagKey<Item> tag;

		Nuggets(String tagNameBase, ItemConvertible ingotVariant, boolean ofGem) {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
			if (ingotVariant == null)
				try {
					ingotVariant = Ingots.valueOf(this.toString());
				}
				catch (IllegalArgumentException ex) {
					if (InitUtils.isDatagenRunning())
						TechReborn.LOGGER.warn(DATAGEN, "Nugget {} has no ingot equivalent!", name);
				}
			ingot = ingotVariant;
			this.ofGem = ofGem;
			InitUtils.setup(item, name + "_nugget");
			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "nuggets/" + Objects.requireNonNullElse(tagNameBase, name)));
		}

		Nuggets(ItemConvertible ingotVariant, boolean ofGem) {
			this(null, ingotVariant, ofGem);
		}

		Nuggets(String tagNameBase) {
			this(tagNameBase, null, false);
		}

		Nuggets() {
			this(null, false);
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

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}

		public ItemConvertible getIngot() {
			return ingot;
		}

		public boolean isOfGem() {
			return false;
		}

		/**
		 * Returns a map that maps the nuggets to their ingot equivalent,
		 * as it was specified in the enum. Note that the ingot equivalent
		 * doesn't have to be an ingot at all (see emerald and diamond).
		 * @return A non {@code null} map mapping the nuggets to their ingot equivalent.
		 * If an ingot equivalent doesn't exist, the raw metal will not be in the keys of this map.
		 */
		public static @NotNull Map<Nuggets, ItemConvertible> getN2IMap() {
			return Arrays.stream(values())
					.map(nugget -> new Pair<>(nugget, nugget.getIngot()))
					.filter(entry -> entry.getRight() != null) // ensure ingot equivalent exists
					.collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
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
		IRIDIUM_NEUTRON_REFLECTOR,

		//java vars can't start with numbers, so these get suffixes
		WATER_COOLANT_CELL_10K,
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
		COMPRESSED_PLANTBALL,
		SPONGE_PIECE,
		TEMPLATE_TEMPLATE,

		SYNTHETIC_REDSTONE_CRYSTAL;

		public final String name;
		public final Item item;

		Parts() {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
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

	public enum Plates implements ItemConvertible, TagConvertible<Item> {
		ADVANCED_ALLOY,
		ALUMINUM,
		BRASS,
		BRONZE,
		CARBON(Parts.CARBON_MESH),
		CHROME(CHROME_TAG_NAME_BASE),
		COAL(Dusts.COAL, Items.COAL_BLOCK),
		COPPER(Items.COPPER_INGOT, Items.COPPER_BLOCK),
		DIAMOND(Dusts.DIAMOND, Items.DIAMOND_BLOCK),
		ELECTRUM,
		EMERALD(Dusts.EMERALD, Items.EMERALD_BLOCK),
		GOLD(Items.GOLD_INGOT, Items.GOLD_BLOCK),
		INVAR,
		IRIDIUM_ALLOY(true),
		IRIDIUM,
		IRON(Items.IRON_INGOT, Items.IRON_BLOCK),
		LAPIS(Items.LAPIS_BLOCK),
		LAZURITE(Dusts.LAZURITE),
		LEAD,
		MAGNALIUM,
		NICKEL,
		OBSIDIAN(Dusts.OBSIDIAN, Items.OBSIDIAN),
		PERIDOT,
		PLATINUM,
		QUARTZ(Dusts.QUARTZ),
		RED_GARNET,
		REDSTONE(Items.REDSTONE_BLOCK),
		REFINED_IRON,
		RUBY,
		SAPPHIRE,
		SILICON,
		SILVER,
		STEEL,
		TIN,
		TITANIUM,
		TUNGSTEN,
		TUNGSTENSTEEL,
		WOOD,
		YELLOW_GARNET,
		ZINC;

		private final String name;
		private final Item item;
		private final ItemConvertible source;
		private final ItemConvertible sourceBlock;
		private final boolean industrial;
		private final TagKey<Item> tag;

		Plates(ItemConvertible source, ItemConvertible sourceBlock, boolean industrial, String tagNameBase) {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new Item(new Item.Settings());
			ItemConvertible sourceVariant = null;
			if (source != null) {
				sourceVariant = source;
			}
			else {
				try {
					sourceVariant = Ingots.valueOf(this.toString());
				}
				catch (IllegalArgumentException ex) {
					try {
						sourceVariant = Gems.valueOf(this.toString());
					}
					catch (IllegalArgumentException ex2) {
						if (InitUtils.isDatagenRunning())
							TechReborn.LOGGER.warn(DATAGEN, "Plate {} has no identifiable source!", name);
					}
				}
			}
			if (sourceBlock != null) {
				this.sourceBlock = sourceBlock;
			}
			else {
				if (sourceVariant instanceof Gems gem)
					this.sourceBlock = gem.getStorageBlock();
				else if (sourceVariant instanceof Ingots ingot)
					this.sourceBlock = ingot.getStorageBlock();
				else {
					if (InitUtils.isDatagenRunning())
						TechReborn.LOGGER.info(DATAGEN, "Plate {} has no identifiable source block.", name);
					this.sourceBlock = null;
				}
			}
			if (sourceVariant instanceof Gems gem)
				this.source = gem.getDust();
			else
				this.source = sourceVariant;
			this.industrial = industrial;
			InitUtils.setup(item, name + "_plate");

			if (tagNameBase == null) {
				tagNameBase = name;
			}

			tag = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "plates/" + tagNameBase));
		}

		Plates(String tagNameBase) {
			this(null, null, false, tagNameBase);
		}

		Plates(ItemConvertible source, ItemConvertible sourceBlock) {
			this(source, sourceBlock, false, null);
		}

		Plates(ItemConvertible source) {
			this(source, null, false, null);
		}

		Plates(boolean industrial) {
			this(null, null, industrial, null);
		}

		Plates() {
			this(null, null, false, null);
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

		public ItemConvertible getSource() {
			return source;
		}

		public ItemConvertible getSourceBlock() {
			return sourceBlock;
		}

		public boolean isIndustrial() {
			return industrial;
		}

		@Override
		public TagKey<Item> asTag() {
			return tag;
		}
	}

	public enum Upgrades implements ItemConvertible {
		OVERCLOCKER((blockEntity, handler, stack) -> {
			PowerAcceptorBlockEntity powerAcceptor = null;
			if (blockEntity instanceof PowerAcceptorBlockEntity) {
				powerAcceptor = (PowerAcceptorBlockEntity) blockEntity;
			}
			if (handler != null) {
				handler.addSpeedMultiplier(TechRebornConfig.overclockerSpeed);
				handler.addPowerMultiplier(TechRebornConfig.overclockerPower);
			}
			if (powerAcceptor != null) {
				powerAcceptor.extraPowerInput += powerAcceptor.getMaxInput(null);
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
		}),
		MUFFLER((blockEntity, handler, stack) -> {
			blockEntity.muffle();
		});

		public final String name;
		public final Item item;

		Upgrades(IUpgrade upgrade) {
			name = this.toString().toLowerCase(Locale.ROOT);
			item = new UpgradeItem(name, upgrade);
			InitUtils.setup(item, name + "_upgrade");
		}

		@Override
		public Item asItem() {
			return item;
		}
	}

	public static final EntityType<EntityNukePrimed> ENTITY_NUKE = FabricEntityTypeBuilder.create()
		.entityFactory((EntityType.EntityFactory<EntityNukePrimed>) EntityNukePrimed::new)
		.dimensions(EntityDimensions.fixed(1f, 1f))
		.trackRangeChunks(10)
		.build();

	static {
		ModRegistry.register();
		TRItemGroup.register();

		Registry.register(Registries.ENTITY_TYPE, Identifier.of(TechReborn.MOD_ID, "nuke"), ENTITY_NUKE);
	}
}
