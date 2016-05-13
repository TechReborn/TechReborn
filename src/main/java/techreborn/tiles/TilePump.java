package techreborn.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Tank;
import techreborn.config.ConfigTechReborn;

import java.util.List;

/**
 * Created by modmuss50 on 08/05/2016.
 */
public class TilePump extends TilePowerAcceptor implements IFluidHandler {

    public Tank tank = new Tank("TilePump", 10000, this);

    public TilePump() {
        super(1);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(!worldObj.isRemote && worldObj.getTotalWorldTime() % 10 == 0 && !tank.isFull() && tank.getCapacity() - tank.getFluidAmount() >= 1000 && canUseEnergy(ConfigTechReborn.pumpExtractEU)){
            FluidStack fluidStack = drainBlock(worldObj, pos.down(), false);
            if(fluidStack != null){
                tank.fill(drainBlock(worldObj, pos.down(), true), true);
                useEnergy(ConfigTechReborn.pumpExtractEU);
            }
            tank.compareAndUpdate();
        }
    }

    @Override
    public void addInfo(List<String> info, boolean isRealTile) {
        super.addInfo(info, isRealTile);
        info.add(TextFormatting.LIGHT_PURPLE + "Eu per extract " + TextFormatting.GREEN
                + PowerSystem.getLocaliszedPower(ConfigTechReborn.pumpExtractEU));
        info.add(TextFormatting.LIGHT_PURPLE + "Speed: " + TextFormatting.GREEN
                + "1000mb/5 sec");
    }

    public static FluidStack drainBlock(World world, BlockPos pos, boolean doDrain) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);

        if (fluid != null && FluidRegistry.isFluidRegistered(fluid)) {
            if (block instanceof IFluidBlock) {
                IFluidBlock fluidBlock = (IFluidBlock) block;
                if (!fluidBlock.canDrain(world, pos)) {
                    return null;
                }
                return fluidBlock.drain(world, pos, doDrain);
            } else {
                //Checks if source
                int level = state.getValue(BlockLiquid.LEVEL);
                if (level != 0) {
                    return null;
                }
                if (doDrain) {
                    world.setBlockToAir(pos);
                }
                return new FluidStack(fluid, 1000);
            }
        } else {
            return null;
        }
    }

    @Override
    public double getMaxPower() {
        return 10000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return false;
    }

    @Override
    public double getMaxOutput() {
        return 0;
    }

    @Override
    public double getMaxInput() {
        return 32;
    }

    @Override
    public EnumPowerTier getTier() {
        return EnumPowerTier.LOW;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        readFromNBTWithoutCoords(tagCompound);
    }

    public void readFromNBTWithoutCoords(NBTTagCompound tagCompound)
    {
        tank.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        writeToNBTWithoutCoords(tagCompound);
    }

    public void writeToNBTWithoutCoords(NBTTagCompound tagCompound)
    {
        tank.writeToNBT(tagCompound);
    }

    // IFluidHandler
    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int fill = tank.fill(resource, doFill);
        tank.compareAndUpdate();
        return fill;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        FluidStack drain = tank.drain(resource.amount, doDrain);
        tank.compareAndUpdate();
        return drain;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        FluidStack drain = tank.drain(maxDrain, doDrain);
        tank.compareAndUpdate();
        return drain;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return tank.getFluid() == null || tank.getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[] { tank.getInfo() };
    }
}
