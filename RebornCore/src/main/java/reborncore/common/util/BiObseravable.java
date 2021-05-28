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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class BiObseravable<A, B> {
	@Nullable
	private A a;
	@Nullable
	private B b;

	private final List<BiConsumer<A, B>> listeners = new LinkedList<>();

	public void pushA(A a) {
		this.a = a;
		fireListeners();
	}

	public void pushB(B b) {
		this.b = b;
		fireListeners();
	}

	@NotNull
	public A getA() {
		Objects.requireNonNull(a);
		return a;
	}

	@NotNull
	public B getB() {
		Objects.requireNonNull(b);
		return b;
	}

	private void fireListeners() {
		if (a == null || b == null) {
			return;
		}
		for (BiConsumer<A, B> listener : listeners) {
			listener.accept(a, b);
		}
	}

	public void listen(@NotNull BiConsumer<@NotNull A, @NotNull B> consumer) {
		listeners.add(consumer);
	}
}
