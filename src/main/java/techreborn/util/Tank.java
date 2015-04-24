package techreborn.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public class Tank extends FluidTank {

	private final String name;

	public Tank(String name, int capacity, TileEntity tile)
	{
		super(capacity);
		this.name = name;
		this.tile = tile;
	}

	public boolean isEmpty()
	{
		return getFluid() == null || getFluid().amount <= 0;
	}

	public boolean isFull()
	{
		return getFluid() != null && getFluid().amount >= getCapacity();
	}

	public Fluid getFluidType()
	{
		return getFluid() != null ? getFluid().getFluid() : null;
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound tankData = new NBTTagCompound();
		super.writeToNBT(tankData);
		nbt.setTag(name, tankData);
		return nbt;
	}

	@Override
	public final FluidTank readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey(name))
		{
			// allow to read empty tanks
			setFluid(null);

			NBTTagCompound tankData = nbt.getCompoundTag(name);
			super.readFromNBT(tankData);
		}
		return this;
	}

}
