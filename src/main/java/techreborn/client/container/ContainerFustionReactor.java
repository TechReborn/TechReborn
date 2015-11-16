package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.container.RebornContainer;
import techreborn.tiles.fusionReactor.TileEntityFustionController;


public class ContainerFustionReactor extends RebornContainer {
    public ContainerFustionReactor(TileEntityFustionController tileEntityFustionController,
                                   EntityPlayer player) {

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
