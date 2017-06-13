/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.tiles.idsu;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class IDSUManager {


	@CapabilityInject(IDataIDSU.class)
	public static Capability<IDataIDSU> CAPABILITY_IDSU = null;

	public static void init(){
		CapabilityManager.INSTANCE.register(IDataIDSU.class, new CapabilityIDSUStorage(), new Factory());
		MinecraftForge.EVENT_BUS.register(IDSUManager.class);
	}

	@SubscribeEvent
	public static void Attach(AttachCapabilitiesEvent<World> event){
		event.addCapability(new ResourceLocation("techreborn","idsuManager"), new CapProvider(event.getObject()));
	}

	private static class CapProvider implements ICapabilityProvider {

		World world;
		IDSUEnergyStore store;

		public CapProvider(World world) {
			this.world = world;
			this.store = new IDSUEnergyStore();
		}

		@Override
		public boolean hasCapability(
			@Nonnull
				Capability<?> capability,
			@Nullable
				EnumFacing facing) {
			if(capability == CAPABILITY_IDSU){
				return true;
			}
			return false;
		}

		@Nullable
		@Override
		public <T> T getCapability(
			@Nonnull
				Capability<T> capability,
			@Nullable
				EnumFacing facing) {
			if(capability == CAPABILITY_IDSU){
				return (T) store;
			}
			return null;
		}
	}

	private static class Factory implements Callable<IDataIDSU> {

		@Override
		public IDataIDSU call() throws Exception {
			return new IDSUEnergyStore();
		}
	}

	public static IDataIDSU getData(World world){
		if(world.hasCapability(CAPABILITY_IDSU, null)){
			return world.getCapability(CAPABILITY_IDSU, null);
		}
		return null;
	}

}
