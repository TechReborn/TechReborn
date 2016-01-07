package ic2.api.info;

import net.minecraftforge.fml.common.*;

public class IC2Classic
{
    public static IWindTicker windNetwork;
    private static IC2Type ic2;
    
    public static IC2Type getLoadedIC2Type() {
        if (IC2Classic.ic2 == IC2Type.NeedLoad) {
            updateState();
        }
        return IC2Classic.ic2;
    }
    
    public static boolean isIc2ExpLoaded() {
        if (IC2Classic.ic2 == IC2Type.NeedLoad) {
            updateState();
        }
        return IC2Classic.ic2 == IC2Type.Experimental;
    }
    
    public static boolean isIc2ClassicLoaded() {
        if (IC2Classic.ic2 == IC2Type.NeedLoad) {
            updateState();
        }
        return IC2Classic.ic2 == IC2Type.SpeigersClassic;
    }
    
    public static IWindTicker getWindNetwork() {
        return IC2Classic.windNetwork;
    }
    
    public static boolean enabledCustoWindNetwork() {
        return IC2Classic.windNetwork != null;
    }
    
    static void updateState() {
        if (Loader.isModLoaded("IC2-Classic")) {
            IC2Classic.ic2 = IC2Type.ImmibisClassic;
            return;
        }
        if (Loader.isModLoaded("IC2")) {
            if (Loader.isModLoaded("IC2-Classic-Spmod")) {
                IC2Classic.ic2 = IC2Type.SpeigersClassic;
                return;
            }
            IC2Classic.ic2 = IC2Type.Experimental;
        }
        else {
            IC2Classic.ic2 = IC2Type.None;
        }
    }
    
    static {
        IC2Classic.ic2 = IC2Type.NeedLoad;
    }
    
    public enum IC2Type
    {
        NeedLoad, 
        Experimental, 
        SpeigersClassic, 
        ImmibisClassic, 
        None;
    }
}
