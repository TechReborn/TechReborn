package techreborn.power;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PowerTickEvent extends Event {
	World world;
	public PowerTickEvent(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}
}
