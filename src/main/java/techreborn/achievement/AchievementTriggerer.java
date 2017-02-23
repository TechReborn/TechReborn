/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.achievement;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import reborncore.common.achievement.ICraftAchievement;
import reborncore.common.achievement.IPickupAchievement;

public class AchievementTriggerer {

	@SubscribeEvent
	public void onItemPickedUp(ItemPickupEvent event) {
		ItemStack stack = event.pickedUp.getEntityItem();
		if (stack != null && stack.getItem() instanceof IPickupAchievement) {
			Achievement achievement = ((IPickupAchievement) stack.getItem()).getAchievementOnPickup(stack, event.player,
				event.pickedUp);
			if (achievement != null)
				event.player.addStat(achievement, 1);
		}
	}

	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event) {
		if (event.crafting != null && event.crafting.getItem() instanceof ICraftAchievement) {
			Achievement achievement = ((ICraftAchievement) event.crafting.getItem())
				.getAchievementOnCraft(event.crafting, event.player, event.craftMatrix);
			if (achievement != null)
				event.player.addStat(achievement, 1);
		}
	}

}
