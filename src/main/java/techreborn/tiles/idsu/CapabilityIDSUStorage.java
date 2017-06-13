package techreborn.tiles.idsu;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class CapabilityIDSUStorage implements Capability.IStorage<IDataIDSU> {
	@Nullable
	@Override
	public NBTBase writeNBT(Capability<IDataIDSU> capability, IDataIDSU instance, EnumFacing side) {
		return new NBTTagDouble(instance.getStoredPower());
	}

	@Override
	public void readNBT(Capability<IDataIDSU> capability, IDataIDSU instance, EnumFacing side, NBTBase nbt) {
		instance.setStoredPower(((NBTTagDouble)nbt).getDouble());
	}
}
