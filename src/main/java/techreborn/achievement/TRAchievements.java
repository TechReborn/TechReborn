package techreborn.achievement;

import cpw.mods.fml.common.FMLCommonHandler;
import techreborn.init.ModBlocks;
import techreborn.lib.AchievementNames;
import techreborn.lib.ModInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class TRAchievements {
	
	public static AchievementPage techrebornPage;
	public static int pageIndex;
	
	public static Achievement orePickUp;
	public static Achievement thermalgenCraft;
	public static Achievement centrifugeCraft;
	
	public static void init()
	{
		orePickUp = new AchievementMod(AchievementNames.ORE_PICKUP, 0, 0, new ItemStack(ModBlocks.ore,1, 0), null);
		centrifugeCraft = new AchievementMod(AchievementNames.CENTRIFUGE_CRAFT, 1, 1, new ItemStack(ModBlocks.centrifuge), orePickUp);
		thermalgenCraft = new AchievementMod("thermalgen_craft", 2, 1, new ItemStack(ModBlocks.thermalGenerator), null);

		pageIndex = AchievementPage.getAchievementPages().size();
		techrebornPage = new AchievementPage(ModInfo.MOD_NAME, AchievementMod.achievements.toArray(new Achievement[AchievementMod.achievements.size()]));	
		AchievementPage.registerAchievementPage(techrebornPage);

		FMLCommonHandler.instance().bus().register(new AchievementTriggerer());
	
	}

}
