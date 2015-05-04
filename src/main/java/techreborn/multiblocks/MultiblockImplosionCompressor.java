package techreborn.multiblocks;

import erogenousbeef.coreTR.common.CoordTriplet;
import erogenousbeef.coreTR.multiblock.IMultiblockPart;
import erogenousbeef.coreTR.multiblock.MultiblockControllerBase;
import erogenousbeef.coreTR.multiblock.MultiblockValidationException;
import erogenousbeef.coreTR.multiblock.rectangular.RectangularMultiblockControllerBase;
import erogenousbeef.coreTR.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineCasing;
import techreborn.util.LogHelper;

/**
 * Created by mark on 04/05/15.
 */
public class MultiblockImplosionCompressor extends MultiBlockCasing {

	public MultiblockImplosionCompressor(World world) {
		super(world);
	}

	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part,
												 NBTTagCompound data) {

	}

	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
	}

	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {

	}

	@Override
	protected void onMachineAssembled() {
		LogHelper.warn("New multiblock created!");
	}

	@Override
	protected void onMachineRestored() {

	}

	@Override
	protected void onMachinePaused() {

	}

	@Override
	protected void onMachineDisassembled() {

	}

	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return 1;
	}

	@Override
	protected int getMaximumXSize() {
		return 3;
	}

	@Override
	protected int getMaximumZSize() {
		return 3;
	}

	@Override
	protected int getMaximumYSize() {
		return 3;
	}

	@Override
	protected int getMinimumXSize() {
		return 3;
	}

	@Override
	protected int getMinimumYSize() {
		return 3;
	}

	@Override
	protected int getMinimumZSize() {
		return 3;
	}

	@Override
	protected void onAssimilate(MultiblockControllerBase assimilated) {

	}

	@Override
	protected void onAssimilated(MultiblockControllerBase assimilator) {

	}

	@Override
	protected boolean updateServer() {
		return true;
	}

	@Override
	protected void updateClient() {

	}

	@Override
	public void writeToNBT(NBTTagCompound data) {

	}

	@Override
	public void readFromNBT(NBTTagCompound data) {

	}

	@Override
	public void formatDescriptionPacket(NBTTagCompound data) {

	}

	@Override
	public void decodeDescriptionPacket(NBTTagCompound data) {

	}

	/**
	 * @return True if the machine is "whole" and should be assembled. False
	 *         otherwise.
	 */
	@Override
	protected void isMachineWhole() throws MultiblockValidationException
	{
		if (connectedParts.size() < getMinimumNumberOfBlocksForAssembledMachine())
		{
			throw new MultiblockValidationException("Machine is too small.");
		}

		CoordTriplet maximumCoord = getMaximumCoord();
		CoordTriplet minimumCoord = getMinimumCoord();

		// Quickly check for exceeded dimensions
		int deltaX = maximumCoord.x - minimumCoord.x + 1;
		int deltaY = maximumCoord.y - minimumCoord.y + 1;
		int deltaZ = maximumCoord.z - minimumCoord.z + 1;

		int maxX = getMaximumXSize();
		int maxY = getMaximumYSize();
		int maxZ = getMaximumZSize();
		int minX = getMinimumXSize();
		int minY = getMinimumYSize();
		int minZ = getMinimumZSize();

		if (maxX > 0 && deltaX > maxX)
		{
			throw new MultiblockValidationException(
					String.format(
							"Machine is too large, it may be at most %d blocks in the X dimension",
							maxX));
		}
		if (maxY > 0 && deltaY > maxY)
		{
			throw new MultiblockValidationException(
					String.format(
							"Machine is too large, it may be at most %d blocks in the Y dimension",
							maxY));
		}
		if (maxZ > 0 && deltaZ > maxZ)
		{
			throw new MultiblockValidationException(
					String.format(
							"Machine is too large, it may be at most %d blocks in the Z dimension",
							maxZ));
		}
		if (deltaX < minX)
		{
			throw new MultiblockValidationException(
					String.format(
							"Machine is too small, it must be at least %d blocks in the X dimension",
							minX));
		}
		if (deltaY < minY)
		{
			throw new MultiblockValidationException(
					String.format(
							"Machine is too small, it must be at least %d blocks in the Y dimension",
							minY));
		}
		if (deltaZ < minZ)
		{
			throw new MultiblockValidationException(
					String.format(
							"Machine is too small, it must be at least %d blocks in the Z dimension",
							minZ));
		}

		// Now we run a simple check on each block within that volume.
		// Any block deviating = NO DEAL SIR
		TileEntity te;
		RectangularMultiblockTileEntityBase part;
		Class<? extends RectangularMultiblockControllerBase> myClass = this
				.getClass();

		//This is the bottom corner block
		te = this.worldObj.getTileEntity(minimumCoord.x, minimumCoord.y, minimumCoord.z);
		checkTeIsCorner(te);

		te = this.worldObj.getTileEntity(minimumCoord.x + 2, minimumCoord.y, minimumCoord.z);
		checkTeIsCorner(te);

		te = this.worldObj.getTileEntity(minimumCoord.x, minimumCoord.y, minimumCoord.z + 2);
		checkTeIsCorner(te);

		te = this.worldObj.getTileEntity(minimumCoord.x + 2, minimumCoord.y, minimumCoord.z + 2);
		checkTeIsCorner(te);

		te = this.worldObj.getTileEntity(minimumCoord.x, minimumCoord.y + 2, minimumCoord.z);
		checkTeIsCorner(te);

		te = this.worldObj.getTileEntity(minimumCoord.x + 2, minimumCoord.y + 2, minimumCoord.z);
		checkTeIsCorner(te);

		te = this.worldObj.getTileEntity(minimumCoord.x, minimumCoord.y + 2, minimumCoord.z + 2);
		checkTeIsCorner(te);

		te = this.worldObj.getTileEntity(minimumCoord.x + 2, minimumCoord.y + 2, minimumCoord.z + 2);
		checkTeIsCorner(te);

	}


	public void checkTeIsCorner(TileEntity te) throws MultiblockValidationException{
		if(!(te.blockType instanceof BlockMachineCasing && te.blockMetadata == 0)){
			throw new MultiblockValidationException(
					String.format(
							"Part @ %d, %d, %d is incompatible with machines of type %s",
							te.xCoord, te.yCoord, te.zCoord, "MultiBlock Implosion"));
		}
	}
}
