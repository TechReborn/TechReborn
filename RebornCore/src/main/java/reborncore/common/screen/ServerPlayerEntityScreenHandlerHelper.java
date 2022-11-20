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

package reborncore.common.screen;

import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Optional;

// Helper to access the ServerPlayerEntity instance from a ScreenHandlerListener
public class ServerPlayerEntityScreenHandlerHelper {
	private static final String CLASS_NAME = ServerPlayerEntity.class.getName() + "$2";
	private static final String FIELD_NAME = "field_29183";

	private static final Class<?> CLAZZ;
	private static final VarHandle VAR_HANDLE;

	static {
		try {
			CLAZZ = Class.forName(CLASS_NAME);
			VAR_HANDLE = MethodHandles.privateLookupIn(CLAZZ, MethodHandles.lookup())
				.findVarHandle(CLAZZ, FIELD_NAME, ServerPlayerEntity.class);
		} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Optional<ServerPlayerEntity> getServerPlayerEntity(ScreenHandlerListener screenHandlerListener) {
		if (screenHandlerListener.getClass() == CLAZZ) {
			return Optional.of((ServerPlayerEntity) VAR_HANDLE.get(screenHandlerListener));
		}

		return Optional.empty();
	}
}
