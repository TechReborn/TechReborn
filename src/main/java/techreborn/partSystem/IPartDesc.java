package techreborn.partSystem;

import net.minecraft.nbt.NBTTagCompound;

public interface IPartDesc {

	public void readDesc(NBTTagCompound tagCompound);

	public void writeDesc(NBTTagCompound tagCompound);
}
