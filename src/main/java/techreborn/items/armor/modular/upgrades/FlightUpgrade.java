package techreborn.items.armor.modular.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techreborn.api.armor.ArmorSlot;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

import java.util.Collections;
import java.util.List;

public class FlightUpgrade extends BaseArmorUprgade {

	int powerUage = 1000;

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
		if(player.isCreative()){
			return;
		}
		if(canUsePower(holder, powerUage)){
			startFlying(player);
		}
	}

	@Override
	public void unequip(UpgradeHolder holder, EntityPlayer player) {
		if(player.isCreative()){
			return;
		}
		stopFlying(player);
	}

	@Override
	public void tick(UpgradeHolder holder, EntityPlayer player) {
		if(player.isCreative()){
			return;
		}
		if(!canUsePower(holder, powerUage)){
			stopFlying(player);
		} else if(!player.capabilities.allowFlying) {
			startFlying(player);
		}
		if(player.capabilities.isFlying){
			holder.getArmorManager().getEnergyStorage().extractEnergy(powerUage, false);
		}
	}

	@Override
	public boolean hurt(UpgradeHolder holder, LivingHurtEvent event) {
		EntityPlayer player = (EntityPlayer) event.getEntity();
		if(player.isCreative()){
			return false;
		}
		if(canUsePower(holder, powerUage) && player.capabilities.allowFlying){
			return event.getSource() == DamageSource.FALL;
		}
		return false;
	}

	public void startFlying(EntityPlayer player){
		player.capabilities.allowFlying = true;
	}

	public void stopFlying(EntityPlayer player){
		if(player.isCreative()){
			return;
		}
		player.capabilities.allowFlying = false;
		player.capabilities.isFlying = false;
	}
}
