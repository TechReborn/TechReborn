package techreborn.world.veins;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import techreborn.utils.OreDictUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VeinGenerator {

	public static final int BASE_VEIN_SIZE_Y = 17;
	public static final int BASE_VEIN_SIZE_WIDTH = 23;
	public static final int BASE_VEIN_SIZE_DEPTH = 11;

	public static final int VEIN_DENSITY = 4;

	private static final HashMap<Integer, ArrayList<VeinInfo>> dimensionVeins = new HashMap<>();

	private static void registerVeinInternal(int dimension, VeinInfo veinInfo) {
		if (!dimensionVeins.containsKey(dimension))
			dimensionVeins.put(dimension, new ArrayList<>());
		dimensionVeins.get(dimension).add(veinInfo);
	}

	public static void registerVein(int dimension, float chance, float minSize, float maxSize, int minHeight, int maxHeight, Map<Integer, IBlockState> blocks) {
		registerVeinInternal(dimension, new VeinInfo(minSize, maxSize, minHeight, maxHeight, (int) (chance * 100), blocks));
	}

	public static void registerVein(int dimension, float chance, float averageSize, int minHeight, int maxHeight, Pair<Float, IBlockState>... varargs) {
		HashMap<Integer, IBlockState> veinBlocks = new HashMap<>();
		for (Pair<Float, IBlockState> block : varargs)
			veinBlocks.put((int) (block.getKey() * 100), block.getValue());
		registerVein(dimension, chance, averageSize - 0.2f, averageSize + 0.2f, minHeight, maxHeight, veinBlocks);
	}

	public static void generateRandomVein(Random random, int chunkX, int chunkZ, World world) {
		int dimension = world.provider.getDimension();
		if (dimensionVeins.containsKey(dimension)) {
			ArrayList<VeinInfo> veins = dimensionVeins.get(dimension);
			VeinInfo randomVein = getRandomVein(veins, random);
			if (randomVein != null) {
				VeinGenerator.generateVein(world, chunkX, chunkZ, random, randomVein);

			}
		}
	}

	public static boolean generateVein(World world, int chunkX, int chunkZ, Random random, VeinInfo veinInfo) {
		float veinSize = veinInfo.getRandomSize(random);
		boolean invertXZ = random.nextBoolean();

		int veinSizeX = invertXZ ? BASE_VEIN_SIZE_DEPTH : BASE_VEIN_SIZE_WIDTH;
		int veinStartX = chunkX * 16 + random.nextInt(16);

		int veinSizeZ = invertXZ ? BASE_VEIN_SIZE_DEPTH : BASE_VEIN_SIZE_WIDTH;
		int veinStartZ = chunkZ * 16 + random.nextInt(16);

		int veinMaxY = world.getTopSolidOrLiquidBlock(new BlockPos(veinStartX, 1, veinStartZ)).getY();
		int veinSizeY = (int) (BASE_VEIN_SIZE_Y * veinSize);
		int veinStartY = veinInfo.getRandomY(random, veinSizeY, veinMaxY);

		if (isStone(world, new BlockPos(veinStartX, veinStartY, veinStartZ))) {
			for (int veinX = 0; veinX < veinSizeX; veinX++) {
				for (int veinZ = 0; veinZ < veinSizeZ; veinZ++) {
					for (int veinY = 0; veinY < veinSizeY; veinY++) {
						BlockPos veinBlockPos = new BlockPos(
							veinStartX + veinX,
							veinStartY + veinY,
							veinStartZ + veinZ);
						if (random.nextInt(VEIN_DENSITY) == 0 && isStone(world, veinBlockPos))
							world.setBlockState(veinBlockPos, getOreBlock(veinInfo, random));
					}
				}
			}

			return true;
		}

		return false;
	}

	public static IBlockState getOreBlock(VeinInfo veinInfo, Random random) {
		Map<Integer, IBlockState> veinBlocks = veinInfo.getVeinBlocks();
		ArrayList<RangedValue<IBlockState>> blocks = new ArrayList<>();
		int maxValue = 0;
		for (Map.Entry<Integer, IBlockState> entry : veinBlocks.entrySet()) {
			blocks.add(new RangedValue<>(maxValue, maxValue += entry.getKey(), entry.getValue()));
		}
		int randomValue = random.nextInt(maxValue);
		for (RangedValue<IBlockState> rangedValue : blocks) {
			if (rangedValue.isInBounds(randomValue))
				return rangedValue.get();
		}

		//if you found diamond block in the vein, report it!
		return Blocks.DIAMOND_BLOCK.getDefaultState();
	}

	private static VeinInfo getRandomVein(ArrayList<VeinInfo> veins, Random random) {
		if (veins.isEmpty())
			return null;

		ArrayList<RangedValue<VeinInfo>> blocks = new ArrayList<>();
		int maxValue = 0;
		for (VeinInfo entry : veins) {
			blocks.add(new RangedValue<>(maxValue, maxValue += entry.getChance(), entry));
		}
		int randomValue = random.nextInt(maxValue);
		for (RangedValue<VeinInfo> rangedValue : blocks) {
			if (rangedValue.isInBounds(randomValue))
				return rangedValue.get();
		}

		return null;
	}

	public static boolean isStone(World world, BlockPos blockPos) {
		IBlockState block = world.getBlockState(blockPos);
		switch (world.provider.getDimension()) {
			case 1:
				return block.getBlock() == Blocks.END_STONE || OreDictUtils.isOre(block, "endstone");
			case -1:
				return block.getBlock() == Blocks.NETHERRACK || OreDictUtils.isOre(block, "netherrack");
			default:
				return block.getBlock() == Blocks.STONE || OreDictUtils.isOre(block, "stone");
		}
	}

	private static class RangedValue<T> {

		private final int min, max;
		private final T block;

		public RangedValue(int min, int max, T block) {
			this.min = min;
			this.max = max;
			this.block = block;
		}

		public boolean isInBounds(int num) {
			return num >= min && num <= max;
		}

		public T get() {
			return block;
		}
	}

}
