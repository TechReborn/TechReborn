package techreborn.tiles.idsu;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 13/06/2017.
 */
public class IDSUSaveManger extends WorldSavedData implements IDataIDSU {
	public IDSUSaveManger(String name) {
		super(ModInfo.MOD_ID + "_IDSU");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		power = nbt.getDouble("power");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setDouble("power", power);
		return compound;
	}

	@Override
	public boolean isDirty()
	{
		return true;
	}

	double power;

	@Override
	public double getStoredPower() {
		return power;
	}

	@Override
	public void setStoredPower(double storedPower) {
		power = storedPower;
	}
}
