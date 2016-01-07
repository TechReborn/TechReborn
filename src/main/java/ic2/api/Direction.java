package ic2.api;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public enum Direction
{
    XN, 
    XP, 
    YN, 
    YP, 
    ZN, 
    ZP;
    
    public static final Direction[] directions;
    
    public static Direction fromSideValue(final int side) {
        return Direction.directions[(side + 2) % 6];
    }
    
    public static Direction fromEnumFacing(final EnumFacing dir) {
        if (dir == null) {
            return null;
        }
        return fromSideValue(dir.getIndex());
    }
    
    public TileEntity applyToTileEntity(final TileEntity te) {
        return this.applyTo(te.getWorld(), te.getPos());
    }
    
    public TileEntity applyTo(final World world, final BlockPos pos) {
        final int[] array;
        final int[] coords = array = new int[] { pos.getX(), pos.getY(), pos.getZ() };
        final int n = this.ordinal() / 2;
        array[n] += this.getSign();
        BlockPos pos2 = new BlockPos(coords[0], coords[1], coords[2]);
        if (world != null && world.isBlockLoaded(pos2)) {
            try {
                return world.getTileEntity(pos2);
            }
            catch (Exception e) {
                throw new RuntimeException("error getting TileEntity at dim " + world.provider.getDimensionId() + " " + pos2.toString());
            }
        }
        return null;
    }
    
    public Direction getInverse() {
        return Direction.directions[this.ordinal() ^ 0x1];
    }
    
    public int toSideValue() {
        return (this.ordinal() + 4) % 6;
    }
    
    private int getSign() {
        return this.ordinal() % 2 * 2 - 1;
    }
    
    public EnumFacing toEnumFacing() {
        return EnumFacing.getFront(this.toSideValue());
    }
    
    static {
        directions = values();
    }
}
