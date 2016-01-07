package ic2.api.network;

import java.lang.reflect.*;
import net.minecraft.tileentity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public final class NetworkHelper
{
    private static Object instance;
    private static Method NetworkManager_updateTileEntityField;
    private static Method NetworkManager_initiateTileEntityEvent;
    private static Method NetworkManager_initiateItemEvent;
    private static Method NetworkManager_initiateClientTileEntityEvent;
    private static Method NetworkManager_initiateClientItemEvent;
    
    public static void updateTileEntityField(final TileEntity te, final String field) {
        try {
            if (NetworkHelper.NetworkManager_updateTileEntityField == null) {
                NetworkHelper.NetworkManager_updateTileEntityField = Class.forName(getPackage() + ".core.network.NetworkManager").getMethod("updateTileEntityField", TileEntity.class, String.class);
            }
            if (NetworkHelper.instance == null) {
                NetworkHelper.instance = getInstance();
            }
            NetworkHelper.NetworkManager_updateTileEntityField.invoke(NetworkHelper.instance, te, field);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void initiateTileEntityEvent(final TileEntity te, final int event, final boolean limitRange) {
        try {
            if (NetworkHelper.NetworkManager_initiateTileEntityEvent == null) {
                NetworkHelper.NetworkManager_initiateTileEntityEvent = Class.forName(getPackage() + ".core.network.NetworkManager").getMethod("initiateTileEntityEvent", TileEntity.class, Integer.TYPE, Boolean.TYPE);
            }
            if (NetworkHelper.instance == null) {
                NetworkHelper.instance = getInstance();
            }
            NetworkHelper.NetworkManager_initiateTileEntityEvent.invoke(NetworkHelper.instance, te, event, limitRange);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void initiateItemEvent(final EntityPlayer player, final ItemStack itemStack, final int event, final boolean limitRange) {
        try {
            if (NetworkHelper.NetworkManager_initiateItemEvent == null) {
                NetworkHelper.NetworkManager_initiateItemEvent = Class.forName(getPackage() + ".core.network.NetworkManager").getMethod("initiateItemEvent", EntityPlayer.class, ItemStack.class, Integer.TYPE, Boolean.TYPE);
            }
            if (NetworkHelper.instance == null) {
                NetworkHelper.instance = getInstance();
            }
            NetworkHelper.NetworkManager_initiateItemEvent.invoke(NetworkHelper.instance, player, itemStack, event, limitRange);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void initiateClientTileEntityEvent(final TileEntity te, final int event) {
        try {
            if (NetworkHelper.NetworkManager_initiateClientTileEntityEvent == null) {
                NetworkHelper.NetworkManager_initiateClientTileEntityEvent = Class.forName(getPackage() + ".core.network.NetworkManager").getMethod("initiateClientTileEntityEvent", TileEntity.class, Integer.TYPE);
            }
            if (NetworkHelper.instance == null) {
                NetworkHelper.instance = getInstance();
            }
            NetworkHelper.NetworkManager_initiateClientTileEntityEvent.invoke(NetworkHelper.instance, te, event);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void initiateClientItemEvent(final ItemStack itemStack, final int event) {
        try {
            if (NetworkHelper.NetworkManager_initiateClientItemEvent == null) {
                NetworkHelper.NetworkManager_initiateClientItemEvent = Class.forName(getPackage() + ".core.network.NetworkManager").getMethod("initiateClientItemEvent", ItemStack.class, Integer.TYPE);
            }
            if (NetworkHelper.instance == null) {
                NetworkHelper.instance = getInstance();
            }
            NetworkHelper.NetworkManager_initiateClientItemEvent.invoke(NetworkHelper.instance, itemStack, event);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static String getPackage() {
        final Package pkg = NetworkHelper.class.getPackage();
        if (pkg != null) {
            final String packageName = pkg.getName();
            return packageName.substring(0, packageName.length() - ".api.network".length());
        }
        return "ic2";
    }
    
    private static Object getInstance() {
        try {
            return Class.forName(getPackage() + ".core.IC2").getDeclaredField("network").get(null);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
