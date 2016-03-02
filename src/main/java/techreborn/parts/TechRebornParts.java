package techreborn.parts;

import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import techreborn.compat.ICompatModule;

/**
 * Created by mark on 02/03/2016.
 */
public class TechRebornParts implements ICompatModule {

    public static Item cables;

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        MultipartRegistry.registerPart(CableMultipart.class, "techreborn:cablepart");
        cables = new ItemCables();
        GameRegistry.registerItem(cables, "cables");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}
