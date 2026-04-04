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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ContainerListener;

// Helper to access the ServerPlayerEntity instance from a ContainerListener
public class ServerPlayerEntityScreenHandlerHelper {
	private static final String CLASS_NAME = ServerPlayer.class.getName() + "$2";
	private static final String FIELD_NAME = "this$0";

	private static final Class<?> CLAZZ;
	private static final VarHandle VAR_HANDLE;

	static {
		try {
			CLAZZ = Class.forName(CLASS_NAME);
			VAR_HANDLE = MethodHandles.privateLookupIn(CLAZZ, MethodHandles.lookup())
				.findVarHandle(CLAZZ, FIELD_NAME, ServerPlayer.class);
		} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Optional<ServerPlayer> getServerPlayerEntity(ContainerListener screenHandlerListener) {
		if (screenHandlerListener.getClass() == CLAZZ) {
			return Optional.of((ServerPlayer) VAR_HANDLE.get(screenHandlerListener));
		}

		return Optional.empty();
	}
}
