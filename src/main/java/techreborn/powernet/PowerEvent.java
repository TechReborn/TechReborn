package techreborn.powernet;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PowerEvent extends Event {

    World world;

    public PowerEvent(World world) {
        this.world = world;
    }

    public PowerEvent() {
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event){
        MinecraftForge.EVENT_BUS.post(new PowerEvent(event.world));
    }
}
