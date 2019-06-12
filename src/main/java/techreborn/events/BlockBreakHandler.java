/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import techreborn.TechReborn;

@RebornRegister(TechReborn.MOD_ID)
public class BlockBreakHandler {

	@ConfigRegistry(config = "compat", category = "general", key = "secondaryGemDrops", comment = "Drop red and yellow garnets and peridot from any harvested oreRuby, oreSapphire, oreSphalerite. False will also disable drop from TechReborn ores.")
	public static boolean secondaryGemDrops = true;
	@ConfigRegistry(config = "misc", category = "blocks", key = "redGarnetDropChance", comment = "Chance to get Red Garnet from Ruby Ore")
	public static double redGarnetDropChance = 0.125;
	@ConfigRegistry(config = "misc", category = "blocks", key = "peridotDropChance", comment = "Chance to get Peridot from Sapphire Ore")
	public static double peridotDropChance = 0.125;
	@ConfigRegistry(config = "misc", category = "blocks", key = "aluminiumDropChance", comment = "Chance to get Aluminium dust from Sodalite Ore")
	public static double aluminiumDropChance = 0.50;
	@ConfigRegistry(config = "misc", category = "blocks", key = "redstoneDropChance", comment = "Chance to get Redstone from Cinnabar Ore")
	public static double redstoneDropChance = 0.25;
	@ConfigRegistry(config = "misc", category = "blocks", key = "yellowGarnetDropChance", comment = "Chance to get Yellow Garnet gem from Sphalerite Ore")
	public static double yellowGarnetDropChance = 0.125;

	//TODO 1.14
//	@SubscribeEvent
//	public void onBlockHarvest(BlockEvent.HarvestDropsEvent event) {
//		if (secondaryGemDrops && !event.isSilkTouching()) {
//			BlockState state = event.getState();
//			List<ItemStack> drops = event.getDrops();
//			Random random = new Random();
//		//TODO: fix tags
//			if (state.getBlock().matches(new BlockTags.CachingTag(new Identifier(TechReborn.MOD_ID, "ruby_ore")))) {
//				OreDrop redGarnet = new OreDrop(TRContent.Gems.RED_GARNET.getStack(), redGarnetDropChance, 1);
//				drops.add(redGarnet.getDrops(event.getFortuneLevel(), random));
//			}
//			else if (state.getBlock().matches(new BlockTags.CachingTag(new Identifier(TechReborn.MOD_ID, "sapphire_ore")))) {
//				OreDrop peridot = new OreDrop(TRContent.Gems.PERIDOT.getStack(), peridotDropChance, 1);
//				drops.add(peridot.getDrops(event.getFortuneLevel(), random));
//			}
//			else if (state.getBlock().matches(new BlockTags.CachingTag(new Identifier(TechReborn.MOD_ID, "sodalite_ore")))) {
//				OreDrop aluminium = new OreDrop(TRContent.Dusts.ALUMINUM.getStack(), aluminiumDropChance, 1);
//				drops.add(aluminium.getDrops(event.getFortuneLevel(), random));
//			}
//			else if (state.getBlock().matches(new BlockTags.CachingTag(new Identifier(TechReborn.MOD_ID, "cinnabar_ore")))) {
//				OreDrop redstone = new OreDrop(new ItemStack(Items.REDSTONE), redstoneDropChance, 1);
//				drops.add(redstone.getDrops(event.getFortuneLevel(), random));
//			}
//			else if (state.getBlock().matches(new BlockTags.CachingTag(new Identifier(TechReborn.MOD_ID, "sphalerite_ore")))) {
//				OreDrop yellowGarnet = new OreDrop(TRContent.Gems.YELLOW_GARNET.getStack(), yellowGarnetDropChance, 1);
//				drops.add(yellowGarnet.getDrops(event.getFortuneLevel(), random));
//			}
//		}
//	}
//
//	@SubscribeEvent
//	public void getBreakSpeedEvent(PlayerEvent.BreakSpeed event){
//		if(event.getEntityPlayer().getHeldItem(Hand.MAIN_HAND).getItem() == TRContent.INDUSTRIAL_CHAINSAW && event.getOriginalSpeed() > 1.0f){
//			BlockPos pos = event.getPos();
//			World worldIn = event.getEntityPlayer().world;
//			float speed = 20F;
//			int blocks = 0;
//			for (int i = 1; i < 10; i++) {
//				BlockPos nextPos = pos.up(i);
//				BlockState nextState = worldIn.getBlockState(nextPos);
//				if(nextState.getBlock().matches(BlockTags.LOGS)){
//					blocks ++;
//				}
//			}
//			event.setNewSpeed(speed / blocks);
//		}
//	}
}
