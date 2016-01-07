package ic2.api.info;

import net.minecraft.util.*;
import net.minecraftforge.fml.common.*;

public class Info
{
    public static IEnergyValueProvider itemEnergy;
    public static IFuelValueProvider itemFuel;
    public static Object ic2ModInstance;
    public static DamageSource DMG_ELECTRIC;
    public static DamageSource DMG_NUKE_EXPLOSION;
    public static DamageSource DMG_RADIATION;
    private static Boolean ic2Available;
    
    public static boolean isIc2Available() {
        if (Info.ic2Available != null) {
            return Info.ic2Available;
        }
        final boolean loaded = Loader.isModLoaded("IC2");
        if (Loader.instance().hasReachedState(LoaderState.CONSTRUCTING)) {
            Info.ic2Available = loaded;
        }
        return loaded;
    }
    
    static {
        Info.ic2Available = null;
    }
}
