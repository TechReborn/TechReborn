package ic2.api.tile;

import net.minecraft.block.*;
import java.util.*;

public final class ExplosionWhitelist
{
    private static Set<Block> whitelist;
    
    public static void addWhitelistedBlock(final Block block) {
        ExplosionWhitelist.whitelist.add(block);
    }
    
    public static void removeWhitelistedBlock(final Block block) {
        ExplosionWhitelist.whitelist.remove(block);
    }
    
    public static boolean isBlockWhitelisted(final Block block) {
        return ExplosionWhitelist.whitelist.contains(block);
    }
    
    static {
        ExplosionWhitelist.whitelist = new HashSet<>();
    }
}
