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

package reborncore.common.config2.impl;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import reborncore.common.config2.ConfigNode;

public abstract class AbstractConfigNode implements ConfigNode {
	private boolean finalized = false;
	private String comment = null;

	public abstract Codec<? extends AbstractConfigNode> codec();

	@Override
	public ConfigNode comment(String comment) {
		validateSpecChange();

		if (this.comment != null) {
			throw new IllegalStateException("Comment already set");
		}

		this.comment = comment;
		return this;
	}

	public @Nullable String getComment() {
		return this.comment;
	}

	public void finalizeSpec() {
		finalized = true;
	}

	protected void validateSpecChange() {
		if (finalized) {
			throw new IllegalStateException("Cannot alter configuration spec after it has been finalized");
		}
	}
}
