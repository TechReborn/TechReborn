package techreborn;

import techreborn.client.GuiHandler;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES)
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
