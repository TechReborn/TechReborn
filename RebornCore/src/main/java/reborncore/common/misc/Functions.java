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
		switch (dir) {
			case DOWN:
				return 0;
			case EAST:
				return 5;
			case NORTH:
				return 2;
			case SOUTH:
				return 3;
			case UP:
				return 1;
			case WEST:
				return 4;
			default:
				return 0;
		}
	}

	public static Direction getDirectionFromInt(int dir) {
		int metaDataToSet = 0;
		switch (dir) {
			case 0:
				metaDataToSet = 2;
				break;
			case 1:
				metaDataToSet = 4;
				break;
			case 2:
				metaDataToSet = 3;
				break;
			case 3:
				metaDataToSet = 5;
				break;
		}
		return Direction.byId(metaDataToSet);
	}
}
