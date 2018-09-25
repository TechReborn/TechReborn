package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.RebornRegistry;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IUpgrade;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.BucketHandler;
import techreborn.TechReborn;
import techreborn.api.Reference;
import techreborn.blocks.BlockAlarm;
import techreborn.blocks.BlockComputerCube;
import techreborn.blocks.BlockMachineCasing;
import techreborn.blocks.BlockMachineFrame;
import techreborn.blocks.BlockNuke;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockRefinedIronFence;
import techreborn.blocks.BlockReinforcedGlass;
import techreborn.blocks.BlockRubberLeaves;
import techreborn.blocks.BlockRubberLog;
import techreborn.blocks.BlockRubberPlank;
import techreborn.blocks.BlockRubberPlankSlab;
import techreborn.blocks.BlockRubberPlankStair;
import techreborn.blocks.BlockRubberSapling;
import techreborn.blocks.BlockStorage;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.generator.BlockDieselGenerator;
import techreborn.blocks.generator.BlockDragonEggSyphon;
import techreborn.blocks.generator.BlockFusionCoil;
import techreborn.blocks.generator.BlockFusionControlComputer;
import techreborn.blocks.generator.BlockGasTurbine;
import techreborn.blocks.generator.BlockLightningRod;
import techreborn.blocks.generator.BlockMagicEnergyAbsorber;
import techreborn.blocks.generator.BlockMagicEnergyConverter;
import techreborn.blocks.generator.BlockPlasmaGenerator;
import techreborn.blocks.generator.BlockSemiFluidGenerator;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.blocks.generator.BlockSolidFuelGenerator;
import techreborn.blocks.generator.BlockThermalGenerator;
import techreborn.blocks.generator.BlockWaterMill;
import techreborn.blocks.generator.BlockWindMill;
import techreborn.blocks.lighting.BlockLamp;
import techreborn.blocks.storage.BlockAdjustableSU;
import techreborn.blocks.storage.BlockHighVoltageSU;
import techreborn.blocks.storage.BlockInterdimensionalSU;
import techreborn.blocks.storage.BlockLSUStorage;
import techreborn.blocks.storage.BlockLapotronicSU;
import techreborn.blocks.storage.BlockLowVoltageSU;
import techreborn.blocks.storage.BlockMediumVoltageSU;
import techreborn.blocks.tier0.BlockIronAlloyFurnace;
import techreborn.blocks.tier0.BlockIronFurnace;
import techreborn.blocks.tier1.BlockAlloySmelter;
import techreborn.blocks.tier1.BlockAssemblingMachine;
import techreborn.blocks.tier1.BlockAutoCraftingTable;
import techreborn.blocks.tier1.BlockCompressor;
import techreborn.blocks.tier1.BlockElectricFurnace;
import techreborn.blocks.tier1.BlockExtractor;
import techreborn.blocks.tier1.BlockGrinder;
import techreborn.blocks.tier1.BlockPlayerDetector;
import techreborn.blocks.tier1.BlockRecycler;
import techreborn.blocks.tier1.BlockRollingMachine;
import techreborn.blocks.tier1.BlockScrapboxinator;
import techreborn.blocks.tier2.BlockChargeOMat;
import techreborn.blocks.tier2.BlockChemicalReactor;
import techreborn.blocks.tier2.BlockDigitalChest;
import techreborn.blocks.tier2.BlockDistillationTower;
import techreborn.blocks.tier2.BlockImplosionCompressor;
import techreborn.blocks.tier2.BlockIndustrialBlastFurnace;
import techreborn.blocks.tier2.BlockIndustrialCentrifuge;
import techreborn.blocks.tier2.BlockIndustrialElectrolyzer;
import techreborn.blocks.tier2.BlockIndustrialGrinder;
import techreborn.blocks.tier2.BlockIndustrialSawmill;
import techreborn.blocks.tier2.BlockVacuumFreezer;
import techreborn.blocks.tier3.BlockChunkLoader;
import techreborn.blocks.tier3.BlockCreativeQuantumChest;
import techreborn.blocks.tier3.BlockCreativeQuantumTank;
import techreborn.blocks.tier3.BlockFluidReplicator;
import techreborn.blocks.tier3.BlockMatterFabricator;
import techreborn.blocks.tier3.BlockQuantumChest;
import techreborn.blocks.tier3.BlockQuantumTank;
import techreborn.blocks.transformers.BlockHVTransformer;
import techreborn.blocks.transformers.BlockLVTransformer;
import techreborn.blocks.transformers.BlockMVTransformer;
import techreborn.config.ConfigTechReborn;
import techreborn.itemblocks.ItemBlockAdjustableSU;
import techreborn.itemblocks.ItemBlockDigitalChest;
import techreborn.itemblocks.ItemBlockQuantumChest;
import techreborn.itemblocks.ItemBlockQuantumTank;
import techreborn.itemblocks.ItemBlockRubberSapling;
import techreborn.items.DynamicCell;
import techreborn.items.ItemFrequencyTransmitter;
import techreborn.items.ItemManual;
import techreborn.items.ItemScrapBox;
import techreborn.items.ItemUpgrade;
import techreborn.items.armor.ItemCloakingDevice;
import techreborn.items.armor.ItemLapotronicOrbpack;
import techreborn.items.armor.ItemLithiumIonBatpack;
import techreborn.items.armor.ItemTRArmour;
import techreborn.items.battery.ItemEnergyCrystal;
import techreborn.items.battery.ItemLapotronCrystal;
import techreborn.items.battery.ItemLapotronicOrb;
import techreborn.items.battery.ItemLithiumIonBattery;
import techreborn.items.battery.ItemRedCellBattery;
import techreborn.items.tool.ItemDebugTool;
import techreborn.items.tool.ItemTreeTap;
import techreborn.items.tool.ItemWrench;
import techreborn.items.tool.advanced.ItemAdvancedChainsaw;
import techreborn.items.tool.advanced.ItemAdvancedDrill;
import techreborn.items.tool.advanced.ItemAdvancedJackhammer;
import techreborn.items.tool.advanced.ItemRockCutter;
import techreborn.items.tool.basic.ItemBasicChainsaw;
import techreborn.items.tool.basic.ItemBasicDrill;
import techreborn.items.tool.basic.ItemBasicJackhammer;
import techreborn.items.tool.basic.ItemElectricTreetap;
import techreborn.items.tool.industrial.ItemIndustrialChainsaw;
import techreborn.items.tool.industrial.ItemIndustrialDrill;
import techreborn.items.tool.industrial.ItemIndustrialJackhammer;
import techreborn.items.tool.industrial.ItemNanosaber;
import techreborn.items.tool.industrial.ItemOmniTool;
import techreborn.items.tool.vanilla.ItemTRAxe;
import techreborn.items.tool.vanilla.ItemTRHoe;
import techreborn.items.tool.vanilla.ItemTRSpade;
import techreborn.items.tool.vanilla.ItemTRSword;
import techreborn.utils.InitUtils;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

@RebornRegister(modID = TechReborn.MOD_ID)
public class TRContent {
	
	// Misc Blocks
	public static Block COMPUTER_CUBE;
	public static Block FLARE;
	public static Block NUKE;
	public static Block REFINED_IRON_FENCE;
	public static Block REINFORCED_GLASS;
	public static Block RUBBER_LEAVES;
	public static Block RUBBER_LOG;
	public static Block RUBBER_LOG_SLAB_HALF;
	public static Block RUBBER_LOG_SLAB_DOUBLE;
	public static Block RUBBER_LOG_STAIR;
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
		
	public static enum SolarPanels {
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
	}

	public static enum Cables implements IItemProvider {
		COPPER(128, 12.0, true, EnumPowerTier.MEDIUM),
		TIN(32, 12.0, true, EnumPowerTier.LOW),
		GOLD(512, 12.0, true, EnumPowerTier.HIGH),
		HV(2048, 12.0, true, EnumPowerTier.EXTREME),
		GLASSFIBER(8192, 12.0, false, EnumPowerTier.INSANE),
		INSULATED_COPPER(128, 10.0, false, EnumPowerTier.MEDIUM),
		INSULATED_GOLD(512, 10.0, false, EnumPowerTier.HIGH),
		INSULATED_HV(2048, 10.0, false, EnumPowerTier.EXTREME);

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
			return Item.getItemFromBlock(block);
		}
	}

	public static enum Ores implements IItemProvider {
		BAUXITE, CINNABAR, COPPER, GALENA, IRIDIUM, LEAD, PERIDOT, PYRITE, RUBY, SAPPHIRE, SHELDONITE, SILVER, SODALITE,
		SPHALERITE, TIN, TUNGSTEN;

		public final String name;
		public final Block block;

		private Ores() {
			name = this.toString().toLowerCase();
			block = new BlockOre();
			InitUtils.setup(block, name + "_ore");
		}

		@Override
		public Item asItem() {
			return Item.getItemFromBlock(block);
		}
	}

	public static enum StorageBlocks implements IItemProvider {
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
			return Item.getItemFromBlock(block);
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
	}
	
	
	public static enum Machine {
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
		
		CREATIVE_QUANTUM_CHEST(new BlockCreativeQuantumChest(), ItemBlockQuantumChest.class),
		CREATIVE_QUANTUM_TANK(new BlockCreativeQuantumTank(), ItemBlockQuantumTank.class),
		DIGITAL_CHEST(new BlockDigitalChest(), ItemBlockDigitalChest.class),
		QUANTUM_CHEST(new BlockQuantumChest(), ItemBlockQuantumChest.class),
		QUANTUM_TANK(new BlockQuantumTank(), ItemBlockQuantumTank.class),
		
		ADJUSTABLE_SU(new BlockAdjustableSU(), ItemBlockAdjustableSU.class),
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
		LAMP_INCANDESCENT(new BlockLamp(14, 4, 0.625, 0.25)),
		LAMP_LED(new BlockLamp(15, 1, 0.0625, 0.125)),
		PLAYER_DETECTOR(new BlockPlayerDetector());
		
		public final String name;
		public final Block block;

		private <B extends Block> Machine(B block) {
			this.name = this.toString().toLowerCase();
			this.block = block;
			InitUtils.setup(block, name);
		}
		
		private <B extends Block, IB extends ItemBlock> Machine(B block, Class<IB> itemBlock) {
			this.name = this.toString().toLowerCase();
			this.block = block;
			InitUtils.setup(block, name);
		}
		
		public ItemStack getStack() {
			return new ItemStack(block);
		}
	}
	
	public static enum Dusts implements IItemProvider {
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

	public static enum SmallDusts implements IItemProvider {
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

	public static enum Gems implements IItemProvider {
		PERIDOT, RED_GARNET, RUBY, SAPPHIRE, YELLOW_GARNET;

		public final String name;
		public final Item item;

		private Gems() {
			name = this.toString().toLowerCase();
			item = new Item();
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

	public static enum Ingots implements IItemProvider {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CHROME, COPPER, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM_ALLOY, IRIDIUM,
		LEAD, MIXED_METAL, NICKEL, PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;

		private Ingots() {
			name = this.toString().toLowerCase();
			item = new Item();
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

	public static enum Nuggets implements IItemProvider {
		ALUMINUM, BRASS, BRONZE, CHROME, COPPER, DIAMOND, ELECTRUM, HOT_TUNGSTENSTEEL, INVAR, IRIDIUM, LEAD, NICKEL,
		PLATINUM, REFINED_IRON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, ZINC;

		public final String name;
		public final Item item;

		private Nuggets() {
			name = this.toString().toLowerCase();
			item = new Item();
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

	public static enum Parts implements IItemProvider {
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
		public Item asItem() {
			return item;
		}
	}

	public static enum Plates implements IItemProvider {
		ADVANCED_ALLOY, ALUMINUM, BRASS, BRONZE, CARBON, COAL, COPPER, DIAMOND, ELECTRUM, EMERALD, GOLD, INVAR,
		IRIDIUM_ALLOY, IRIDIUM, IRON, LAPIS, LAZURITE, LEAD, MAGNALIUM, NICKEL, OBSIDIAN, PERIDOT, PLATINUM, RED_GARNET,
		REDSTONE, REFINED_IRON, RUBY, SAPPHIRE, SILICON, SILVER, STEEL, TIN, TITANIUM, TUNGSTEN, TUNGSTENSTEEL, WOOD,
		YELLOW_GARNET, ZINC;

		public final String name;
		public final Item item;

		private Plates() {
			name = this.toString().toLowerCase();
			item = new Item();
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

	public static enum Upgrades implements IItemProvider {
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
	
	public static void registerBlocks() {
		Arrays.stream(Ores.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(StorageBlocks.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(MachineBlocks.values()).forEach(value -> {
			RebornRegistry.registerBlock(value.frame);
			RebornRegistry.registerBlock(value.casing);
		});
		Arrays.stream(SolarPanels.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(Cables.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		Arrays.stream(Machine.values()).forEach(value -> RebornRegistry.registerBlock(value.block));
		
		// Misc. blocks
		COMPUTER_CUBE = new BlockComputerCube();
		registerBlock(COMPUTER_CUBE, "computer_cube");

		NUKE = new BlockNuke();
		registerBlock(NUKE, "nuke");

		REFINED_IRON_FENCE = new BlockRefinedIronFence();
		registerBlock(REFINED_IRON_FENCE, "refined_iron_fence");

		REINFORCED_GLASS = new BlockReinforcedGlass();
		registerBlock(REINFORCED_GLASS, "reinforced_glass");

		RUBBER_LEAVES = new BlockRubberLeaves();
		registerBlock(RUBBER_LEAVES, "rubber_leaves");

		RUBBER_LOG = new BlockRubberLog();
		registerBlock(RUBBER_LOG, "rubber_log");

		RUBBER_LOG_SLAB_HALF = new BlockRubberPlankSlab.BlockHalf("rubber_plank");
		registerBlockNoItem(RUBBER_LOG_SLAB_HALF, "rubber_plank_slab");

		RUBBER_LOG_SLAB_DOUBLE = new BlockRubberPlankSlab.BlockDouble("rubber_plank", RUBBER_LOG_SLAB_HALF);
		registerBlock(RUBBER_LOG_SLAB_DOUBLE, new ItemSlab(RUBBER_LOG_SLAB_HALF, (BlockSlab) RUBBER_LOG_SLAB_HALF, (BlockSlab) RUBBER_LOG_SLAB_DOUBLE) , "rubber_plank_double_slab");

		RUBBER_LOG_STAIR = new BlockRubberPlankStair(TRContent.RUBBER_LOG.getDefaultState(), "rubber_plank");
		registerBlock(RUBBER_LOG_STAIR, "rubber_plank_stair");

		RUBBER_PLANKS = new BlockRubberPlank();
		registerBlock(RUBBER_PLANKS, "rubber_planks");

		RUBBER_SAPLING = new BlockRubberSapling();
		registerBlock(RUBBER_SAPLING, ItemBlockRubberSapling.class, "rubber_sapling");

		//TODO enable when done
		//		flare = new BlockFlare();
		//		registerBlock(flare, "flare");
		//		ItemBlock itemBlock = new ItemColored(flare, true);
		//		itemBlock.setRegistryName("flareItemBlock");
		//		itemBlock.setCreativeTab(TechRebornCreativeTabMisc.instance);
		//		GameRegistry.register(itemBlock);
		//		GameRegistry.registerTileEntity(TileEntityFlare.class, "TileEntityFlareTR");

		TechReborn.LOGGER.debug("TechReborns Blocks Loaded");
	}

	public static void registerItems() {
		Arrays.stream(Ingots.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Nuggets.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Gems.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Dusts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(SmallDusts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Plates.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Parts.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		Arrays.stream(Upgrades.values()).forEach(value -> RebornRegistry.registerItem(value.item));
		
		// Gem armor & tools
		if (ConfigTechReborn.enableGemArmorAndTools) {
			//Todo: repair with tags
			RebornRegistry.registerItem(BRONZE_SWORD = InitUtils.setup(new ItemTRSword(Reference.BRONZE, "ingotBronze"), "bronze_sword"));
			RebornRegistry.registerItem(BRONZE_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.BRONZE, "ingotBronze"), "bronze_pickaxe"));
			RebornRegistry.registerItem(BRONZE_SPADE = InitUtils.setup(new ItemTRSpade(Reference.BRONZE, "ingotBronze"), "bronze_spade"));
			RebornRegistry.registerItem(BRONZE_AXE = InitUtils.setup(new ItemTRAxe(Reference.BRONZE, "ingotBronze"), "bronze_axe"));
			RebornRegistry.registerItem(BRONZE_HOE = InitUtils.setup(new ItemTRHoe(Reference.BRONZE, "ingotBronze"), "bronze_hoe"));

			RebornRegistry.registerItem(BRONZE_HELMET = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.HEAD, "ingotBronze"), "bronze_helmet"));
			RebornRegistry.registerItem(BRONZE_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.CHEST, "ingotBronze"), "bronze_chestplate"));
			RebornRegistry.registerItem(BRONZE_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.LEGS, "ingotBronze"), "bronze_leggings"));
			RebornRegistry.registerItem(BRONZE_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.FEET, "ingotBronze"), "bronze_boots"));

			RebornRegistry.registerItem(RUBY_SWORD = InitUtils.setup(new ItemTRSword(Reference.RUBY, "gemRuby"), "ruby_sword"));
			RebornRegistry.registerItem(RUBY_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.RUBY, "gemRuby"), "ruby_pickaxe"));
			RebornRegistry.registerItem(RUBY_SPADE = InitUtils.setup(new ItemTRSpade(Reference.RUBY, "gemRuby"), "ruby_spade"));
			RebornRegistry.registerItem(RUBY_AXE = InitUtils.setup(new ItemTRAxe(Reference.RUBY, "gemRuby"), "ruby_axe"));
			RebornRegistry.registerItem(RUBY_HOE = InitUtils.setup(new ItemTRHoe(Reference.RUBY, "gemRuby"), "ruby_hoe"));

			RebornRegistry.registerItem(RUBY_HELMET = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.HEAD, "gemRuby"), "ruby_helmet"));
			RebornRegistry.registerItem(RUBY_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.CHEST, "gemRuby"), "ruby_chestplate"));
			RebornRegistry.registerItem(RUBY_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.LEGS, "gemRuby"), "ruby_leggings"));
			RebornRegistry.registerItem(RUBY_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.FEET, "gemRuby"), "ruby_boots"));

			RebornRegistry.registerItem(SAPPHIRE_SWORD = InitUtils.setup(new ItemTRSword(Reference.SAPPHIRE, "gemSapphire"), "sapphire_sword"));
			RebornRegistry.registerItem(SAPPHIRE_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.SAPPHIRE, "gemSapphire"), "sapphire_pickaxe"));
			RebornRegistry.registerItem(SAPPHIRE_SPADE = InitUtils.setup(new ItemTRSpade(Reference.SAPPHIRE, "gemSapphire"), "sapphire_spade"));
			RebornRegistry.registerItem(SAPPHIRE_AXE = InitUtils.setup(new ItemTRAxe(Reference.SAPPHIRE, "gemSapphire"), "sapphire_axe"));
			RebornRegistry.registerItem(SAPPHIRE_HOE = InitUtils.setup(new ItemTRHoe(Reference.SAPPHIRE, "gemSapphire"), "sapphire_hoe"));

			RebornRegistry.registerItem(SAPPHIRE_HELMET = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.HEAD, "gemSapphire"), "sapphire_helmet"));
			RebornRegistry.registerItem(SAPPHIRE_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.CHEST, "gemSapphire"), "sapphire_chestplate"));
			RebornRegistry.registerItem(SAPPHIRE_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.LEGS, "gemSapphire"), "sapphire_leggings"));
			RebornRegistry.registerItem(SAPPHIRE_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.FEET, "gemSapphire"), "sapphire_boots"));

			RebornRegistry.registerItem(PERIDOT_SWORD = InitUtils.setup(new ItemTRSword(Reference.PERIDOT, "gemPeridot"), "peridot_sword"));
			RebornRegistry.registerItem(PERIDOT_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.PERIDOT, "gemPeridot"), "peridot_pickaxe"));
			RebornRegistry.registerItem(PERIDOT_SPADE = InitUtils.setup(new ItemTRSpade(Reference.PERIDOT, "gemPeridot"), "peridot_spade"));
			RebornRegistry.registerItem(PERIDOT_AXE = InitUtils.setup(new ItemTRAxe(Reference.PERIDOT, "gemPeridot"), "peridot_axe"));
			RebornRegistry.registerItem(PERIDOT_HOE = InitUtils.setup(new ItemTRHoe(Reference.PERIDOT, "gemPeridot"), "peridot_hoe"));

			RebornRegistry.registerItem(PERIDOT_HELMET = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.HEAD, "gemPeridot"), "peridot_helmet"));
			RebornRegistry.registerItem(PERIDOT_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.CHEST, "gemPeridot"), "peridot_chestplate"));
			RebornRegistry.registerItem(PERIDOT_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.LEGS, "gemPeridot"), "peridot_leggings"));
			RebornRegistry.registerItem(PERIDOT_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.FEET, "gemPeridot"), "peridot_boots"));
		}

		// Battery
		RebornRegistry.registerItem(RED_CELL_BATTERY = InitUtils.setup(new ItemRedCellBattery(), "red_cell_battery"));
		RebornRegistry.registerItem(LITHIUM_ION_BATTERY = InitUtils.setup(new ItemLithiumIonBattery(), "lithium_ion_battery"));
		RebornRegistry.registerItem(LITHIUM_ION_BATPACK = InitUtils.setup(new ItemLithiumIonBatpack(), "lithium_ion_batpack"));
		RebornRegistry.registerItem(ENERGY_CRYSTAL = InitUtils.setup(new ItemEnergyCrystal(), "energy_crystal"));
		RebornRegistry.registerItem(LAPOTRON_CRYSTAL = InitUtils.setup(new ItemLapotronCrystal(), "lapotron_crystal"));
		RebornRegistry.registerItem(LAPOTRONIC_ORB = InitUtils.setup(new ItemLapotronicOrb(), "lapotronic_orb"));
		RebornRegistry.registerItem(LAPOTRONIC_ORBPACK = InitUtils.setup(new ItemLapotronicOrbpack(), "lapotronic_orbpack"));

		// Tools
		RebornRegistry.registerItem(TREE_TAP = InitUtils.setup(new ItemTreeTap(), "treetap"));
		RebornRegistry.registerItem(WRENCH = InitUtils.setup(new ItemWrench(), "wrench"));

		RebornRegistry.registerItem(BASIC_DRILL = InitUtils.setup(new ItemBasicDrill(), "basic_drill"));
		RebornRegistry.registerItem(BASIC_CHAINSAW = InitUtils.setup(new ItemBasicChainsaw(), "basic_chainsaw"));
		RebornRegistry.registerItem(BASIC_JACKHAMMER = InitUtils.setup(new ItemBasicJackhammer(), "basic_jackhammer"));
		RebornRegistry.registerItem(ELECTRIC_TREE_TAP = InitUtils.setup(new ItemElectricTreetap(), "electric_treetap"));

		RebornRegistry.registerItem(ADVANCED_DRILL = InitUtils.setup(new ItemAdvancedDrill(), "advanced_drill"));
		RebornRegistry.registerItem(ADVANCED_CHAINSAW = InitUtils.setup(new ItemAdvancedChainsaw(), "advanced_chainsaw"));
		RebornRegistry.registerItem(ADVANCED_JACKHAMMER = InitUtils.setup(new ItemAdvancedJackhammer(), "advanced_jackhammer"));
		RebornRegistry.registerItem(ROCK_CUTTER = InitUtils.setup(new ItemRockCutter(), "rock_cutter"));

		RebornRegistry.registerItem(INDUSTRIAL_DRILL = InitUtils.setup(new ItemIndustrialDrill(), "industrial_drill"));
		RebornRegistry.registerItem(INDUSTRIAL_CHAINSAW = InitUtils.setup(new ItemIndustrialChainsaw(), "industrial_chainsaw"));
		RebornRegistry.registerItem(INDUSTRIAL_JACKHAMMER = InitUtils.setup(new ItemIndustrialJackhammer(), "industrial_jackhammer"));
		RebornRegistry.registerItem(NANOSABER = InitUtils.setup(new ItemNanosaber(), "nanosaber"));
		RebornRegistry.registerItem(OMNI_TOOL = InitUtils.setup(new ItemOmniTool(), "omni_tool"));

		// Armor
		RebornRegistry.registerItem(CLOAKING_DEVICE = InitUtils.setup(new ItemCloakingDevice(), "cloaking_device"));

		// Other
		RebornRegistry.registerItem(FREQUENCY_TRANSMITTER = InitUtils.setup(new ItemFrequencyTransmitter(), "frequency_transmitter"));
		RebornRegistry.registerItem(SCRAP_BOX = InitUtils.setup(new ItemScrapBox(), "scrap_box"));
		RebornRegistry.registerItem(MANUAL = InitUtils.setup(new ItemManual(), "manual"));
		RebornRegistry.registerItem(DEBUG_TOOL = InitUtils.setup(new ItemDebugTool(), "debug_tool"));
		RebornRegistry.registerItem(CELL = InitUtils.setup(new DynamicCell(), "cell"));
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		// TODO: do we need this at all?
		BlockMachineBase.advancedFrameStack = new ItemStack(TRContent.MachineBlocks.ADVANCED.getFrame());
		BlockMachineBase.basicFrameStack = new ItemStack(TRContent.MachineBlocks.BASIC.getFrame());
		
		TechReborn.LOGGER.debug("TechReborns Items Loaded");
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
		
		ResourceLocation upgradeRL = new ResourceLocation(TechReborn.MOD_ID, "items/misc/upgrades");
		Arrays.stream(Upgrades.values()).forEach(value -> ModelLoader.setCustomModelResourceLocation(value.item, 0,
			new ModelResourceLocation(upgradeRL, "type=" + value.name)));

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

		ResourceLocation machineBlockRL = new ResourceLocation(TechReborn.MOD_ID, "machine_block");
		for (MachineBlocks value : MachineBlocks.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(value.frame), 0, new ModelResourceLocation(machineBlockRL, "type=" + value.name + "_machine_frame"));
			ModelLoader.setCustomStateMapper(value.frame, new DefaultStateMapper() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation(machineBlockRL, "type=" + value.name + "_machine_frame");
				}
			});
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(value.casing), 0, new ModelResourceLocation(machineBlockRL, "type=" + value.name + "_machine_casing"));
			ModelLoader.setCustomStateMapper(value.casing, new DefaultStateMapper() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation(machineBlockRL, "type=" + value.name + "_machine_casing");
				}
			});
		}
		
		ResourceLocation cableRL = new ResourceLocation(TechReborn.MOD_ID, "cable_inv");
		for (Cables value : Cables.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(value.block), 0,
					new ModelResourceLocation(cableRL, "type=" + value.name));
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
	}

	/**
	 * Wrapper method for RebornRegistry
	 * @param block Block to register
	 * @param name Name of block to register
	 */
	public static void registerBlock(Block block, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, new ResourceLocation(TechReborn.MOD_ID, name));
	}

	/**
	 * Wrapper method for RebornRegistry
	 * @param block Block to Register
	 * @param itemclass Itemblock of block to register
	 * @param name Name of block to register
	 */
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, itemclass, new ResourceLocation(TechReborn.MOD_ID, name));
	}

	public static void registerBlock(Block block, ItemBlock itemBlock, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlock(block, itemBlock, new ResourceLocation(TechReborn.MOD_ID, name));
	}

	public static void registerBlockNoItem(Block block, String name) {
		name = name.toLowerCase();
		block.setTranslationKey(TechReborn.MOD_ID + "." + name);
		RebornRegistry.registerBlockNoItem(block,  new ResourceLocation(TechReborn.MOD_ID, name));
	}

}
