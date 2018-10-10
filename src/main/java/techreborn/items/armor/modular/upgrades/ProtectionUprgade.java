package techreborn.items.armor.modular.upgrades;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

public class ProtectionUprgade extends BaseArmorUprgade {
	public ProtectionUprgade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "protection"));
	}

	@Override
	public boolean hurt(UpgradeHolder holder, LivingHurtEvent event) {
		event.setAmount(event.getAmount() / 2);
		return false;
	}
}
