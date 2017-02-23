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

package techreborn.tiles;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import techreborn.blocks.BlockFlare;
import techreborn.client.particle.ParticleSmoke;

import java.util.Random;

/**
 * Created by modmuss50 on 06/11/2016.
 */
public class TileEntityFlare extends TileEntity implements ITickable {

	Random random = new Random();

	@Override
	public void update() {
		EnumDyeColor color = world.getBlockState(pos).getValue(BlockFlare.COLOR);
		if (world.isRemote && world.isAirBlock(getPos().up())) {
			ParticleSmoke particleSmokeLarge = new ParticleSmoke(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0.0D, 0);
			particleSmokeLarge.setMaxAge(250);
			if (color != EnumDyeColor.WHITE) {
				float[] rgb = EntitySheep.getDyeRgb(color);
				particleSmokeLarge.setRBGColorF(rgb[0] + (random.nextFloat() / 20), rgb[1] + (random.nextFloat() / 20), rgb[2] + (random.nextFloat() / 20));
			}
			particleSmokeLarge.multipleParticleScaleBy(0.5F);

			Minecraft.getMinecraft().effectRenderer.addEffect(particleSmokeLarge);

			world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}
	}

}
