package techreborn.items.armor.modular.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

public class TestUpgrade extends BaseArmorUprgade {

	public TestUpgrade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "test"));
	}

	@Override
	public void tick(UpgradeHolder holder, EntityPlayer player) {
		//System.out.println("Hello " + player.getDisplayNameString());
	}

	@Override
	public boolean hurt(UpgradeHolder holder, LivingHurtEvent event) {
		//Note fall dame is not blocked by armor so it needs to be handled sepratly
		if(!event.getSource().isUnblockable()){
			event.setAmount(event.getAmount() / 8); //Reduces standard damage by 8
		} else if (event.getSource() == DamageSource.FALL){
			event.setCanceled(true); // Fall damage protection
		}
		return false;
	}

	@Override
	public double getSpeed(UpgradeHolder holder) {
		return 2F;
	}
}
