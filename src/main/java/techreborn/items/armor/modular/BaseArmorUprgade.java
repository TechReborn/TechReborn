package techreborn.items.armor.modular;

import net.minecraft.util.ResourceLocation;
import techreborn.api.armor.IArmorUpgrade;

public abstract class BaseArmorUprgade implements IArmorUpgrade {

	ResourceLocation name;

	public BaseArmorUprgade(ResourceLocation name) {
		this.name = name;
	}

	@Override
	public ResourceLocation getName() {
		return name;
	}
}
