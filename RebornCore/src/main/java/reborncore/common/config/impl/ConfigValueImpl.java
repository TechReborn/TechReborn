/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016-2017 TeamReborn
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

package reborncore.common.config.impl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.mojang.serialization.Codec;
import reborncore.common.config.ConfigValue;

public class ConfigValueImpl<T> extends AbstractConfigNode implements ConfigValue<T> {
	private final Codec<T> codec;
	private final T defaultValue;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private T value = null;

	public ConfigValueImpl(Codec<T> codec, T defaultValue) {
		this.codec = codec;
		this.defaultValue = defaultValue;
	}

	@Override
	public T get() {
		lock.readLock().lock();

		try {
			if (value == null) {
				return defaultValue;
			}

			return value;
		} finally {
			lock.readLock().unlock();
		}
	}

	public void setValue(T value) {
		lock.writeLock().lock();

		try {
			this.value = value;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Codec<ConfigValueImpl<T>> codec() {
		return codec.xmap(newVal -> {
			setValue(newVal);
			return this;
		}, ConfigValueImpl::get);
	}

	@Override
	public ConfigValueImpl<T> comment(String comment) {
		super.comment(comment);
		return this;
	}
}
