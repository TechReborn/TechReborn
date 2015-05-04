package techreborn.tiles;

import codechicken.multipart.MultiPartRegistry;
import erogenousbeef.coreTR.multiblock.MultiblockRegistry;
import erogenousbeef.coreTR.multiblock.MultiblockWorldRegistry;
import techreborn.multiblocks.MultiBlockCasing;
import erogenousbeef.coreTR.multiblock.MultiblockControllerBase;
import erogenousbeef.coreTR.multiblock.MultiblockValidationException;
import erogenousbeef.coreTR.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import techreborn.multiblocks.MultiblockImplosionCompressor;

public class TileMachineCasing extends RectangularMultiblockTileEntityBase {

	int ticks;
	boolean isStarShape = false;

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void onMachineActivated()
	{

	}

	@Override
	public void onMachineDeactivated()
	{

	}

	@Override
	public MultiblockControllerBase createNewMultiblock()
	{
		if(isStarShape)
		{
			return new MultiblockImplosionCompressor(worldObj);
		}
		return new MultiBlockCasing(worldObj);
	}

	@Override
	public Class<? extends MultiblockControllerBase> getMultiblockControllerType()
	{
		if(isStarShape)
		{
			return MultiblockImplosionCompressor.class;
		}
		return MultiBlockCasing.class;
	}

	@Override
	public void isGoodForFrame() throws MultiblockValidationException
	{

	}

	@Override
	public void isGoodForSides() throws MultiblockValidationException
	{

	}

	@Override
	public void isGoodForTop() throws MultiblockValidationException
	{

	}

	@Override
	public void isGoodForBottom() throws MultiblockValidationException
	{

	}

	@Override
	public void isGoodForInterior() throws MultiblockValidationException
	{

	}

	public MultiBlockCasing getMultiblockController()
	{
		return (MultiBlockCasing) super.getMultiblockController();
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(getMultiblockController() == null || !getMultiblockController().isAssembled()){
			ticks ++;
			if(ticks == 20){
				isStarShape = !isStarShape;
				ticks = 0;
				//TODO recalculate multiblock
			}
		}
	}
}
