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

package techreborn.init;

import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class ModParts {

	public static HashMap<Integer, ItemStack> CABLE = new HashMap<>();

	public static void init() { // TODO 1.8
		// if (Loader.isModLoaded("IC2")) {
		// for (int i = 0; i < 11; i++) {
		// CablePart part = new CablePart();
		// part.setType(i);
		// ModPartRegistry.registerPart(part);
		// }
		// }
		// ModPartRegistry.addProvider("techreborn.partSystem.fmp.FMPFactory",
		// "ForgeMultipart");
		// ModPartRegistry.addProvider("techreborn.partSystem.QLib.QModPartFactory",
		// "qmunitylib");
		// ModPartRegistry.addAllPartsToSystems();
		// for (IPartProvider provider : ModPartRegistry.providers) {
		// if (provider.modID().equals("ForgeMultipart")) {
		// ModPartRegistry.masterProvider = provider;
		// }
		// }
	}
}
