package techreborn.blockentity.cable;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.util.Identifier;

public interface CableBlockEntityCache {
	BlockApiLookup<CableBlockEntityCache, Void> CACHE = BlockApiLookup.get(new Identifier("techreborn","cable"), CableBlockEntityCache.class, Void.class);

}