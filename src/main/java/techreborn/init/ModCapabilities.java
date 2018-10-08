package techreborn.init;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import techreborn.api.armor.IArmorUpgrade;

import javax.annotation.Nullable;

public class ModCapabilities {

	public static void init(){
		CapabilityManager.INSTANCE.register(IArmorUpgrade.class, new Capability.IStorage<IArmorUpgrade>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IArmorUpgrade> capability, IArmorUpgrade instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IArmorUpgrade> capability, IArmorUpgrade instance, EnumFacing side, NBTBase nbt) {

			}
		}, () -> {
			//TODO can I even do this, or do I NEED to have a defualt?
			throw new RuntimeException("No defualt implimentation advalible");
		});
	}

}
