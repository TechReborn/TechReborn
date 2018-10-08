package techreborn.items.armor.modular.upgrades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import techreborn.api.armor.UpgradeHolder;
import techreborn.items.armor.modular.BaseArmorUprgade;
import techreborn.lib.ModInfo;

public class TestUpgrade extends BaseArmorUprgade {

	public TestUpgrade() {
		super(new ResourceLocation(ModInfo.MOD_ID, "test"));
	}

	@Override
	public void tick(UpgradeHolder holder, EntityPlayer player) {
		System.out.println("Hello " + player.getDisplayNameString());
	}
}
