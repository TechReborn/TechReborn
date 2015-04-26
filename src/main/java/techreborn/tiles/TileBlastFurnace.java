package techreborn.tiles;

import ic2.api.energy.prefab.BasicSink;
import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;
import techreborn.lib.Location;
import techreborn.multiblocks.MultiBlockCasing;
import techreborn.util.Inventory;

public class TileBlastFurnace extends TileMachineBase implements IWrenchable {

	public int tickTime;
	public BasicSink energy;
	public Inventory inventory = new Inventory(3, "TileBlastFurnace", 64);

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
		return new ItemStack(ModBlocks.BlastFurnace, 1);
	}

    public int getHeat(){
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (tileEntity instanceof TileMachineCasing) {
                if((tileEntity.getBlockType() instanceof BlockMachineCasing)){
                    int heat;
                    heat = BlockMachineCasing.getHeatFromMeta(tileEntity.getBlockMetadata());
                        Location location = new Location(xCoord, yCoord, zCoord, direction);
                        location.modifyPositionFromSide(direction, 1);
                        if(worldObj.getBlock(location.getX(), location.getY(), location.getZ()).getUnlocalizedName().equals("tile.lava")){
                            heat += 500;
                        }
                    return heat;
                }
            }
        }
        return 0;
    }


}
