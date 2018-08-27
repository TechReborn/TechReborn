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

package techreborn.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.EGui;

public class ItemParts extends ItemTR {

	public ItemParts() {
		this.setTranslationKey("techreborn.part");
	}

//	public static ItemStack getPartByName(String name, final int count) {
//		//TODO: Change all recipes n' shit to use proper snake_case names so I don't have to do this bullshit
//		if (name.equals("NaKCoolantSimple"))
//			name = "nak_coolant_simple";
//		if (name.equals("NaKCoolantTriple"))
//			name = "nak_coolant_triple";
//		if (name.equals("NaKCoolantSix"))
//			name = "nak_coolant_six";
//		if (name.equals("superconductor"))
//			name = "super_conductor";
//		if (name.equals("carbonfiber"))
//			name = "carbon_fiber";
//		if (name.equals("carbonmesh"))
//			name = "carbon_mesh";
//		if (name.equals("rubberSap"))
//			name = "sap";
//		name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
//		for (int i = 0; i < ItemParts.types.length; i++) {
//			if (ItemParts.types[i].equalsIgnoreCase(name)) {
//				return new ItemStack(ModItems.PARTS, count, i);
//			}
//		}
//		throw new InvalidParameterException("The part " + name + " could not be found.");
//	}
//
//	public static ItemStack getPartByName(final String name) {
//		return ItemParts.getPartByName(name, 1);
//	}

	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
		switch (itemStack.getItemDamage()) {
			case 37: // Destructo pack
				player.openGui(Core.INSTANCE, EGui.DESTRUCTOPACK.ordinal(), world, (int) player.posX, (int) player.posY,
					(int) player.posY);
				break;
		}
		return itemStack;
	}
}
