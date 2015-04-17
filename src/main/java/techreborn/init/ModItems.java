package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.items.ItemCells;
import techreborn.items.ItemDusts;
import techreborn.items.ItemDustsSmall;
import techreborn.items.ItemGems;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.items.ItemTechManuel;
import techreborn.items.armor.ItemGravityChest;
import techreborn.items.armor.ItemLapotronPack;
import techreborn.items.armor.ItemLithiumBatpack;
import techreborn.items.tools.ItemAdvancedDrill;
import techreborn.items.tools.ItemOmniTool;
import techreborn.items.tools.ItemRockCutter;
import techreborn.util.LogHelper;

public class ModItems {

    public static Item dusts;
    public static Item smallDusts;
    public static Item ingots;
    public static Item gems;
    public static Item parts;
    public static Item cells;
    public static Item rockCutter;
    public static Item lithiumBatpack;
    public static Item lapotronpack;
    public static Item gravityChest;
    public static Item omniTool;
    public static Item advancedDrill;
    public static Item manuel;

    public static void init() {
        dusts = new ItemDusts();
        GameRegistry.registerItem(dusts, "dust");
        smallDusts = new ItemDustsSmall();
        GameRegistry.registerItem(smallDusts, "smallDust");
        ingots = new ItemIngots();
        GameRegistry.registerItem(ingots, "ingot");
        gems = new ItemGems();
        GameRegistry.registerItem(gems, "gem");
        parts = new ItemParts();
        GameRegistry.registerItem(parts, "part");
        cells = new ItemCells();
        GameRegistry.registerItem(cells, "cell");
        rockCutter = new ItemRockCutter(ToolMaterial.EMERALD);
        GameRegistry.registerItem(rockCutter, "rockCutter");
        lithiumBatpack = new ItemLithiumBatpack(ArmorMaterial.DIAMOND, 7, 1);
        GameRegistry.registerItem(lithiumBatpack, "lithiumBatpack");
        lapotronpack = new ItemLapotronPack(ArmorMaterial.DIAMOND, 7, 1);
        GameRegistry.registerItem(lapotronpack, "lapotronPack");
        omniTool = new ItemOmniTool(ToolMaterial.EMERALD);
        GameRegistry.registerItem(omniTool, "omniTool");
        advancedDrill = new ItemAdvancedDrill();
        GameRegistry.registerItem(advancedDrill, "advancedDrill");
        gravityChest = new ItemGravityChest(ArmorMaterial.DIAMOND, 7, 1);
        GameRegistry.registerItem(gravityChest, "gravitychestplate");
        manuel = new ItemTechManuel();
        GameRegistry.registerItem(manuel, "techmanuel");

        LogHelper.info("TechReborns Items Loaded");

        registerOreDict();
    }

    public static void registerOreDict() {
        //Dusts
        OreDictionary.registerOre("dustAlmandine", new ItemStack(dusts, 1, 0));
        OreDictionary.registerOre("dustAluminium", new ItemStack(dusts, 1, 1));
        OreDictionary.registerOre("dustAndradite", new ItemStack(dusts, 1, 2));
        OreDictionary.registerOre("dustBasalt", new ItemStack(dusts, 1, 4));
        OreDictionary.registerOre("dustBauxite", new ItemStack(dusts, 1, 5));
        OreDictionary.registerOre("dustBrass", new ItemStack(dusts, 1, 6));
        OreDictionary.registerOre("dustBronze", new ItemStack(dusts, 1, 7));
        OreDictionary.registerOre("dustCalcite", new ItemStack(dusts, 1, 8));
        OreDictionary.registerOre("dustCharcoal", new ItemStack(dusts, 1, 9));
        OreDictionary.registerOre("dustChrome", new ItemStack(dusts, 1, 10));
        OreDictionary.registerOre("dustCinnabar", new ItemStack(dusts, 1, 11));
        OreDictionary.registerOre("dustClay", new ItemStack(dusts, 1, 12));
        OreDictionary.registerOre("dustCoal", new ItemStack(dusts, 1, 13));
        OreDictionary.registerOre("dustCopper", new ItemStack(dusts, 1, 14));
        OreDictionary.registerOre("dustDiamond", new ItemStack(dusts, 1, 16));
        OreDictionary.registerOre("dustElectrum", new ItemStack(dusts, 1, 17));
        OreDictionary.registerOre("dustEmerald", new ItemStack(dusts, 1, 18));
        OreDictionary.registerOre("dustEnderEye", new ItemStack(dusts, 1, 19));
        OreDictionary.registerOre("dustEnderPearl", new ItemStack(dusts, 1, 20));
        OreDictionary.registerOre("dustEndstone", new ItemStack(dusts, 1, 21));
        OreDictionary.registerOre("dustFlint", new ItemStack(dusts, 1, 22));
        OreDictionary.registerOre("dustGold", new ItemStack(dusts, 1, 23));
        OreDictionary.registerOre("dustGreenSapphire", new ItemStack(dusts, 1, 24));
        OreDictionary.registerOre("dustGrossular", new ItemStack(dusts, 1, 25));
        OreDictionary.registerOre("dustInvar", new ItemStack(dusts, 1, 26));
        OreDictionary.registerOre("dustIron", new ItemStack(dusts, 1, 27));
        OreDictionary.registerOre("dustLazurite", new ItemStack(dusts, 1, 28));
        OreDictionary.registerOre("dustLead", new ItemStack(dusts, 1, 29));
        OreDictionary.registerOre("dustMagnesium", new ItemStack(dusts, 1, 30));
        OreDictionary.registerOre("dustMarble", new ItemStack(dusts, 31));
        OreDictionary.registerOre("dustNetherrack", new ItemStack(dusts, 32));
        OreDictionary.registerOre("dustNickel", new ItemStack(dusts, 1, 33));
        OreDictionary.registerOre("dustObsidian", new ItemStack(dusts, 1, 34));
        OreDictionary.registerOre("dustOlivine", new ItemStack(dusts, 1, 35));
        OreDictionary.registerOre("dustPhosphor", new ItemStack(dusts, 1, 36));
        OreDictionary.registerOre("dustPlatinum", new ItemStack(dusts, 1, 37));
        OreDictionary.registerOre("dustPyrite", new ItemStack(dusts, 1, 38));
        OreDictionary.registerOre("dustPyrope", new ItemStack(dusts, 1, 39));
        OreDictionary.registerOre("dustRedGarnet", new ItemStack(dusts, 1, 40));
        OreDictionary.registerOre("dustRedrock", new ItemStack(dusts, 1, 41));
        OreDictionary.registerOre("dustRuby", new ItemStack(dusts, 1, 42));
        OreDictionary.registerOre("dustSaltpeter", new ItemStack(dusts, 1, 43));
        OreDictionary.registerOre("dustSapphire", new ItemStack(dusts, 1, 44));
        OreDictionary.registerOre("dustSilver", new ItemStack(dusts, 1, 45));
        OreDictionary.registerOre("dustSodalite", new ItemStack(dusts, 1, 46));
        OreDictionary.registerOre("dustSpessartine", new ItemStack(dusts, 1, 47));
        OreDictionary.registerOre("dustSphalerite", new ItemStack(dusts, 1, 48));
        OreDictionary.registerOre("dustSteel", new ItemStack(dusts, 1, 49));
        OreDictionary.registerOre("dustSulfur", new ItemStack(dusts, 1, 50));
        OreDictionary.registerOre("dustTin", new ItemStack(dusts, 1, 51));
        OreDictionary.registerOre("dustTitanium", new ItemStack(dusts, 1, 52));
        OreDictionary.registerOre("dustTungsten", new ItemStack(dusts, 1, 53));
        OreDictionary.registerOre("dustUranium", new ItemStack(dusts, 1, 54));
        OreDictionary.registerOre("dustUvarovite", new ItemStack(dusts, 1, 55));
        OreDictionary.registerOre("dustYellowGarnet", new ItemStack(dusts, 1, 56));
        OreDictionary.registerOre("dustZinc", new ItemStack(dusts, 1, 57));
        OreDictionary.registerOre("ingotCobalt", new ItemStack(dusts, 1, 58));
        OreDictionary.registerOre("ingotArdite", new ItemStack(ingots, 1, 59));
        OreDictionary.registerOre("ingotManyullyn", new ItemStack(ingots, 1, 60));
        OreDictionary.registerOre("ingotAluminumBrass", new ItemStack(ingots, 1, 61));
        OreDictionary.registerOre("ingotAlumite", new ItemStack(ingots, 1, 62));
        //Ingots
        OreDictionary.registerOre("ingotIridium", new ItemStack(ingots, 1, 3));
        OreDictionary.registerOre("ingotSilver", new ItemStack(ingots, 1, 4));
        OreDictionary.registerOre("ingotAluminium", new ItemStack(ingots, 1, 5));
        OreDictionary.registerOre("ingotTitanium", new ItemStack(ingots, 1, 6));
        OreDictionary.registerOre("ingotChrome", new ItemStack(ingots, 1, 7));
        OreDictionary.registerOre("ingotElectrum", new ItemStack(ingots, 1, 8));
        OreDictionary.registerOre("ingotTungsten", new ItemStack(ingots, 1, 9));
        OreDictionary.registerOre("ingotLead", new ItemStack(ingots, 1, 10));
        OreDictionary.registerOre("ingotZinc", new ItemStack(ingots, 1, 11));
        OreDictionary.registerOre("ingotBrass", new ItemStack(ingots, 1, 12));
        OreDictionary.registerOre("ingotSteel", new ItemStack(ingots, 1, 13));
        OreDictionary.registerOre("ingotPlatinum", new ItemStack(ingots, 1, 14));
        OreDictionary.registerOre("ingotNickel", new ItemStack(ingots, 1, 15));
        OreDictionary.registerOre("ingotInvar", new ItemStack(ingots, 1, 16));
        OreDictionary.registerOre("ingotCobalt", new ItemStack(ingots, 1, 17));
        OreDictionary.registerOre("ingotArdite", new ItemStack(ingots, 1, 18));
        OreDictionary.registerOre("ingotManyullyn", new ItemStack(ingots, 1, 19));
        OreDictionary.registerOre("ingotAluminumBrass", new ItemStack(ingots, 1, 20));
        OreDictionary.registerOre("ingotAlumite", new ItemStack(ingots, 1, 21));
        //Gems
        OreDictionary.registerOre("gemRuby", new ItemStack(gems, 1, 0));
        OreDictionary.registerOre("gemSapphire", new ItemStack(gems, 1, 1));
        OreDictionary.registerOre("gemGreenSapphire", new ItemStack(gems, 1, 2));
        OreDictionary.registerOre("gemOlivine", new ItemStack(gems, 1, 3));
        OreDictionary.registerOre("gemRedGarnet", new ItemStack(gems, 1, 4));
        OreDictionary.registerOre("gemYellowGarnet", new ItemStack(gems, 1, 5));

    }

}
