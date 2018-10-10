package techreborn.items.armor.modular.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import techreborn.api.armor.ArmorSlot;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

import java.util.Collections;
import java.util.List;

public class FlightUpgrade extends BaseArmorUprgade {
	public FlightUpgrade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "flight"));
	}

	@Override
	public boolean allowMultiple() {
		return false;
	}

	@Override
	public List<ArmorSlot> getValidSlots() {
		return Collections.singletonList(ArmorSlot.CHEST);
	}

	@Override
	public void equip(UpgradeHolder holder, EntityPlayer player) {
		player.capabilities.allowFlying = true;
	}

	@Override
	public void unequip(UpgradeHolder holder, EntityPlayer player) {
		if(!player.isCreative()){
			player.capabilities.allowFlying = false;
			player.capabilities.isFlying = false;
		}
	}
}
