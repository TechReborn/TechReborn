/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.test

import groovy.util.logging.Slf4j
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.test.TestContext

import java.lang.reflect.Method

/**
 * Base class that all TR game tests should extend from.
 *
 * All test methods should accept 1 argument of TRTestContext
 */
@Slf4j
abstract class TRGameTest implements FabricGameTest {
	@Override
	void invokeTestMethod(TestContext context, Method method) {
		try {
			method.invoke(this, new TRTestContext(context))
		} catch (TRGameTestException gameTestException) {
			log.error("Test ${method.name} failed with message ${gameTestException.message}", gameTestException.cause)
			log.error(gameTestException.cause.message)
			throw gameTestException
		} catch (Throwable throwable) {
			log.error("Test ${method.name} failed", throwable)
			throw throwable
		}
	}

	static class TRGameTestException extends AssertionError {
		TRGameTestException(String message, Throwable cause) {
			super(message, cause)
		}
	}
}
