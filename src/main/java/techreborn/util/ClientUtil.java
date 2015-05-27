package techreborn.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientUtil {

    @SideOnly(Side.CLIENT)
    public static World getWorld(){
        return Minecraft.getMinecraft().theWorld;
    }

}
