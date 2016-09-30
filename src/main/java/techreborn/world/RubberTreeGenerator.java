package techreborn.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;
import techreborn.Core;
import techreborn.blocks.BlockRubberLeaves;
import techreborn.blocks.BlockRubberLog;
import techreborn.init.ModBlocks;

import java.util.Random;

public class RubberTreeGenerator extends WorldGenerator {

	boolean isWorldGen = true;

	public RubberTreeGenerator() {
		super();
	}

	public RubberTreeGenerator(boolean isWorldGen) {
		super(!isWorldGen);
		this.isWorldGen = isWorldGen;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		int x = position.getX();
		int z = position.getZ();
		int retries = rand.nextInt(3) + 2;
		if (isWorldGen) {
			for (int c = 0; c < retries; c++) {
				int y = worldIn.getActualHeight() - 1;
				while (worldIn.isAirBlock(new BlockPos(x, y, z)) && y > 0) {
					y--;
				}
				if (!growTree(worldIn, rand, x, y + 1, z)) {
					retries--;
				}
				x += rand.nextInt(16) - 8;
				z += rand.nextInt(16) - 8;
			}
		} else {
			int y = worldIn.getActualHeight() - 1;
			while (worldIn.isAirBlock(new BlockPos(x, y, z)) && y > 0) {
				y--;
			}
			return growTree(worldIn, rand, x, y + 1, z);
		}

		return false;
	}

	public boolean growTree(World world, Random rand, int x, int y, int z) {
		int treeHeight = rand.nextInt(5) + Core.worldGen.config.rubberTreeConfig.treeBaseHeight;
		int worldHeight = world.getHeight();
		if (y >= 1 && y + treeHeight + 1 <= worldHeight) {
			int xOffset;
			int yOffset;
			int zOffset;
			IBlockState baseSate = world.getBlockState(new BlockPos(x, y - 1, z));
			Block baseBlock = baseSate.getBlock();
			boolean hasPlacedBlock = false;
			if (baseBlock != null && baseBlock.canSustainPlant(baseSate, world, new BlockPos(x, y - 1, z),
				EnumFacing.UP, (IPlantable) ModBlocks.rubberSapling) && y < worldHeight - treeHeight - 1) {
				for (yOffset = y; yOffset <= y + 1 + treeHeight; ++yOffset) {
					byte radius = 1;
					if (yOffset == y) {
						radius = 0;
					}
					if (yOffset >= y + 1 + treeHeight - 2) {
						radius = 2;
					}
					if (yOffset >= 0 & yOffset < worldHeight) {
						for (xOffset = x - radius; xOffset <= x + radius; ++xOffset) {
							for (zOffset = z - radius; zOffset <= z + radius; ++zOffset) {
								IBlockState state = world.getBlockState(new BlockPos(xOffset, yOffset, zOffset));
								Block block = state.getBlock();

								if (block != null
									&& !(block.isLeaves(state, world, new BlockPos(xOffset, yOffset, zOffset))
									|| block.isAir(state, world, new BlockPos(xOffset, yOffset, zOffset))
									|| block.canBeReplacedByLeaves(state, world,
									new BlockPos(xOffset, yOffset, zOffset)))) {
									return false;
								}
							}
						}
					} else {
						return false;
					}
				}

				BlockPos treeBase = new BlockPos(x, y, z);
				BlockPos treeRoot = treeBase.down();
				IBlockState state = world.getBlockState(treeRoot);
				state.getBlock().onPlantGrow(state, world, treeRoot, treeBase);
				for (yOffset = y - 3 + treeHeight; yOffset <= y + treeHeight; ++yOffset) {
					int var12 = yOffset - (y + treeHeight), center = 1 - var12 / 2;
					for (xOffset = x - center; xOffset <= x + center; ++xOffset) {
						int xPos = xOffset - x, t = xPos >> 15;
						xPos = (xPos + t) ^ t;
						for (zOffset = z - center; zOffset <= z + center; ++zOffset) {
							int zPos = zOffset - z;
							zPos = (zPos + (t = zPos >> 31)) ^ t;
							IBlockState state1 = world.getBlockState(new BlockPos(xOffset, yOffset, zOffset));
							Block block = state1.getBlock();
							if (((xPos != center | zPos != center) || rand.nextInt(2) != 0 && var12 != 0)
								&& (block == null
								|| block.isLeaves(state1, world, new BlockPos(xOffset, yOffset, zOffset))
								|| block.isAir(state1, world, new BlockPos(xOffset, yOffset, zOffset))
								|| block.canBeReplacedByLeaves(state1, world,
								new BlockPos(xOffset, yOffset, zOffset)))) {
								this.setBlockAndNotifyAdequately(world, new BlockPos(xOffset, yOffset, zOffset),
									ModBlocks.rubberLeaves.getDefaultState());
								hasPlacedBlock = true;
							}
						}
					}
				}

				BlockPos topLogPos = null;
				for (yOffset = 0; yOffset < treeHeight; ++yOffset) {
					BlockPos blockpos = new BlockPos(x, y + yOffset, z);
					IBlockState state1 = world.getBlockState(blockpos);
					Block block = state1.getBlock();
					if (block == null || block.isAir(state1, world, blockpos) || block.isLeaves(state1, world, blockpos)
						|| block.isReplaceable(world, blockpos)) {
						IBlockState newState = ModBlocks.rubberLog.getDefaultState();
						boolean isAddingSap = false;
						if (rand.nextInt(Core.worldGen.config.rubberTreeConfig.sapRarity) == 0) {
							newState = newState.withProperty(BlockRubberLog.HAS_SAP, true)
								.withProperty(BlockRubberLog.SAP_SIDE, EnumFacing.getHorizontal(rand.nextInt(4)));
							isAddingSap = true;
						}
						if (isAddingSap) {
							world.setBlockState(blockpos, newState, 2);
						} else {
							this.setBlockAndNotifyAdequately(world, blockpos, newState);
						}
						hasPlacedBlock = true;
						topLogPos = blockpos;
					}
				}
				if (topLogPos != null) {
					for (int i = 0; i < Core.worldGen.config.rubberTreeConfig.spireHeight; i++) {
						BlockPos spikePos = topLogPos.up(i);
						this.setBlockAndNotifyAdequately(world, spikePos, ModBlocks.rubberLeaves.getDefaultState()
							.withProperty(BlockRubberLeaves.DECAYABLE, true));
					}
				}
			}
			return hasPlacedBlock;
		}
		return false;
	}
}
