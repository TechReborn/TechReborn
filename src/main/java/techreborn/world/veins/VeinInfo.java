package techreborn.world.veins;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;

import java.util.Map;
import java.util.Random;

public class VeinInfo {

	private final float minSize, maxSize;
	private final int minHeight, maxHeight;
	private final int chance;

	private final ImmutableMap<Integer, IBlockState> veinBlocks;

	public VeinInfo(float minSize, float maxSize, int minHeight, int maxHeight, int chance, Map<Integer, IBlockState> veinBlocks) {
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.chance = chance;
		this.veinBlocks = ImmutableMap.copyOf(veinBlocks);
	}

	public float getMinSize() {
		return minSize;
	}

	public float getMaxSize() {
		return maxSize;
	}

	public float getRandomSize(Random random) {
		return minSize + random.nextFloat() * maxSize;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getRandomY(Random random, int boxHeight, int groundHeight) {
		int maxValue = groundHeight > maxHeight ? maxHeight : groundHeight;
		int randomY = (int) (minHeight + random.nextFloat() * maxValue);
		if (randomY + boxHeight > maxValue) {
			return randomY - (boxHeight - (maxValue - randomY));
		} else if (randomY - boxHeight < minHeight) {
			return randomY + (boxHeight - (randomY - minHeight));
		}
		return randomY;
	}

	public int getChance() {
		return chance;
	}

	public boolean shouldGenerate(Random random) {
		return chance >= random.nextInt(100);
	}

	public ImmutableMap<Integer, IBlockState> getVeinBlocks() {
		return veinBlocks;
	}

}
