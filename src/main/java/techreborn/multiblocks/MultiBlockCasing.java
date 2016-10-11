package techreborn.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.multiblock.CoordTriplet;
import reborncore.common.multiblock.IMultiblockPart;
import reborncore.common.multiblock.MultiblockControllerBase;
import reborncore.common.multiblock.MultiblockValidationException;
import reborncore.common.multiblock.rectangular.RectangularMultiblockControllerBase;
import reborncore.common.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import techreborn.init.ModBlocks;

public class MultiBlockCasing extends RectangularMultiblockControllerBase {

	public boolean hasLava;
	public boolean isStar = false;
	public int height = 0;

	public MultiBlockCasing(World world) {
		super(world);
	}

	public String getInfo() {
		String value = "Intact";
		try {
			isMachineWhole();
		} catch (MultiblockValidationException e) {
			e.printStackTrace();
			value = e.getLocalizedMessage();
		}
		return value;
	}

	/**
	 * @return True if the machine is "whole" and should be assembled. False
	 * otherwise.
	 */
	@Override
	protected void isMachineWhole() throws MultiblockValidationException {
		if (connectedParts.size() < getMinimumNumberOfBlocksForAssembledMachine()) {
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

		if (maxX > 0 && deltaX > maxX) {
			throw new MultiblockValidationException(
				String.format("Machine is too large, it may be at most %d blocks in the X dimension", maxX));
		}
		if (maxY > 0 && deltaY > maxY) {
			throw new MultiblockValidationException(
				String.format("Machine is too large, it may be at most %d blocks in the Y dimension", maxY));
		}
		if (maxZ > 0 && deltaZ > maxZ) {
			throw new MultiblockValidationException(
				String.format("Machine is too large, it may be at most %d blocks in the Z dimension", maxZ));
		}
		if (deltaX < minX) {
			throw new MultiblockValidationException(
				String.format("Machine is too small, it must be at least %d blocks in the X dimension", minX));
		}
		if (deltaY < minY) {
			throw new MultiblockValidationException(
				String.format("Machine is too small, it must be at least %d blocks in the Y dimension", minY));
		}
		if (deltaZ < minZ) {
			throw new MultiblockValidationException(
				String.format("Machine is too small, it must be at least %d blocks in the Z dimension", minZ));
		}
		height = deltaY;

		// if(checkIfStarShape(minimumCoord.x, minimumCoord.y, minimumCoord.z)){
		// isStar = true;
		// return;
		// } else {
		// isStar = false;
		// }

		if (deltaY < minY) {
			throw new MultiblockValidationException(
				String.format("Machine is too small, it must be at least %d blocks in the Y dimension", minY));
		}

		// Now we run a simple check on each block within that volume.
		// Any block deviating = NO DEAL SIR
		TileEntity te;
		RectangularMultiblockTileEntityBase part;
		Class<? extends RectangularMultiblockControllerBase> myClass = this.getClass();

		for (int x = minimumCoord.x; x <= maximumCoord.x; x++) {
			for (int y = minimumCoord.y; y <= maximumCoord.y; y++) {
				for (int z = minimumCoord.z; z <= maximumCoord.z; z++) {
					// Okay, figure out what sort of block this should be.

					te = this.worldObj.getTileEntity(new BlockPos(x, y, z));
					if (te instanceof RectangularMultiblockTileEntityBase) {
						part = (RectangularMultiblockTileEntityBase) te;

						// Ensure this part should actually be allowed within a
						// cube of this controller's type
						if (!myClass.equals(part.getMultiblockControllerType())) {
							throw new MultiblockValidationException(
								String.format("Part @ %d, %d, %d is incompatible with machines of type %s", x, y, z,
									myClass.getSimpleName()));
						}
					} else {
						// This is permitted so that we can incorporate certain
						// non-multiblock parts inside interiors
						part = null;
					}

					// Validate block type against both part-level and
					// material-level validators.
					int extremes = 0;
					if (x == minimumCoord.x) {
						extremes++;
					}
					if (y == minimumCoord.y) {
						extremes++;
					}
					if (z == minimumCoord.z) {
						extremes++;
					}

					if (x == maximumCoord.x) {
						extremes++;
					}
					if (y == maximumCoord.y) {
						extremes++;
					}
					if (z == maximumCoord.z) {
						extremes++;
					}

					if (extremes >= 2) {
						if (part != null) {
							part.isGoodForFrame();
						} else {
							isBlockGoodForFrame(this.worldObj, x, y, z);
						}
					} else if (extremes == 1) {
						if (y == maximumCoord.y) {
							if (part != null) {
								part.isGoodForTop();
							} else {
								isBlockGoodForTop(this.worldObj, x, y, z);
							}
						} else if (y == minimumCoord.y) {
							if (part != null) {
								part.isGoodForBottom();
							} else {
								isBlockGoodForBottom(this.worldObj, x, y, z);
							}
						} else {
							// Side
							if (part != null) {
								part.isGoodForSides();
							} else {
								isBlockGoodForSides(this.worldObj, x, y, z);
							}
						}
					} else {
						if (part != null) {
							part.isGoodForInterior();
						} else {
							isBlockGoodForInterior(this.worldObj, x, y, z);
						}
					}
				}
			}
		}
	}

	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {

	}

	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
	}

	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {

	}

	@Override
	protected void onMachineAssembled() {

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
		return 4;
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

	@Override
	protected void isBlockGoodForInterior(World world, int x, int y, int z) throws MultiblockValidationException {
		Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();

		System.out.println(block);
		if (block.isAir(world.getBlockState(new BlockPos(x, y, z)), world, new BlockPos(x, y, z))) {

		} else if (block.getUnlocalizedName().equals("tile.lava")) {
			hasLava = true;
		} else {
			super.isBlockGoodForInterior(world, x, y, z);
		}
	}

	@Override
	protected void isBlockGoodForFrame(World world, int x, int y, int z) throws MultiblockValidationException {
		Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
		if (block == ModBlocks.machineCasing) {

		} else {
			super.isBlockGoodForFrame(world, x, y, z);
		}
	}

}