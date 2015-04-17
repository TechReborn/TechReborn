package techreborn.multiblocks;

import erogenousbeef.coreTR.multiblock.IMultiblockPart;
import erogenousbeef.coreTR.multiblock.MultiblockControllerBase;
import erogenousbeef.coreTR.multiblock.MultiblockValidationException;
import erogenousbeef.coreTR.multiblock.rectangular.RectangularMultiblockControllerBase;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import techreborn.util.LogHelper;

public class MultiBlockCasing extends RectangularMultiblockControllerBase {

    public MultiBlockCasing(World world) {
        super(world);
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
        return 4;
    }

    @Override
    protected int getMinimumXSize() {
        return 3;
    }

    @Override
    protected int getMinimumYSize() {
        return 4;
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
        Block block = world.getBlock(x, y, z);
        if(block.getUnlocalizedName().equals("tile.lava") || block.getUnlocalizedName().equals("tile.air")){
        } else {
            super.isBlockGoodForInterior(world, x, y, z);
        }
    }
}
