package techreborn.parts;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import techreborn.compat.ICompatModule;
import techreborn.parts.powerCables.ItemStandaloneCables;

/**
 * Created by modmuss50 on 06/03/2016.
 */
public class StandalonePartCompact implements ICompatModule {

	public static ItemStandaloneCables itemStandaloneCable;

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		itemStandaloneCable = new ItemStandaloneCables();
		GameRegistry.registerItem(itemStandaloneCable, "cables");
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
