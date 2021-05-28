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

/*
 * Easing Functions - taken from https://gist.github.com/gre/1650294
 * only considering the t value for the range [0, 1] => [0, 1]
 */
public class EasingFunctions {

	// no easing, no acceleration
	public double linear(double t) {
		return t;
	}

	// accelerating from zero velocity
	public double easeInQuad(double t) {
		return t * t;
	}

	// decelerating to zero velocity
	public double easeOutQuad(double t) {
		return t * (2 - t);
	}

	// acceleration until halfway, then deceleration
	public double easeInOutQuad(double t) {
		return t < .5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
	}

	// accelerating from zero velocity
	public double easeInCubic(double t) {
		return t * t * t;
	}

	// decelerating to zero velocity
	public double easeOutCubic(double t) {
		return (--t) * t * t + 1;
	}

	// acceleration until halfway, then deceleration
	public double easeInOutCubic(double t) {
		return t < .5 ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1;
	}

	// accelerating from zero velocity
	public double easeInQuart(double t) {
		return t * t * t * t;
	}

	// decelerating to zero velocity
	public double easeOutQuart(double t) {
		return 1 - (--t) * t * t * t;
	}

	// acceleration until halfway, then deceleration
	public double easeInOutQuart(double t) {
		return t < .5 ? 8 * t * t * t * t : 1 - 8 * (--t) * t * t * t;
	}

	// accelerating from zero velocity
	public double easeInQuint(double t) {
		return t * t * t * t * t;
	}

	// decelerating to zero velocity
	public double easeOutQuint(double t) {
		return 1 + (--t) * t * t * t * t;
	}

	// acceleration until halfway, then deceleration
	public double easeInOutQuint(double t) {
		return t < .5 ? 16 * t * t * t * t * t : 1 + 16 * (--t) * t * t * t * t;
	}

}
