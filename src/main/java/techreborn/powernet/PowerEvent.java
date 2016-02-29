package techreborn.powernet;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class PowerEvent extends Event {

    World world;

    public static List<PowerNetwork> networks = new ArrayList<>();

    public PowerEvent(World world) {
        this.world = world;
    }

    public PowerEvent() {
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event){
        MinecraftForge.EVENT_BUS.post(new PowerEvent(event.world));
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event){
        List<PowerNetwork> deadNetworks = new ArrayList<>();
        for(PowerNetwork network : networks){
            if(network.cables.size() != 0){
                if(network.cables.get(0).container.getWorld() == event.world){
                    MinecraftForge.EVENT_BUS.unregister(network);
                    deadNetworks.add(network);
                }
            } else {
                MinecraftForge.EVENT_BUS.unregister(network);
                deadNetworks.add(network);
            }
        }
        networks.removeAll(deadNetworks);
    }
}
