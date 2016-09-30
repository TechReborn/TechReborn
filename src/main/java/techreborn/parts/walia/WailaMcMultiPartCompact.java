package techreborn.parts.walia;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.*;
import reborncore.mcmultipart.block.TileMultipartContainer;
import techreborn.compat.ICompatModule;

/**
 * Created by modmuss50 on 07/03/2016.
 */
public class WailaMcMultiPartCompact implements ICompatModule {
	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new WaliaPartProvider(), TileMultipartContainer.class);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		FMLInterModComms.sendMessage("Waila", "register", getClass().getName() + ".callbackRegister");
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
