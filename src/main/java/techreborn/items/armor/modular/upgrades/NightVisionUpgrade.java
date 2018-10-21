package techreborn.items.armor.modular.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.api.armor.ArmorSlot;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

import java.util.Collections;
import java.util.List;

@RebornRegistry
public class NightVisionUpgrade extends BaseArmorUprgade {

	@ConfigRegistry(config = "upgrades", category = "night_vision", key = "power_usage", comment = "The amont of power to each tick when using the upgrade")
	public static int powerUsage = 16;

	public NightVisionUpgrade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "night_vision"));
	}

	@Override
	public void tick(UpgradeHolder holder, EntityPlayer player) {
		if(canUsePower(holder, powerUsage)){
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 60));
			usePower(holder, powerUsage);
		}
	}

	@Override
	public List<ArmorSlot> getValidSlots() {
		return Collections.singletonList(ArmorSlot.HEAD);
	}

	@Override
	public boolean allowMultiple() {
		return false;
	}
}
