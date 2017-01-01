package techreborn.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Mark on 20/03/2016.
 */
public class ModSounds {

	public static SoundEvent CABLE_SHOCK;
	public static SoundEvent BLOCK_DISMANTLE;
	public static SoundEvent SAP_EXTRACT;

	public static void init() {
		CABLE_SHOCK = getSound("cable_shock");
		BLOCK_DISMANTLE = getSound("block_dismantle");
		SAP_EXTRACT = getSound("sap_extract");
	}

	private static SoundEvent getSound(String str) {
		ResourceLocation resourceLocation = new ResourceLocation("techreborn", str);
		return GameRegistry.register(new SoundEvent(resourceLocation).setRegistryName(resourceLocation));
	}

}
