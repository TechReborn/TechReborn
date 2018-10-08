package techreborn.items.armor.modular.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import techreborn.api.armour.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmourUprgade;
import techreborn.lib.ModInfo;

public class TestUpgrade extends BaseArmourUprgade {

	public TestUpgrade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "test"));
	}

	@Override
	public void tick(UpgradeHolder holder, EntityPlayer player) {
		System.out.println("Hello " + player.getDisplayNameString());
	}
}
