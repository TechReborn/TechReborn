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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import techreborn.client.TechRebornCreativeTab;

public class ItemJetpackT1 extends ItemArmor {

	private  boolean inFlight = false;
	private float fallDist = 0;
	public ItemJetpackT1() {
		super(ArmorMaterial.LEATHER, 7, EntityEquipmentSlot.FEET);
		setCreativeTab(TechRebornCreativeTab.instance);
		setUnlocalizedName("techreborn.jetpackt1");
		setMaxStackSize(1);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

		if(player.onGround && !inFlight && Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				player.motionY = 3;
				inFlight = true;
			}
			//TODO Figure out why this isn't triggering randomly ^
		if(inFlight && player.isSneaking()){
			player.motionY = 1;
			player.motionX = 2;
		}
		if(player.fallDistance > 1 && inFlight) {
			fallDist += player.fallDistance;
			player.fallDistance = 0;
		}
		if(fallDist > 0.5 && player.onGround) {
			inFlight = false;
			player.motionY = 0.5;
			player.fallDistance = 0;
			fallDist = 0;
		}
		player.sendMessage(new TextComponentString(("falldist: " + fallDist + " inFlight: " + inFlight + " playerdist: " + player.fallDistance + " Player: " + player.motionY)));
		}
	}

