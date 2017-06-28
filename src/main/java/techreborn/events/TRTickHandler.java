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

package techreborn.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import techreborn.init.ModItems;

public class TRTickHandler {

	public Item previouslyWearing;

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		Item chestslot = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != ItemStack.EMPTY
		                 ? player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() : null;

		if (previouslyWearing != chestslot && previouslyWearing == ModItems.CLOAKING_DEVICE && player.isInvisible()
			&& !player.isPotionActive(MobEffects.INVISIBILITY)) {
			player.setInvisible(false);
		}

		previouslyWearing = chestslot;
	}

}
