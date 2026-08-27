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

package reborncore.common.multiblock;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.Nullable;
import reborncore.RebornCore;

/**
 * Directs multiblock operations to the registry attached to each server level.
 */
public final class MultiblockRegistry {
	private static final AttachmentType<MultiblockWorldRegistry> REGISTRY_ATTACHMENT = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath(RebornCore.MOD_ID, "multiblock_registry")
	);

	private MultiblockRegistry() {
	}

	public static void tickStart(ServerLevel world) {
		MultiblockWorldRegistry registry = getRegistry(world);
		if (registry != null) {
			registry.tick(world);
		}
	}

	public static void onPartAdded(ServerLevel world, IMultiblockPart part) {
		world.getAttachedOrCreate(REGISTRY_ATTACHMENT, MultiblockWorldRegistry::new).onPartAdded(part);
	}

	public static void onPartRemovedFromWorld(ServerLevel world, IMultiblockPart part) {
		MultiblockWorldRegistry registry = getRegistry(world);
		if (registry != null) {
			registry.onPartRemovedFromWorld(part);
		}
	}

	public static void addDirtyController(ServerLevel world, MultiblockControllerBase controller) {
		MultiblockWorldRegistry registry = getRegistry(world);
		if (registry != null) {
			registry.addDirtyController(controller);
		} else {
			RebornCore.LOGGER.error("Adding a dirty controller to a level with no multiblock registry");
		}
	}

	public static void addDeadController(ServerLevel world, MultiblockControllerBase controller) {
		MultiblockWorldRegistry registry = getRegistry(world);
		if (registry != null) {
			registry.addDeadController(controller);
		} else {
			RebornCore.LOGGER.warn("Controller {} in level {} marked as dead, but that level has no multiblock registry", controller.hashCode(), world);
		}
	}

	private static @Nullable MultiblockWorldRegistry getRegistry(ServerLevel world) {
		return world.getAttached(REGISTRY_ATTACHMENT);
	}
}
