package techreborn;

import java.io.File;

import techreborn.client.GuiHandler;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.lib.ModInfo;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.MOD_VERSION, dependencies = ModInfo.MOD_DEPENDENCUIES, guiFactory = ModInfo.GUI_FACTORY_CLASS)
public class Core {
	public static ConfigTechReborn config;

    @Mod.Instance
    public static Core INSTANCE;
    
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	INSTANCE = this;
		String path = event.getSuggestedConfigurationFile().getAbsolutePath()
				.replace(ModInfo.MOD_ID, "TechReborn");

		config = ConfigTechReborn.initialize(new File(path));
    }

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
