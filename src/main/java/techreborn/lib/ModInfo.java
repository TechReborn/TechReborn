package techreborn.lib;

import net.minecraft.util.StatCollector;

public class ModInfo {
    public static final String MOD_NAME = "TechReborn";
    public static final String MOD_ID = "techreborn";
    public static final String MOD_VERSION = "@MODVERSION@";
    public static final String MOD_DEPENDENCUIES =
            "required-after:Forge@[10.13.3.1374,)";
    public static final String SERVER_PROXY_CLASS = "techreborn.proxies.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "techreborn.proxies.ClientProxy";
    public static final String GUI_FACTORY_CLASS = "techreborn.config.TechRebornGUIFactory";

    public static final String MISSING_MULTIBLOCK = StatCollector.translateToLocal("techreborn.message.missingmultiblock");

    public static final class Keys {
        public static final String CATEGORY = "keys.techreborn.category";
        public static final String CONFIG = "keys.techreborn.config";
    }
}
