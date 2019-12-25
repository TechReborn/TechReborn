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

package techreborn.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.Energy;

import java.util.Random;

/**
 * @author drcrazy
 *
 */

public class ToolsUtil {
    public static void breakBlock(ItemStack tool, World world, BlockPos pos, LivingEntity entityLiving, int cost) {
        if (!(entityLiving instanceof PlayerEntity)) {
            return;
        }
        Random rand = new Random();
        if (rand.nextInt(EnchantmentHelper.getLevel(Enchantments.UNBREAKING, tool) + 1) == 0) {
            if (!Energy.of(tool).use(cost)) {
                return;
            }
        }
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getHardness(world, pos) == -1.0F) {
            return;
        }

        blockState.getBlock().afterBreak(world, (PlayerEntity) entityLiving, pos, blockState, world.getBlockEntity(pos), tool);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        world.removeBlockEntity(pos);
    }
}
