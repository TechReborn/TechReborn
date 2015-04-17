package techreborn.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import techreborn.api.multiblock.IMultiblockComponent;

public class MultiblockEvent {

    @SubscribeEvent
    public void blockBreakEvent(net.minecraftforge.event.world.BlockEvent.BreakEvent event){
        if(event.world.getTileEntity(event.x, event.y, event.z) instanceof IMultiblockComponent){
            IMultiblockComponent component = (IMultiblockComponent) event.world.getTileEntity(event.x, event.y, event.z);
//            if(component.getMultiblock() != null)
//                component.getMultiblock().recompute();
            //TODO find all multiblock controllers fo the same type that are near this location and recompute.
        }
    }

    @SubscribeEvent
    public void blockPlaceEvent(net.minecraftforge.event.world.BlockEvent.PlaceEvent event){
        if(event.world.getTileEntity(event.x, event.y, event.z) instanceof IMultiblockComponent){
            IMultiblockComponent component = (IMultiblockComponent) event.world.getTileEntity(event.x, event.y, event.z);
//            if(component.getMultiblock() != null)
//                component.getMultiblock().recompute();
        }
    }
}
