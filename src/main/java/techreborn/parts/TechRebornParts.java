package techreborn.parts;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.mcmultipart.multipart.MultipartRegistry;
import techreborn.compat.ICompatModule;
import techreborn.parts.powerCables.CableMultipart;
import techreborn.parts.powerCables.EnumCableType;
import techreborn.parts.powerCables.ItemCables;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by modmuss50 on 02/03/2016.
 */
public class TechRebornParts implements ICompatModule {

	@Nullable
	public static Item cables;

	@Nullable
	public static Item fluidPipe;

	public static HashMap<EnumCableType, Class<? extends CableMultipart>> multipartHashMap = new HashMap<>();

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		for (EnumCableType cableType : EnumCableType.values()) {
			multipartHashMap.put(cableType, cableType.cableClass);
			MultipartRegistry.registerPart(cableType.cableClass, "techreborn:cable." + cableType.name());
		}
		cables = new ItemCables();
		cables.setRegistryName("cables");
		GameRegistry.register(cables);

		//		MultipartRegistry.registerPart(EmptyFluidPipe.class, "techreborn:fluidpipe.empty");
		//		MultipartRegistry.registerPart(InsertingFluidPipe.class, "techreborn:fluidpipe.inserting");
		//		MultipartRegistry.registerPart(ExtractingFluidPipe.class, "techreborn:fluidpipe.extracting");
		//		fluidPipe = new ItemFluidPipe();
		//		fluidPipe.setRegistryName("fluidPipe");
		//		GameRegistry.register(fluidPipe);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
