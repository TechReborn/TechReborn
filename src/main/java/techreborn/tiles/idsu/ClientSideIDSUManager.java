package techreborn.tiles.idsu;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.event.world.WorldEvent;

@SideOnly(Side.CLIENT)
public class ClientSideIDSUManager {

	public static IDSUManager CLIENT = new IDSUManager();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void worldLoad(WorldEvent.Load event) {
		CLIENT = new IDSUManager();
	}

}
