package techreborn.achievement;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.FMLCommonHandler;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

public class TRAchievements {

	public static AchievementPage techrebornPage;
	public static int pageIndex;

	public static Achievement ore_PickUp;
	public static Achievement thermalgen_Craft;
	public static Achievement centrifuge_Craft;

	public static void init() {
		ore_PickUp = new AchievementMod("ore_PickUp", 0, 0, new ItemStack(ModBlocks.ore, 1, 0), null);
		centrifuge_Craft = new AchievementMod("centrifuge_Craft", 1, 1, ModBlocks.centrifuge, ore_PickUp);
		thermalgen_Craft = new AchievementMod("thermalgen_Craft", 2, 1, ModBlocks.thermalGenerator, ore_PickUp);

		pageIndex = AchievementPage.getAchievementPages().size();
		techrebornPage = new AchievementPage(ModInfo.MOD_NAME,
			AchievementMod.achievements.toArray(new Achievement[AchievementMod.achievements.size()]));
		AchievementPage.registerAchievementPage(techrebornPage);

		FMLCommonHandler.instance().bus().register(new AchievementTriggerer());

	}

}
