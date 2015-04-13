package techreborn.client.container;

import techreborn.client.SlotOutput;
import techreborn.tiles.TileCentrifuge;
import techreborn.tiles.TileRollingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerRollingMachine extends TechRebornContainer{
	
	EntityPlayer player;
	TileRollingMachine tile;
	
	  public ContainerRollingMachine(TileRollingMachine tileRollingmachine, EntityPlayer player){
	        tile = tileRollingmachine;
	        this.player = player;

	        //input
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 0, 30, 17));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 1, 30, 35));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 2, 30, 53));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 3, 48, 17));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 4, 48, 35));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 5, 48, 53));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 6, 66, 17));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 7, 66, 35));
	        this.addSlotToContainer(new Slot(tileRollingmachine.inventory, 8, 66, 53));
	        //outputs
	        this.addSlotToContainer(new SlotOutput(tileRollingmachine.inventory, 9, 124, 35));


	        int i;

	        for (i = 0; i < 3; ++i) {
	            for (int j = 0; j < 9; ++j) {
	                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	            }
	        }

	        for (i = 0; i < 9; ++i) {
	            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
	        }
	    }

	    @Override
	    public boolean canInteractWith(EntityPlayer player) {
	        return true;
	    }

}
