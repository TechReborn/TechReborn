/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TeamReborn
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

package reborncore.client.gui;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ColorCode;

public record Theme(
	ColorCode titleColor,
	ColorCode subtitleColor,
	ColorCode warningTextColor,
	ColorCode ioInputColor,
	ColorCode ioOutputColor,
	ColorCode ioBothColor
) {
	public static final Codec<Theme> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ColorCode.CODEC.fieldOf("titleColor").forGetter(Theme::titleColor),
		ColorCode.CODEC.fieldOf("subtitleColor").forGetter(Theme::subtitleColor),
		ColorCode.CODEC.fieldOf("warningTextColor").forGetter(Theme::warningTextColor),
		ColorCode.CODEC.fieldOf("ioInputColor").forGetter(Theme::ioInputColor),
		ColorCode.CODEC.fieldOf("ioOutputColor").forGetter(Theme::ioOutputColor),
		ColorCode.CODEC.fieldOf("ioBothColor").forGetter(Theme::ioBothColor)
	).apply(instance, Theme::new));
}
