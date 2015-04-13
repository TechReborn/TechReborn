package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ic2.api.energy.prefab.BasicSink;
import techreborn.api.CentrifugeRecipie;
import techreborn.api.RollingMachineRecipie;
import techreborn.api.TechRebornAPI;
import techreborn.util.Inventory;

public class TileRollingMachine extends TileMachineBase {
	
	public BasicSink energy;
    public Inventory inventory = new Inventory(10, "TileRollingMachine", 64);

    public boolean isRunning;
    public int tickTime;
    public RollingMachineRecipie currentRecipe;
    
    public int euTick = 5;
    
    public TileRollingMachine()
    {
        energy = new BasicSink(this, 100000, 1);
    }
    
    @Override
    public void updateEntity() 
    {
    	super.updateEntity();
    	energy.updateEntity();
    }

}
