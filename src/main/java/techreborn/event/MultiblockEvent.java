package techreborn.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import techreborn.api.multiblock.IMultiblockComponent;

public class MultiblockEvent {

    @SubscribeEvent
    public void blockBreakEvent(net.minecraftforge.event.world.BlockEvent.BreakEvent event){
        if(event.block instanceof IMultiblockComponent){
            IMultiblockComponent component = (IMultiblockComponent) event.block;
            component.getMultiblock(event.world.getBlockMetadata(event.x, event.y, event.z)).recompute();
        }
    }

    @SubscribeEvent
    public void blockPlaceEvent(net.minecraftforge.event.world.BlockEvent.PlaceEvent event){
        if(event.block instanceof  IMultiblockComponent){
            IMultiblockComponent component = (IMultiblockComponent) event.block;
            component.getMultiblock(event.world.getBlockMetadata(event.x, event.y, event.z)).recompute();
        }
    }
}
