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

package techreborn.items.armor.jetpacks;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;

public class ItemJetpackT1 extends ItemArmor {

	public ItemJetpackT1() {
		super(ArmorMaterial.LEATHER, 7, EntityEquipmentSlot.FEET);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.jetpackt1");
		setMaxStackSize(1);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!player.onGround) {
			player.capabilities.allowFlying = true;
			if (player.motionY > -0.5D)
			{
				player.fallDistance = 1.0F;
			}
			Vec3d vec3d = player.getLookVec();
			float f = player.rotationPitch * 0.017453292F;
			double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
			double d8 = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
			double d1 = vec3d.lengthVector();
			float f4 = MathHelper.cos(f);
			f4 = (float)((double)f4 * (double)f4 * Math.min(1.0D, d1 / 0.4D));
			player.motionY += -0.08D + (double)f4 * 0.06D;

			if (player.motionY < 0.0D && d6 > 0.0D)
			{
				double d2 = player.motionY * -0.1D * (double)f4;
				player.motionY += d2;
				player.motionX += vec3d.x * d2 / d6;
				player.motionZ += vec3d.z * d2 / d6;
				player.move(MoverType.SELF, player.motionX, player.motionY, player.motionZ);
			}

			if (f < 0.0F)
			{
				double d10 = d8 * (double)(-MathHelper.sin(f)) * 0.04D;
				player.motionY += d10 * 3.2D;
				player.motionX -= vec3d.x * d10 / d6;
				player.motionZ -= vec3d.z * d10 / d6;
			}

			if (d6 > 0.0D)
			{
				player.motionX += (vec3d.x / d6 * d8 - player.motionX) * 0.1D;
				player.motionZ += (vec3d.z / d6 * d8 - player.motionZ) * 0.1D;
			}

			player.motionX *= 0.9900000095367432D;
			player.motionY *= 0.9800000190734863D;
			player.motionZ *= 0.9900000095367432D;
		}else{
			player.capabilities.allowFlying = false;
		}
	}

	}

