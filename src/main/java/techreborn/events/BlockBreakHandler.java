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

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.init.ModItems;
import techreborn.items.ItemGems;
import techreborn.lib.ModInfo;
import techreborn.utils.OreDictUtils;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class BlockBreakHandler {

	@ConfigRegistry(config = "misc", category = "general", key = "RubyRedGarnetDrops", comment = "Give red garnet drops to any harvested oreRuby")
	public static boolean rubyGarnetDrops = true;

	@SubscribeEvent
	public void onBlockHarvest(BlockEvent.HarvestDropsEvent event) {
		if (!event.isSilkTouching() && rubyGarnetDrops && OreDictUtils.isOre(event.getState(), "oreRuby")) {
			event.getDrops().add(ItemGems.getGemByName("red_garnet").copy());
		}
	}

	@SubscribeEvent
	public void getBreakSpeedEvent(PlayerEvent.BreakSpeed event){
		if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.ADVANCED_CHAINSAW){
			BlockPos pos = event.getPos();
			World worldIn = event.getEntityPlayer().world;
			float speed = 2F;
			int blocks = 0;
			for (int i = 1; i < 10; i++) {
				BlockPos nextPos = pos.up(i);
				IBlockState nextState = worldIn.getBlockState(nextPos);
				if(nextState.getBlock().isWood(worldIn, nextPos)){
					blocks ++;
				}
			}
			event.setNewSpeed(speed * blocks);
		}
	}
}
