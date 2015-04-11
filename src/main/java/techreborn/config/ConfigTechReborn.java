package techreborn.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigTechReborn {
	private static ConfigTechReborn instance = null;
	public static String CATEGORY_WORLD = "world";
	
	//WORLDGEN
	public static boolean GalenaOreTrue;
	public static boolean IridiumOreTrue;
	public static boolean RubyOreTrue;
	public static boolean SapphireOreTrue;
	public static boolean BauxiteOreTrue;
	public static boolean PyriteOreTrue;
	public static boolean CinnabarOreTrue;
	public static boolean SphaleriteOreTrue;
	public static boolean TungstonOreTrue;
	public static boolean SheldoniteOreTrue;
	public static boolean OlivineOreTrue;
	public static boolean SodaliteOreTrue;


	
	public static Configuration config;

	private ConfigTechReborn(File configFile)
	{
		config = new Configuration(configFile);
		config.load();

		ConfigTechReborn.Configs();

		config.save();

	}

	public static ConfigTechReborn initialize(File configFile)
	{

		if (instance == null)
			instance = new ConfigTechReborn(configFile);
		else
			throw new IllegalStateException(
					"Cannot initialize TechReborn Config twice");

		return instance;
	}

	public static ConfigTechReborn instance()
	{
		if (instance == null) {

			throw new IllegalStateException(
					"Instance of TechReborn Config requested before initialization");
		}
		return instance;
	}
	
	public static void Configs()
	{
		GalenaOreTrue = config.get(CATEGORY_WORLD,
				"Allow GalenaOre", true,
				"Allow GalenaOre to be generated in your world.")
				.getBoolean(true);
		IridiumOreTrue = config.get(CATEGORY_WORLD,
				"Allow IridiumOre", true,
				"Allow IridiumOre to be generated in your world.")
				.getBoolean(true);
		RubyOreTrue = config.get(CATEGORY_WORLD,
				"Allow RubyOre", true,
				"Allow RubyOre to be generated in your world.")
				.getBoolean(true);
		SapphireOreTrue = config.get(CATEGORY_WORLD,
				"Allow SapphireOre", true,
				"Allow SapphireOre to be generated in your world.")
				.getBoolean(true);
		BauxiteOreTrue = config.get(CATEGORY_WORLD,
				"Allow BauxiteOre", true,
				"Allow BauxiteOre to be generated in your world.")
				.getBoolean(true);
		PyriteOreTrue = config.get(CATEGORY_WORLD,
				"Allow PyriteOre", true,
				"Allow PyriteOre to be generated in your world.")
				.getBoolean(true);
		CinnabarOreTrue = config.get(CATEGORY_WORLD,
				"Allow CinnabarOre", true,
				"Allow CinnabarOre to be generated in your world.")
				.getBoolean(true);
		SphaleriteOreTrue = config.get(CATEGORY_WORLD,
				"Allow SphaleriteOre", true,
				"Allow SphaleriteOre to be generated in your world.")
				.getBoolean(true);
		TungstonOreTrue = config.get(CATEGORY_WORLD,
				"Allow TungstonOre", true,
				"Allow TungstonOre to be generated in your world.")
				.getBoolean(true);
		SheldoniteOreTrue = config.get(CATEGORY_WORLD,
				"Allow SheldoniteOre", true,
				"Allow SheldoniteOre to be generated in your world.")
				.getBoolean(true);
		OlivineOreTrue = config.get(CATEGORY_WORLD,
				"Allow OlivineOre", true,
				"Allow OlivineOre to be generated in your world.")
				.getBoolean(true);
		SodaliteOreTrue = config.get(CATEGORY_WORLD,
				"Allow SodaliteOre", true,
				"Allow SodaliteOre to be generated in your world.")
				.getBoolean(true);
		
		if (config.hasChanged())
			config.save();
	}


}
