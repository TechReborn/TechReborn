package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.api.recipe.RecipeCrafter;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

public class TileLathe extends TileMachineBase implements IWrenchable {

	public int tickTime;
	public BasicSink energy;
	public Inventory inventory = new Inventory(3, "TileLathe", 64);
	public RecipeCrafter crafter;
	
	public TileLathe()
	{
		//TODO configs
		energy = new BasicSink(this, 1000, 2);
		//Input slots
		int[] inputs = new int[1];
		inputs[0] = 0;
		int[] outputs = new int[1];
		outputs[0] = 2;
		crafter = new RecipeCrafter("latheRecipe", this, energy, 2, 2, inventory, inputs, outputs);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		energy.updateEntity();
		crafter.updateEntity();
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return false;
	}

	@Override
	public short getFacing()
	{
		return 0;
	}

	@Override
	public void setFacing(short facing)
	{
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return true;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.lathe, 1);
	}

	public boolean isComplete()
	{
		return false;
	}

}
