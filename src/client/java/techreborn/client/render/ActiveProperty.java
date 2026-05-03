/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.client.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.base.SimpleEnergyItem;
import techreborn.TechReborn;
import techreborn.component.TRDataComponentTypes;
import techreborn.items.BatteryItem;
import techreborn.items.FrequencyTransmitterItem;
import techreborn.items.armor.BatpackItem;
import techreborn.items.tool.ChainsawItem;
import techreborn.items.tool.industrial.NanosaberItem;

public record ActiveProperty() implements SelectItemModelProperty<PowerType> {
	public static Identifier ID = Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "active");
	public static Codec<PowerType> VALUE_CODEC = PowerType.CODEC;
	public static final SelectItemModelProperty.Type<ActiveProperty, PowerType> TYPE = SelectItemModelProperty.Type.create(
		MapCodec.unit(new ActiveProperty()), PowerType.CODEC
	);

	@Override
	public PowerType get(
		ItemStack stack, ClientLevel world, LivingEntity entity, int seed, ItemDisplayContext mode
	) {
		Item item = stack.getItem();
		if (item instanceof NanosaberItem nanosaber) {
			if (stack.get(TRDataComponentTypes.IS_ACTIVE) != Boolean.TRUE) return PowerType.OFF;
			if ((double) nanosaber.getStoredEnergy(stack) / nanosaber.getEnergyCapacity(stack) <= 0.1) return PowerType.LOW;
			return PowerType.ON;
		} else if (item instanceof ChainsawItem chainsaw) {
			if (SimpleEnergyItem.getStoredEnergyUnchecked(stack) < chainsaw.getCost()) return PowerType.OFF;
			if (entity == null || !entity.getMainHandItem().equals(stack)) return PowerType.OFF;
			return PowerType.ON;
		} else if (item instanceof BatteryItem || item instanceof BatpackItem) {
			return SimpleEnergyItem.getStoredEnergyUnchecked(stack) == 0 ? PowerType.OFF : PowerType.ON;
		} else if (item instanceof FrequencyTransmitterItem) {
			return stack.get(TRDataComponentTypes.FREQUENCY_TRANSMITTER) == null ? PowerType.OFF : PowerType.ON;
		}
		return PowerType.OFF;
	}

	@Override
	public SelectItemModelProperty.Type<ActiveProperty, PowerType> type() {
		return TYPE;
	}

	@Override
	public Codec<PowerType> valueCodec() {
		return VALUE_CODEC;
	}
}
