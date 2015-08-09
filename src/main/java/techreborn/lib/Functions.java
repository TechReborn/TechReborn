package techreborn.lib;

import net.minecraftforge.common.util.ForgeDirection;

public class Functions {
    public static int getIntDirFromDirection(ForgeDirection dir) {
        switch (dir) {
            case DOWN:
                return 0;
            case EAST:
                return 5;
            case NORTH:
                return 2;
            case SOUTH:
                return 3;
            case UNKNOWN:
                return 0;
            case UP:
                return 1;
            case WEST:
                return 4;
            default:
                return 0;
        }
    }

    public static ForgeDirection getDirectionFromInt(int dir) {
        int metaDataToSet = 0;
        switch (dir) {
            case 0:
                metaDataToSet = 2;
                break;
            case 1:
                metaDataToSet = 4;
                break;
            case 2:
                metaDataToSet = 3;
                break;
            case 3:
                metaDataToSet = 5;
                break;
        }
        return ForgeDirection.getOrientation(metaDataToSet);
    }
}
