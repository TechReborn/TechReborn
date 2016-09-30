package techreborn.achievement;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import java.util.ArrayList;
import java.util.List;

public class AchievementMod extends Achievement {

	public static List<Achievement> achievements = new ArrayList();

	public AchievementMod(String name, int x, int y, ItemStack icon, Achievement parent) {
		super("achievement.techreborn:" + name, "TechReborn:" + name, x, y, icon, parent);
		achievements.add(this);
		registerStat();
	}

	public AchievementMod(String name, int x, int y, Item icon, Achievement parent) {
		this(name, x, y, new ItemStack(icon), parent);
	}

	public AchievementMod(String name, int x, int y, Block icon, Achievement parent) {
		this(name, x, y, new ItemStack(icon), parent);
	}

}
