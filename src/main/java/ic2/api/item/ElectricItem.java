package ic2.api.item;

import net.minecraft.item.*;
import java.util.*;

public final class ElectricItem
{
    public static IElectricItemManager manager;
    public static IElectricItemManager rawManager;
    private static final List<IBackupElectricItemManager> backupManagers;
    
    public static void registerBackupManager(final IBackupElectricItemManager manager) {
        ElectricItem.backupManagers.add(manager);
    }
    
    public static IBackupElectricItemManager getBackupManager(final ItemStack stack) {
        for (final IBackupElectricItemManager manager : ElectricItem.backupManagers) {
            if (manager.handles(stack)) {
                return manager;
            }
        }
        return null;
    }
    
    static {
        backupManagers = new ArrayList<IBackupElectricItemManager>();
    }
}
