package techreborn.parts.walia;

import mcmultipart.block.TileMultipart;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import techreborn.compat.ICompatModule;

/**
 * Created by modmuss50 on 07/03/2016.
 */
public class WailaMcMultiPartCompact implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("Waila", "register", getClass().getName()
                + ".callbackRegister");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new WaliaPartProvider(),
                TileMultipart.class);
    }
}
