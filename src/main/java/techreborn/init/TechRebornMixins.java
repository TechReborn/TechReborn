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
import reborncore.mixin.api.IMixinRegistry;
import reborncore.mixin.api.MixinRegistationTime;
import reborncore.mixin.json.MixinTargetData;

import java.util.ArrayList;
import java.util.List;

public class TechRebornMixins implements IMixinRegistry {
	@Override
	public List<MixinTargetData> register() {
		List<MixinTargetData> targetData = new ArrayList<>();
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemRockCutter"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.armor.ItemLithiumBatpack"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.armor.ItemLapotronPack"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.battery.ItemLithiumBattery"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.battery.ItemLapotronicOrb"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemOmniTool"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.battery.ItemEnergyCrystal"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.battery.ItemLapotronCrystal"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.battery.ItemReBattery"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemElectricTreetap"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemSteelDrill"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemDiamondDrill"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemAdvancedDrill"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemSteelChainsaw"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemDiamondChainsaw"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemAdvancedChainsaw"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemSteelJackhammer"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemDiamondJackhammer"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemAdvancedJackhammer"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemNanosaber"));
		targetData.addAll(PoweredItem.registerPoweredItem("techreborn.items.tools.ItemCloakingDevice"));
		return targetData;
	}

	@Override
	public MixinRegistationTime registrationTime() {
		return MixinRegistationTime.LATE;
	}
}
