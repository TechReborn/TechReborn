package techreborn.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.tileentity.TileEntity;
import techreborn.api.multiblock.IMultiBlockController;
import techreborn.api.multiblock.IMultiblockComponent;

public class MultiblockEvent {

    @SubscribeEvent
    public void blockBreakEvent(net.minecraftforge.event.world.BlockEvent.BreakEvent event) {
        if (event.world.getTileEntity(event.x, event.y, event.z) instanceof IMultiblockComponent) {
            IMultiblockComponent component = (IMultiblockComponent) event.world.getTileEntity(event.x, event.y, event.z);
            for (int x = -3; x < 3; x++) {
                for (int y = -3; y < 3; y++) {
                    for (int z = -3; z < 3; z++) {
                        //TODO have a quicker way of doing this, Arraylist with all controllers?
                        TileEntity tile = event.world.getTileEntity(x + event.x, y + event.y, z + event.z);
                        if (tile != null && tile instanceof IMultiBlockController) {
                            IMultiBlockController controller = (IMultiBlockController) tile;
                            controller.getMultiBlock().recompute();
                            System.out.println(controller.getMultiBlock().isComplete());
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent(priority =)
    public void blockPlaceEvent(net.minecraftforge.event.world.BlockEvent.PlaceEvent event) {
        if (event.world.getTileEntity(event.x, event.y, event.z) instanceof IMultiblockComponent) {
            IMultiblockComponent component = (IMultiblockComponent) event.world.getTileEntity(event.x, event.y, event.z);
            for (int x = -3; x < 3; x++) {
                for (int y = -3; y < 3; y++) {
                    for (int z = -3; z < 3; z++) {
                        //TODO have a quicker way of doing this
                        TileEntity tile = event.world.getTileEntity(x + event.x, y + event.y, z + event.z);
                        if (tile != null && tile instanceof IMultiBlockController) {
                            IMultiBlockController controller = (IMultiBlockController) tile;
                            controller.getMultiBlock().recompute();
                            System.out.println(controller.getMultiBlock().isComplete());
                        }
                    }
                }
            }
        }
    }
}

