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

package reborncore.common.util;

public class ExceptionUtils {

	public static void tryAndThrow(Runnable runnable, String message) throws RuntimeException {
		try {
			runnable.run();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(message, t);
		}
	}

	public static void requireNonNull(Object obj, String name) {
		if (obj == null)
			throw new NullPointerException(name + " cannot be null!");
	}

	public static <T> void requireNonNullEntries(T[] array, String name) {
		if (array == null)
			return;
		for (T obj : array)
			if (obj == null)
				throw new NullPointerException("No entry of " + name + " can be null!");
	}

	public static void requireNonNullEntries(Iterable<?> iterable, String name) {
		if (iterable == null)
			return;
		for (Object obj : iterable)
			if (obj == null)
				throw new NullPointerException("No entry of " + name + " can be null!");
	}

}
