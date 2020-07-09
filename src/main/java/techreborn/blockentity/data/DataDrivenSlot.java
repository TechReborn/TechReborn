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

package techreborn.blockentity.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.JsonHelper;
import org.apache.commons.lang3.Validate;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.screen.builder.BlockEntityScreenHandlerBuilder;
import reborncore.common.util.serialization.SerializationUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataDrivenSlot {

	public static List<DataDrivenSlot> read(JsonArray jsonArray) {
		AtomicInteger idCount = new AtomicInteger();
		return SerializationUtil.stream(jsonArray)
				.map(JsonElement::getAsJsonObject)
				.map(json -> new DataDrivenSlot(idCount.getAndIncrement(), JsonHelper.getInt(json, "x"), JsonHelper.getInt(json, "y"), SlotType.fromString(JsonHelper.getString(json, "type"))))
				.collect(Collectors.toList());
	}

	private final int id;
	private final int x;
	private final int y;
	private final SlotType type;

	public DataDrivenSlot(int id, int x, int y, SlotType type) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
		Validate.notNull(type);
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public SlotType getType() {
		return type;
	}

	public void add(BlockEntityScreenHandlerBuilder inventoryBuilder) {
		type.getSlotBiConsumer().accept(inventoryBuilder, this);
	}

	@Environment(EnvType.CLIENT)
	public void draw(MatrixStack matrixStack, GuiBase<?> guiBase, GuiBase.Layer layer) {
		//TODO find a better way to do this
		if (getType() == SlotType.OUTPUT) {
			guiBase.drawOutputSlot(matrixStack, getX(), getY(), layer);
		} else {
			guiBase.drawSlot(matrixStack, getX(), getY(), layer);
		}
	}
}
