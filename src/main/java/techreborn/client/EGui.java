package techreborn.client;

public enum EGui {

	THERMAL_GENERATOR(true),
	QUANTUM_TANK(true),
	QUANTUM_CHEST(true),
	CENTRIFUGE(true),
	ROLLING_MACHINE(true),
	BLAST_FURNACE(true),
	ALLOY_SMELTER(true),
	INDUSTRIAL_GRINDER(true),
	IMPLOSION_COMPRESSOR(true),
	MATTER_FABRICATOR(true),
	MANUAL(false),
	CHUNK_LOADER(true),
	ASSEMBLING_MACHINE(true),
	DIESEL_GENERATOR(true),
	INDUSTRIAL_ELECTROLYZER(true),
	AESU(false),
	ALLOY_FURNACE(true),
	SAWMILL(true),
	CHEMICAL_REACTOR(true),
	SEMIFLUID_GENERATOR(true),
	GAS_TURBINE(true),
	DIGITAL_CHEST(true),
	DESTRUCTOPACK(false),
	LESU(false),
	IDSU(false),
	CHARGEBENCH(true),
	FUSION_CONTROLLER(true),
	VACUUM_FREEZER(true),
	GRINDER(true),
	GENERATOR(true),
	EXTRACTOR(true),
	COMPRESSOR(true),
	ELECTRIC_FURNACE(true),
	IRON_FURNACE(true),
	RECYCLER(true),
	SCRAPBOXINATOR(true),
	BATBOX(true),
	MFSU(true),
	MFE(true);

	private final boolean containerBuilder;

	private EGui(final boolean containerBuilder)
	{
		this.containerBuilder = containerBuilder;
	}

	public boolean useContainerBuilder() {
		return this.containerBuilder;
	}
}
