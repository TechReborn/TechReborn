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

import reborncore.common.powerSystem.PoweredItem;

/**
 * Created by modmuss50 on 18/01/2017.
 */
public class ModPoweredItems {

	public static void preInit(){
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemRockCutter");
		PoweredItem.registerPoweredItem("techreborn.items.armor.ItemLithiumBatpack");
		PoweredItem.registerPoweredItem("techreborn.items.armor.ItemLapotronPack");
		PoweredItem.registerPoweredItem("techreborn.items.battery.ItemLithiumBattery");
		PoweredItem.registerPoweredItem("techreborn.items.battery.ItemLapotronicOrb");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemOmniTool");
		PoweredItem.registerPoweredItem("techreborn.items.battery.ItemEnergyCrystal");
		PoweredItem.registerPoweredItem("techreborn.items.battery.ItemLapotronCrystal");
		PoweredItem.registerPoweredItem("techreborn.items.battery.ItemReBattery");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemElectricTreetap");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemSteelDrill");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemDiamondDrill");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemAdvancedDrill");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemSteelChainsaw");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemDiamondChainsaw");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemAdvancedChainsaw");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemSteelJackhammer");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemDiamondJackhammer");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemAdvancedJackhammer");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemNanosaber");
		PoweredItem.registerPoweredItem("techreborn.items.tools.ItemCloakingDevice");
	}

}
