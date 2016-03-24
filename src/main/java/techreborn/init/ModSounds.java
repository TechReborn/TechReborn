package techreborn.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Created by Mark on 20/03/2016.
 */
public class ModSounds
{

	public static SoundEvent shock;

	public static void init()
	{
		shock = getSound("cable_shock");
	}

	private static SoundEvent getSound(String str)
	{
		SoundEvent soundEvent = SoundEvent.soundEventRegistry.getObject(new ResourceLocation("techreborn" + str));
		return soundEvent;
	}

}
