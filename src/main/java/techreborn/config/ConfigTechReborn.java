package techreborn.config;

import net.minecraft.util.StatCollector;
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
    public static boolean CopperOreTrue;
    public static boolean TinOreTrue;
    public static boolean LeadOreTrue;
    public static boolean SilverOreTrue;

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
                StatCollector.translateToLocal("config.techreborn.allow.galenaOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.galenaOre.tooltip"))
                .getBoolean(true);
        IridiumOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.iridiumOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.iridiumOre.tooltip"))
                .getBoolean(true);
        RubyOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.rubyOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.rubyOre.tooltip"))
                .getBoolean(true);
        SapphireOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.sapphireOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.sapphireOre.tooltip"))
                .getBoolean(true);
        BauxiteOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.bauxiteOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.bauxiteOre.tooltip"))
                .getBoolean(true);
        CopperOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.copperOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.copperOre.tooltip"))
                .getBoolean(true);
        TinOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.tinOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.tinOre.tooltip"))
                .getBoolean(true);
        LeadOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.leadOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.leadOre.tooltip"))
                .getBoolean(true);
        SilverOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.silverOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.silverOre.tooltip"))
                .getBoolean(true);
        PyriteOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.pyriteOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.pyriteOre.tooltip"))
                .getBoolean(true);
        CinnabarOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.cinnabarOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.cinnabarOre.tooltip"))
                .getBoolean(true);
        SphaleriteOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.sphaleriteOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.sphaleriteOre.tooltip"))
                .getBoolean(true);
        TungstonOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.tungstonOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.tungstonOre.tooltip"))
                .getBoolean(true);
        SheldoniteOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.sheldoniteOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.sheldoniteOre.tooltip"))
                .getBoolean(true);
        OlivineOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.olivineOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.olivineOre.tooltip"))
                .getBoolean(true);
        SodaliteOreTrue = config.get(CATEGORY_WORLD,
                StatCollector.translateToLocal("config.techreborn.allow.sodaliteOre"), true,
                StatCollector.translateToLocal("config.techreborn.allow.sodaliteOre.tooltip"))
                .getBoolean(true);

        //Power
        ThermalGenertaorOutput = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.thermalGeneratorPower"), 30,
                StatCollector.translateToLocal("config.techreborn.thermalGeneratorPower.tooltip"))
                .getInt();
        CentrifugeInputTick = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.centrifugePowerUsage"), 5,
                StatCollector.translateToLocal("config.techreborn.centrifugePowerUsage.tooltip"))
                .getInt();
        
        //Charge
        AdvancedDrillCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.advancedDrillMaxCharge"), 60000,
                StatCollector.translateToLocal("config.techreborn.advancedDrillMaxCharge.tooltip"))
                .getInt();
        LapotronPackCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.lapotronPackMaxCharge"), 100000000,
                StatCollector.translateToLocal("config.techreborn.lapotronPackMaxCharge.tooltop"))
                .getInt();
        LithiumBatpackCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.lithiumBatpackMaxCharge"), 4000000,
                StatCollector.translateToLocal("config.techreborn.lithiumBatpackMaxCharge.tooltip"))
                .getInt();
        OmniToolCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.omniToolMaxCharge"), 20000,
                StatCollector.translateToLocal("config.techreborn.omniToolMaxCharge.tooltip"))
                .getInt();
        RockCutterCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.rockCutterMaxCharge"), 10000,
                StatCollector.translateToLocal("config.techreborn.rockCutterMaxCharge.tooltip"))
                .getInt();
        GravityCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.gravityChestplateMaxCharge"), 100000,
                StatCollector.translateToLocal("config.techreborn.gravityChestplateMaxCharge.tooltip"))
                .getInt();
        CentrifugeCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.centrifugeMaxCharge"), 1000000,
                StatCollector.translateToLocal("config.techreborn.centrifugeMaxCharge.tooltip"))
                .getInt();
        ThermalGeneratorCharge = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.thermalGeneratorMaxCharge"), 1000000,
                StatCollector.translateToLocal("config.techreborn.thermalGeneratorMaxCharge.tooltip"))
                .getInt();
        
        //Teir
        AdvancedDrillTier = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.advancedDrillTier"), 2,
                StatCollector.translateToLocal("config.techreborn.advancedDrillTier.tooltip"))
                .getInt();
        LapotronPackTier = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.lapotronPackTier"), 2,
                StatCollector.translateToLocal("config.techreborn.lapotronPackTier.tooltip"))
                .getInt();
        LithiumBatpackTier = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.lithiumBatpackTier"), 3,
                StatCollector.translateToLocal("config.techreborn.lithiumBatpackTier.tooltip"))
                .getInt();
        OmniToolTier = config.get(CATEGORY_POWER,
                StatCollector.translateToLocal("config.techreborn.omniToolTier"), 3,
                StatCollector.translateToLocal("config.techreborn.omniToolTier.tooltip"))
                .getInt();
        RockCutterTier = config.get(CATEGORY_POWER,
        		StatCollector.translateToLocal("config.techreborn.rockCutterTier"), 3,
        		StatCollector.translateToLocal("config.techreborn.rockCutterTier.tooltip"))
                .getInt();
        GravityTier = config.get(CATEGORY_POWER,
        		StatCollector.translateToLocal("config.techreborn.gravityChestplateTier"), 3,
        		StatCollector.translateToLocal("config.techreborn.gravityChestplateTier.tooltip"))
                .getInt();
        CentrifugeTier = config.get(CATEGORY_POWER,
        		StatCollector.translateToLocal("config.techreborn.centrifugeTier"), 1,
        		StatCollector.translateToLocal("config.techreborn.centrifugeTier.tooltip"))
                .getInt();
        ThermalGeneratorTier = config.get(CATEGORY_POWER,
        		StatCollector.translateToLocal("config.techreborn.thermalGeneratorTier"), 1,
        		StatCollector.translateToLocal("config.techreborn.thermalGeneratorTier.tooltip"))
                .getInt();

        //Crafting
        ExpensiveMacerator = config.get(CATEGORY_CRAFTING,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveMacerator"), true,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveMacerator.tooltip"))
                .getBoolean(true);
        ExpensiveDrill = config.get(CATEGORY_CRAFTING,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveDrill"), true,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveDrill.tooltip"))
                .getBoolean(true);
        ExpensiveDiamondDrill = config.get(CATEGORY_CRAFTING,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveDiamondDrill"), true,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveDiamondDrill.tooltip"))
                .getBoolean(true);
        ExpensiveSolar = config.get(CATEGORY_CRAFTING,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveSolarPanels"), true,
        		StatCollector.translateToLocal("config.techreborn.allowExpensiveSolarPanels.tooltip"))
                .getBoolean(true);

        if (config.hasChanged())
            config.save();
    }


}
