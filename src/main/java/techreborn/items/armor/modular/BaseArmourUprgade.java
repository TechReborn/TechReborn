package techreborn.items.armor.modular;

import net.minecraft.util.ResourceLocation;
import techreborn.api.armour.IArmourUpgrade;

public abstract class BaseArmourUprgade implements IArmourUpgrade {

	ResourceLocation name;

	public BaseArmourUprgade(ResourceLocation name) {
		this.name = name;
	}

	@Override
	public ResourceLocation getName() {
		return name;
	}
}
