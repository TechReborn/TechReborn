package techreborn.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.fml.common.IWorldGenerator;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.init.ModFluids;
import techreborn.lib.ModInfo;

import java.util.Random;

/**
 * Created by modmuss50 on 13/06/2017.
 */

@RebornRegistry(modID = ModInfo.MOD_ID)
public class OilLakeGenerator implements IWorldGenerator {

	@ConfigRegistry(config = "world", category = "oil_lakes", comment = "Enable the generation of underground oil lakes")
	public static boolean enable = true;

	@ConfigRegistry(config = "world", category = "oil_lakes", comment = "Max height of underground lakes")
	public static int maxHeight = 48;

	@ConfigRegistry(config = "world", category = "oil_lakes", comment = "The chance to spawn in a chunk, smaller is more common")
	public static int rareity = 30;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(!enable){
			return;
		}
		if(!world.provider.isSurfaceWorld()){
			return;
		}
		if(random.nextInt(rareity) != 0){
			return;
		}
		int y = random.nextInt(maxHeight);
		new WorldGenLakes(ModFluids.BLOCK_OIL).generate(world, random, new BlockPos((chunkX * 16) + 8, y, (chunkZ * 16) + 8));
	}
}
