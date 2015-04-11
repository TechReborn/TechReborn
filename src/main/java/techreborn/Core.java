package techreborn;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockQuantumChest;
import techreborn.blocks.BlockQuantumTank;
import techreborn.blocks.BlockThermalGenerator;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileQuantumChest;
import techreborn.tiles.TileQuantumTank;
import net.minecraft.item.Item;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.itemblocks.ItemBlockOre;
import techreborn.items.ItemDusts;
import techreborn.tiles.TileThermalGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "techreborn", name = "TechReborn", version = "@MODVERSION@")
public class Core {

    @Mod.Instance
    public static Core INSTANCE;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event)
    {
    	//Register ModBlocks
    	ModBlocks.init();
    	//Register ModItems
    	ModItems.init();
    	//Register Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
    }

}
