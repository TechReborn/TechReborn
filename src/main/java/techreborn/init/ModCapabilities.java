package techreborn.init;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import techreborn.api.armour.IArmourUpgrade;

import javax.annotation.Nullable;

public class ModCapabilities {

	public static void init(){
		CapabilityManager.INSTANCE.register(IArmourUpgrade.class, new Capability.IStorage<IArmourUpgrade>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<IArmourUpgrade> capability, IArmourUpgrade instance, EnumFacing side) {
				return null;
			}

			@Override
			public void readNBT(Capability<IArmourUpgrade> capability, IArmourUpgrade instance, EnumFacing side, NBTBase nbt) {

			}
		}, () -> {
			//TODO can I even do this, or do I NEED to have a defualt?
			throw new RuntimeException("No defualt implimentation advalible");
		});
	}

}
