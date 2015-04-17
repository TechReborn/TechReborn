package techreborn.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigTechReborn {
    private static ConfigTechReborn instance = null;
    public static String CATEGORY_WORLD = "world";
    public static String CATEGORY_POWER = "power";
    public static String CATEGORY_CRAFTING = "crafting";

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

    //Power
    public static int ThermalGenertaorOutput;
    public static int CentrifugeInputTick;
    //Charge
    public static int AdvancedDrillCharge;
    public static int LapotronPackCharge;
    public static int LithiumBatpackCharge;
    public static int OmniToolCharge;
    public static int RockCutterCharge;
    public static int GravityCharge;
    public static int CentrifugeCharge;
    public static int ThermalGeneratorCharge;
    //Tier
    public static int AdvancedDrillTier;
    public static int LapotronPackTier;
    public static int LithiumBatpackTier;
    public static int OmniToolTier;
    public static int RockCutterTier;
    public static int GravityTier;
    public static int CentrifugeTier;
    public static int ThermalGeneratorTier;
    //Crafting
    public static boolean ExpensiveMacerator;
    public static boolean ExpensiveDrill;
    public static boolean ExpensiveDiamondDrill;
    public static boolean ExpensiveSolar;


    public static Configuration config;

    private ConfigTechReborn(File configFile) {
        config = new Configuration(configFile);
        config.load();

        ConfigTechReborn.Configs();

        config.save();

    }

    public static ConfigTechReborn initialize(File configFile) {

        if (instance == null)
            instance = new ConfigTechReborn(configFile);
        else
            throw new IllegalStateException(
                    "Cannot initialize TechReborn Config twice");

        return instance;
    }

    public static ConfigTechReborn instance() {
        if (instance == null) {

            throw new IllegalStateException(
                    "Instance of TechReborn Config requested before initialization");
        }
        return instance;
    }

    public static void Configs() {
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

        //Power
        ThermalGenertaorOutput = config.get(CATEGORY_POWER,
                "Thermal Generator Power", 30,
                "The amount of power that the thermal generator makes for 1mb of lava")
                .getInt();
        CentrifugeInputTick = config.get(CATEGORY_POWER,
                "Centrifuge power usage", 5,
                "The amount of eu per tick that the Centrifuge uses.")
                .getInt();
        //Charge
        AdvancedDrillCharge = config.get(CATEGORY_POWER,
                "Advanced drill max charge", 60000,
                "The amount of power that the anvanced drill can hold")
                .getInt();
        LapotronPackCharge = config.get(CATEGORY_POWER,
                "Lapotron Pack max charge", 100000000,
                "The amount of power that the Lapotron Pack can hold")
                .getInt();
        LithiumBatpackCharge = config.get(CATEGORY_POWER,
                "Lithium Batpack max charge", 4000000,
                "The amount of power that the Lithium Batpack can hold")
                .getInt();
        OmniToolCharge = config.get(CATEGORY_POWER,
                "OmniTool max charge", 20000,
                "The amount of power that the OmniTool can hold")
                .getInt();
        RockCutterCharge = config.get(CATEGORY_POWER,
                "RockCutter max charge", 10000,
                "The amount of power that the RockCutter can hold")
                .getInt();
        GravityCharge = config.get(CATEGORY_POWER,
                "Gravity Chestplate max charge", 100000,
                "The amount of power that the Gravity Chestplate can hold")
                .getInt();
        CentrifugeCharge = config.get(CATEGORY_POWER,
                "Centrifuge max charge", 1000000,
                "The amount of power that the Centrifuge can hold")
                .getInt();
        ThermalGeneratorCharge = config.get(CATEGORY_POWER,
                "Thermal Generator max charge", 1000000,
                "The amount of power that the Thermal Generator can hold")
                .getInt();
        //Teir
        AdvancedDrillTier = config.get(CATEGORY_POWER,
                "Advanced drill Tier", 2,
                "The tier of the Advanced Drill")
                .getInt();
        LapotronPackTier = config.get(CATEGORY_POWER,
                "Lapotron pack tier", 2,
                "The tier of the Lapotron Pack")
                .getInt();
        LithiumBatpackTier = config.get(CATEGORY_POWER,
                "Lithium Batpack tier", 3,
                "The tier of the Lithium Batpack")
                .getInt();
        OmniToolTier = config.get(CATEGORY_POWER,
                "Omni Tool tier", 3,
                "The tier of the OmniTool")
                .getInt();
        RockCutterTier = config.get(CATEGORY_POWER,
                "Rock Cutter tier", 3,
                "The tier of the RockCutter")
                .getInt();
        GravityTier = config.get(CATEGORY_POWER,
                "GravityChestplate tier", 3,
                "The tier of the GravityChestplate")
                .getInt();
        CentrifugeTier = config.get(CATEGORY_POWER,
                "Centrifuge tier", 1,
                "The tier of the Centrifuge")
                .getInt();
        ThermalGeneratorTier = config.get(CATEGORY_POWER,
                "Thermal Generator tier", 1,
                "The tier of the Thermal Generator")
                .getInt();

        //Crafting
        ExpensiveMacerator = config.get(CATEGORY_CRAFTING,
                "Allow Expensive Macerator", true,
                "Allow TechReborn to overwrite the IC2 recipe for Macerator.")
                .getBoolean(true);
        ExpensiveDrill = config.get(CATEGORY_CRAFTING,
                "Allow Expensive Drill", true,
                "Allow TechReborn to overwrite the IC2 recipe for Drill.")
                .getBoolean(true);
        ExpensiveDiamondDrill = config.get(CATEGORY_CRAFTING,
                "Allow Expensive DiamondDrill", true,
                "Allow TechReborn to overwrite the IC2 recipe for DiamondDrill.")
                .getBoolean(true);
        ExpensiveSolar = config.get(CATEGORY_CRAFTING,
                "Allow Expensive Solar panels", true,
                "Allow TechReborn to overwrite the IC2 recipe for Solar panels.")
                .getBoolean(true);

        if (config.hasChanged())
            config.save();
    }


}
