/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.compat.autoswitch;

import autoswitch.api.AutoSwitchApi;
import autoswitch.api.AutoSwitchMap;
import autoswitch.api.DurabilityGetter;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.common.util.ItemUtils;
import techreborn.items.tool.ChainsawItem;
import techreborn.items.tool.DrillItem;
import techreborn.items.tool.JackhammerItem;
import techreborn.items.tool.basic.RockCutterItem;
import techreborn.items.tool.basic.ElectricTreetapItem;
import techreborn.items.tool.industrial.NanosaberItem;
import techreborn.items.tool.industrial.OmniToolItem;

public class AutoSwitchApiImpl implements AutoSwitchApi {

	@Override
	public void moddedTargets(AutoSwitchMap<String, Object> targets, AutoSwitchMap<String, String> actionConfig, AutoSwitchMap<String, String> usableConfig) {

	}

	@Override
	public void moddedToolGroups(AutoSwitchMap<String, Pair<Tag<Item>, Class<?>>> toolGroupings) {

	}

	@Override
	public void customDamageSystems(AutoSwitchMap<Class<?>, DurabilityGetter> damageMap) {
		// Multiple by 100 to get percentage out of decimal form
		damageMap.put(DrillItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(ChainsawItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(JackhammerItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(NanosaberItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(OmniToolItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(ElectricTreetapItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));
		damageMap.put(RockCutterItem.class, stack -> 100 * ItemUtils.getPowerForDurabilityBar(stack));

	}
}
