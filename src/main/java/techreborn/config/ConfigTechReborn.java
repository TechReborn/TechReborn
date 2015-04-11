package techreborn.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigTechReborn {
	private static ConfigTechReborn instance = null;
	public static String CATEGORY_WORLD = "world";
	
	//WORLDGEN
	public static boolean GalenaOreTrue;
	
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
		
		if (config.hasChanged())
			config.save();
	}


}
