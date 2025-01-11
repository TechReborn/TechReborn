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

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Identifier;
import team.reborn.energy.api.base.SimpleEnergyItem;
import techreborn.TechReborn;
import techreborn.component.TRDataComponentTypes;
import techreborn.items.BatteryItem;
import techreborn.items.FrequencyTransmitterItem;
import techreborn.items.armor.BatpackItem;
import techreborn.items.tool.ChainsawItem;
import techreborn.items.tool.industrial.NanosaberItem;

public record ActiveProperty() implements SelectProperty<PowerType> {
	public static Identifier ID = Identifier.of(TechReborn.MOD_ID, "active");
	public static final SelectProperty.Type<ActiveProperty, PowerType> TYPE = SelectProperty.Type.create(
		MapCodec.unit(new ActiveProperty()), PowerType.CODEC
	);

	public PowerType getValue(
		ItemStack stack, ClientWorld world, LivingEntity entity, int seed, ModelTransformationMode mode
	) {
		Item item = stack.getItem();
		if (item instanceof NanosaberItem nanosaber) {
			if (stack.get(TRDataComponentTypes.IS_ACTIVE) != Boolean.TRUE) return PowerType.OFF;
			if ((double) nanosaber.getStoredEnergy(stack) / nanosaber.getEnergyCapacity(stack) <= 0.1) return PowerType.LOW;
			return PowerType.ON;
		} else if (item instanceof ChainsawItem chainsaw) {
			if (SimpleEnergyItem.getStoredEnergyUnchecked(stack) < chainsaw.getCost()) return PowerType.OFF;
			if (entity == null || !entity.getMainHandStack().equals(stack)) return PowerType.OFF;
			return PowerType.ON;
		} else if (item instanceof BatteryItem || item instanceof BatpackItem) {
			return SimpleEnergyItem.getStoredEnergyUnchecked(stack) == 0 ? PowerType.OFF : PowerType.ON;
		} else if (item instanceof FrequencyTransmitterItem) {
			return stack.get(TRDataComponentTypes.FREQUENCY_TRANSMITTER) == null ? PowerType.OFF : PowerType.ON;
		}
		return PowerType.OFF;
	}

	@Override
	public SelectProperty.Type<ActiveProperty, PowerType> getType() {
		return TYPE;
	}
}
