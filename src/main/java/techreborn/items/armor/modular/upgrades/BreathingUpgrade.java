package techreborn.items.armor.modular.upgrades;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
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
public class BreathingUpgrade extends BaseArmorUprgade {

	@ConfigRegistry(config = "upgrades", category = "water_breathing", key = "power_usage", comment = "The amont of power to use when breathing underwater")
	public static int powerUsage = 16;

	public BreathingUpgrade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "breathing"));
	}

	@Override
	public void tick(UpgradeHolder holder, EntityPlayer player) {
		if(player.isInsideOfMaterial(Material.WATER) && canUsePower(holder, powerUsage)){
			usePower(holder, powerUsage);
			player.setAir(300);
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
