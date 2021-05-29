/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.misc;

import net.minecraft.util.math.Direction;

public class Functions {
	public static int getIntDirFromDirection(Direction dir) {
		return switch (dir) {
			case DOWN -> 0;
			case EAST -> 5;
			case NORTH -> 2;
			case SOUTH -> 3;
			case UP -> 1;
			case WEST -> 4;
		};
	}

	public static Direction getDirectionFromInt(int dir) {
		int metaDataToSet = switch (dir) {
			case 0 -> 2;
			case 1 -> 4;
			case 2 -> 3;
			case 3 -> 5;
			default -> 0;
		};
		return Direction.byId(metaDataToSet);
	}
}
