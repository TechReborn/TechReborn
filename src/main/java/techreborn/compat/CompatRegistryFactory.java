/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.compat;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import reborncore.RebornCore;
import reborncore.common.registration.IRegistryFactory;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.RegistryTarget;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

@IRegistryFactory.RegistryFactory
public class CompatRegistryFactory implements IRegistryFactory {
	@Override
	public Class<? extends Annotation> getAnnotation() {
		return RebornRegistry.class;
	}

	@Override
	public void handleClass(Class<?> clazz) {
		if(isCompatModule(clazz)){
			if(CompatManager.INSTANCE.checkConfig(clazz.getSimpleName())){
				try {
					ICompatModule compatModule = (ICompatModule) clazz.newInstance();
					CompatManager.INSTANCE.compatModules.add(compatModule);
					RebornCore.logHelper.info("Loaded module: " + compatModule.getClass().getSimpleName());
				} catch (InstantiationException | IllegalAccessException e) {
					throw new RuntimeException("Failed to register compat module", e);
				}
			}
		}
	}

	private boolean isCompatModule(Class<?> clazz){
		for(Class<?> iface : clazz.getInterfaces()){
			if(iface == ICompatModule.class){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<RegistryTarget> getTargets() {
		return Collections.singletonList(RegistryTarget.CLASS);
	}

	@Override
	public Class<? extends FMLStateEvent> getProcessSate() {
		return FMLPreInitializationEvent.class;
	}
}
