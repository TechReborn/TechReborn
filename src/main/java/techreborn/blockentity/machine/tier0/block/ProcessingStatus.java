/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.blockentity.machine.tier0.block;

import net.minecraft.text.Text;

/**
 * <b>Interface describing different statuses of a Processing Machine</b>
 * <br>
 * Used to define {@link TranslatableText} for UI elements based on the positive, neutral or negative status of a machine
 *
 * @author SimonFlapse
 */
public interface ProcessingStatus {
	/**
	 * <b>The status state described as a translatable text</b>
	 *
	 * @return The translatable text for displaying in a UI
	 */
	Text getText();

	/**
	 * <b>The processing status with current progress formatted</b>
	 * <br>
	 * May not always use the passed progress value
	 *
	 * @param progress	{@code int} the current progress as an integer between 0 and 100. Displayed as a percentage (Eg. 75%)
	 * @return The translatable text for displaying in a UI
	 */
	Text getProgressText(int progress);

	/**
	 * <b>The color to be used for the status text</b>
	 * <br>
	 * Used to help describe the implication of a status effect
	 * e.g red for critical status, blue for informational status or green for positive status
	 *
	 * @return the color represented as an integer
	 */
	int getColor();

	/**
	 * <b>Integer representation of a status</b>
	 * <br>
	 * Similar to, or fully based on {@link Enum#ordinal()}
	 *
	 * @return an integer corresponding to the status code
	 * @see Enum#ordinal()
	 */
	int getStatusCode();
}
