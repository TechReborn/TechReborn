package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techreborn.api.multiblock.IMultiBlock;
import techreborn.api.multiblock.MultiBlockController;
import techreborn.init.ModBlocks;
import techreborn.util.Inventory;

public class TileBlastFurnace extends MultiBlockController implements IWrenchable {

	public int tickTime;
	public BasicSink energy;
    public Inventory inventory = new Inventory(3, "TileBlastFurnace", 64);

	public TileBlastFurnace(IMultiBlock multiBlock) {
		super(multiBlock);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return false;
	}

	@Override
	public short getFacing() {
		return 0;
	}

	@Override
	public void setFacing(short facing) {
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.BlastFurnace, 1);
	}

}
