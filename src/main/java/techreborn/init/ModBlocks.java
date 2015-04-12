package techreborn.init;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockQuantumChest;
import techreborn.blocks.BlockQuantumTank;
import techreborn.blocks.BlockThermalGenerator;
import techreborn.client.TechRebornCreativeTab;
import techreborn.itemblocks.ItemBlockOre;
import techreborn.itemblocks.ItemBlockQuantumChest;
import techreborn.itemblocks.ItemBlockQuantumTank;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileQuantumTank;
import techreborn.tiles.TileThermalGenerator;
import techreborn.util.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	    public static Block thermalGenerator;
	    public static Block quantumTank;
	    public static Block quantumChest;
	    public static Block ore;
	    
	    public static void init()
	    {
	    	thermalGenerator = new BlockThermalGenerator().setBlockName("techreborn.thermalGenerator").setBlockTextureName("techreborn:ThermalGenerator_other").setCreativeTab(TechRebornCreativeTab.instance);
	    	GameRegistry.registerBlock(thermalGenerator, "techreborn.thermalGenerator");
	    	GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGenerator");

	        quantumTank = new BlockQuantumTank().setBlockName("techreborn.quantumTank").setBlockTextureName("techreborn:quantumTank").setCreativeTab(TechRebornCreativeTab.instance);
	        GameRegistry.registerBlock(quantumTank, ItemBlockQuantumTank.class, "techreborn.quantumTank");
	        GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTank");

	        quantumChest = new BlockQuantumChest().setBlockName("techreborn.quantumChest").setBlockTextureName("techreborn:quantumChest").setCreativeTab(TechRebornCreativeTab.instance);
	        GameRegistry.registerBlock(quantumChest, ItemBlockQuantumChest.class, "techreborn.quantumChest");
	        GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChest");
        
	        ore = new BlockOre(Material.rock);
	        GameRegistry.registerBlock(ore, ItemBlockOre.class, "techreborn.ore");
			LogHelper.info("TechReborns Blocks Loaded");

	        registerOreDict();
	    }
	    
	    public static void registerOreDict()
	    {
	    	OreDictionary.registerOre("oreGalena", new ItemStack(ore,0));
	    	OreDictionary.registerOre("oreIridium", new ItemStack(ore,1));
	    	OreDictionary.registerOre("oreRuby", new ItemStack(ore,2));
	    	OreDictionary.registerOre("oreSapphire", new ItemStack(ore,3));
	    	OreDictionary.registerOre("oreBauxite", new ItemStack(ore,4));
	    	OreDictionary.registerOre("orePyrite", new ItemStack(ore,5));
	    	OreDictionary.registerOre("oreCinnabar", new ItemStack(ore,6));
	    	OreDictionary.registerOre("oreSphalerite", new ItemStack(ore,7));
	    	OreDictionary.registerOre("oreTungston", new ItemStack(ore,8));
	    	OreDictionary.registerOre("oreSheldonite", new ItemStack(ore,9));
	    	OreDictionary.registerOre("oreOlivine", new ItemStack(ore,10));
	    	OreDictionary.registerOre("oreSodalite", new ItemStack(ore,11));
	    }

}
