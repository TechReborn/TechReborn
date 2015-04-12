package techreborn.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.items.ItemDusts;
import techreborn.items.ItemGems;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.util.LogHelper;

public class ModItems {
	
    public static Item dusts;
    public static Item ingots;
    public static Item gems;
    public static Item parts;

    public static void init()
    {
    	dusts = new ItemDusts();
        GameRegistry.registerItem(dusts, "dust");
        ingots = new ItemIngots();
        GameRegistry.registerItem(ingots, "ingot");
        gems = new ItemGems();
        GameRegistry.registerItem(gems, "gem");
        parts = new ItemParts();
        GameRegistry.registerItem(parts, "part");
		LogHelper.info("TechReborns Items Loaded");

        registerOreDict();
    }
    
    public static void registerOreDict()
    {
    	//Dusts
    	OreDictionary.registerOre("dustAlmandine", new ItemStack(dusts,0));
    	OreDictionary.registerOre("dustAluminium", new ItemStack(dusts,1));
    	OreDictionary.registerOre("dustAndradite", new ItemStack(dusts,2));
    	OreDictionary.registerOre("dustBasalt", new ItemStack(dusts,4));
    	OreDictionary.registerOre("dustBauxite", new ItemStack(dusts,5));
    	OreDictionary.registerOre("dustBrass", new ItemStack(dusts,6));
    	OreDictionary.registerOre("dustBronze", new ItemStack(dusts,7));
    	OreDictionary.registerOre("dustCalcite", new ItemStack(dusts,8));
    	OreDictionary.registerOre("dustCharcoal", new ItemStack(dusts,9));
    	OreDictionary.registerOre("dustChrome", new ItemStack(dusts,10));
    	OreDictionary.registerOre("dustCinnabar", new ItemStack(dusts,11));
    	OreDictionary.registerOre("dustClay", new ItemStack(dusts,12));
    	OreDictionary.registerOre("dustCoal", new ItemStack(dusts,13));
    	OreDictionary.registerOre("dustCopper", new ItemStack(dusts,14));
    	OreDictionary.registerOre("dustDiamond", new ItemStack(dusts,16));
    	OreDictionary.registerOre("dustElectrum", new ItemStack(dusts,17));
    	OreDictionary.registerOre("dustEmerald", new ItemStack(dusts,18));
    	OreDictionary.registerOre("dustEnderEye", new ItemStack(dusts,19));
    	OreDictionary.registerOre("dustEnderPearl", new ItemStack(dusts,20));
    	OreDictionary.registerOre("dustEndstone", new ItemStack(dusts,21));
    	OreDictionary.registerOre("dustFlint", new ItemStack(dusts,22));
    	OreDictionary.registerOre("dustGold", new ItemStack(dusts,23));
    	OreDictionary.registerOre("dustGreenSapphire", new ItemStack(dusts,24));
    	OreDictionary.registerOre("dustGrossular", new ItemStack(dusts,25));
    	OreDictionary.registerOre("dustInvar", new ItemStack(dusts,26));
    	OreDictionary.registerOre("dustIron", new ItemStack(dusts,27));
    	OreDictionary.registerOre("dustLazurite", new ItemStack(dusts,28));
    	OreDictionary.registerOre("dustLead", new ItemStack(dusts,29));
    	OreDictionary.registerOre("dustMagnesium", new ItemStack(dusts,30));
    	OreDictionary.registerOre("dustMarble", new ItemStack(dusts,31));
    	OreDictionary.registerOre("dustNetherrack", new ItemStack(dusts,32));
    	OreDictionary.registerOre("dustNickel", new ItemStack(dusts,33));
    	OreDictionary.registerOre("dustObsidian", new ItemStack(dusts,34));
    	OreDictionary.registerOre("dustOlivine", new ItemStack(dusts,35));
    	OreDictionary.registerOre("dustPhosphor", new ItemStack(dusts,36));
    	OreDictionary.registerOre("dustPlatinum", new ItemStack(dusts,37));
    	OreDictionary.registerOre("dustPyrite", new ItemStack(dusts,38));
    	OreDictionary.registerOre("dustPyrope", new ItemStack(dusts,39));
    	OreDictionary.registerOre("dustRedGarnet", new ItemStack(dusts,40));
    	OreDictionary.registerOre("dustRedrock", new ItemStack(dusts,41));
    	OreDictionary.registerOre("dustRuby", new ItemStack(dusts,42));
    	OreDictionary.registerOre("dustSaltpeter", new ItemStack(dusts,43));
    	OreDictionary.registerOre("dustSapphire", new ItemStack(dusts,44));
    	OreDictionary.registerOre("dustSilver", new ItemStack(dusts,45));
    	OreDictionary.registerOre("dustSodalite", new ItemStack(dusts,46));
    	OreDictionary.registerOre("dustSpessartine", new ItemStack(dusts,47));
    	OreDictionary.registerOre("dustSphalerite", new ItemStack(dusts,48));
    	OreDictionary.registerOre("dustSteel", new ItemStack(dusts,49));
    	OreDictionary.registerOre("dustSulfur", new ItemStack(dusts,50));
    	OreDictionary.registerOre("dustTin", new ItemStack(dusts,51));
    	OreDictionary.registerOre("dustTitanium", new ItemStack(dusts,52));
    	OreDictionary.registerOre("dustTungsten", new ItemStack(dusts,53));
    	OreDictionary.registerOre("dustUranium", new ItemStack(dusts,54));
    	OreDictionary.registerOre("dustUvarovite", new ItemStack(dusts,55));
    	OreDictionary.registerOre("dustYellowGarnet", new ItemStack(dusts,56));
    	OreDictionary.registerOre("dustZinc", new ItemStack(dusts,57));
    }

}
