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

package techreborn

import net.minecraft.Bootstrap
import net.minecraft.SharedConstants
import net.minecraft.registry.BuiltinRegistries
import net.minecraft.world.EmptyBlockView
import net.minecraft.world.gen.HeightContext
import net.minecraft.world.gen.chunk.DebugChunkGenerator
import techreborn.world.OreDistribution
import techreborn.world.TargetDimension

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage
import java.nio.file.Paths
import java.util.List

class OreDistributionVisualiser {
	private static final Color[] colors = new Color[] {
			Color.black,
			Color.red,
			Color.pink,
			Color.orange,
			Color.yellow,
			Color.green,
			Color.magenta,
			Color.cyan,
			Color.blue
	}

	static void main(String[] args) {
		// Start the game up enough
		SharedConstants.createGameVersion()
		Bootstrap.initialize()

		generateOreDistributionVisualization()
	}

	static void generateOreDistributionVisualization() throws IOException {
		def heightContext = new FixedHeightContext(-64, 360)

		BufferedImage bi = new BufferedImage(450, 220, BufferedImage.TYPE_INT_ARGB)
		Graphics2D png = bi.createGraphics()
		Font font = new Font("TimesRoman", Font.BOLD, 20)
		png.setFont(font)

		png.setPaint(Color.black)
		// Draw 0
		png.drawLine(0, -heightContext.minY, (overworldOres().size() * 10) + 20, -heightContext.minY)

		png.setPaint(Color.blue)
		// Draw ground
		png.drawLine(0, -heightContext.minY + 64, (overworldOres().size() * 10) + 20, -heightContext.minY + 64)

		int c = 1
		for (OreDistribution value : overworldOres()) {
			png.setPaint(colors[c])

			def yMin = value.minOffset.getY(heightContext) + -heightContext.minY
			def yMax = -heightContext.minY + value.maxY

			png.fill3DRect(c * 10, yMin, 10, yMax - yMin, true)

			png.drawString(value.name() + "(%d -> %d)".formatted(value.minOffset.getY(heightContext), value.maxY), 190, 25 * c)

			c += 1
		}

		ImageIO.write(bi, "PNG", Paths.get("ore_distribution.png").toFile())
	}

	private static List<OreDistribution> overworldOres() {
		return Arrays.stream(OreDistribution.values())
				.filter(ore -> ore.dimension == TargetDimension.OVERWORLD)
				.toList()
	}

	private static class FixedHeightContext extends HeightContext {
		final int minY
		final int height

		FixedHeightContext(int minY, int height) {
			super(new DebugChunkGenerator(BuiltinRegistries.BIOME), EmptyBlockView.INSTANCE)

			this.minY = minY
			this.height = height
		}

		@Override
		int getMinY() {
			return minY
		}

		@Override
		int getHeight() {
			return height
		}
	}
}
