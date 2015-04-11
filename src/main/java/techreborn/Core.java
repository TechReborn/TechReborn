package techreborn;

import net.minecraft.block.Block;
import techreborn.blocks.BlockQuantumChest;
import techreborn.blocks.BlockQuantumTank;
import techreborn.blocks.BlockThermalGenerator;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileQuantumTank;
import net.minecraft.item.Item;
import techreborn.items.ItemDusts;
import techreborn.tiles.TileThermalGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "techreborn", name = "TechReborn", version = "@MODVERSION@")
public class Core {

    public static Block thermalGenerator;
    public static Block quantumTank;
    public static Block quantumChest;

    public static Item dusts;


    @Mod.Instance
    public static Core INSTANCE;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event){
        thermalGenerator = new BlockThermalGenerator().setBlockName("techreborn.thermalGenerator").setBlockTextureName("techreborn:ThermalGenerator_other").setCreativeTab(TechRebornCreativeTab.instance);
        GameRegistry.registerBlock(thermalGenerator, "techreborn.thermalGenerator");
        GameRegistry.registerTileEntity(TileThermalGenerator.class, "TileThermalGenerator");

        quantumTank = new BlockQuantumTank().setBlockName("techreborn.quantumTank").setBlockTextureName("techreborn:quantumTank").setCreativeTab(TechRebornCreativeTab.instance);
        GameRegistry.registerBlock(quantumTank, "techreborn.quantumTank");
        GameRegistry.registerTileEntity(TileQuantumTank.class, "TileQuantumTank");

        quantumChest = new BlockQuantumChest().setBlockName("techreborn.quantumChest").setBlockTextureName("techreborn:quantumChest").setCreativeTab(TechRebornCreativeTab.instance);
        GameRegistry.registerBlock(quantumChest, "techreborn.quantumChest");
        GameRegistry.registerTileEntity(TileQuantumChest.class, "TileQuantumChest");

        dusts = new ItemDusts();
        GameRegistry.registerItem(dusts, "dust");

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
    }

}
