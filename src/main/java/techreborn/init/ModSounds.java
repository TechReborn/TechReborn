package techreborn.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Mark on 20/03/2016.
 */
public class ModSounds {

	public static SoundEvent shock;
	public static SoundEvent dismantle;
	public static SoundEvent extract;

	public static void init() {
		shock = getSound("cable_shock");
		dismantle = getSound("block_dismantle");
		extract = getSound("sap_extract");
	}

	private static SoundEvent getSound(String str) {
		ResourceLocation resourceLocation = new ResourceLocation("techreborn", str);
		return GameRegistry.register(new SoundEvent(resourceLocation).setRegistryName(resourceLocation));
	}

}
