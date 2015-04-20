package techreborn.lib.vecmath;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.StringTokenizer;

public class Vecs3d {
    protected double x, y, z;
    protected World w = null;

    public Vecs3d(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vecs3d(double x, double y, double z, World w) {

        this(x, y, z);
        this.w = w;
    }

    public Vecs3d(TileEntity te) {

        this(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj());
    }

    public Vecs3d(Vec3 vec) {

        this(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vecs3d(Vec3 vec, World w) {

        this(vec.xCoord, vec.yCoord, vec.zCoord);
        this.w = w;
    }

    public boolean hasWorld() {

        return w != null;
    }

    public Vecs3d add(double x, double y, double z) {

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vecs3d add(ForgeDirection dir) {

        return add(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vecs3d add(Vecs3d vec) {

        return add(vec.x, vec.y, vec.z);
    }

    public Vecs3d sub(double x, double y, double z) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vecs3d sub(ForgeDirection dir) {

        return sub(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vecs3d sub(Vecs3d vec) {

        return sub(vec.x, vec.y, vec.z);
    }

    public Vecs3d mul(double x, double y, double z) {

        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vecs3d mul(double multiplier) {

        return mul(multiplier, multiplier, multiplier);
    }

    public Vecs3d mul(ForgeDirection direction) {

        return mul(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public Vecs3d multiply(Vecs3d v) {

        return mul(v.getX(), v.getY(), v.getZ());
    }

    public Vecs3d div(double x, double y, double z) {

        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public Vecs3d div(double multiplier) {

        return div(multiplier, multiplier, multiplier);
    }

    public Vecs3d div(ForgeDirection direction) {

        return div(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public double length() {

        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vecs3d normalize() {

        Vecs3d v = clone();

        double len = length();

        if (len == 0)
            return v;

        v.x /= len;
        v.y /= len;
        v.z /= len;

        return v;
    }

    public Vecs3d abs() {

        return new Vecs3d(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public double dot(Vecs3d v) {

        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public Vecs3d cross(Vecs3d v) {

        return new Vecs3d(y * v.getZ() - z * v.getY(), x * v.getZ() - z * v.getX(), x * v.getY() - y * v.getX());
    }

    public Vecs3d getRelative(double x, double y, double z) {

        return clone().add(x, y, z);
    }

    public Vecs3d getRelative(ForgeDirection dir) {

        return getRelative(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public ForgeDirection getDirectionTo(Vecs3d vec) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            if (getBlockX() + d.offsetX == vec.getBlockX() && getBlockY() + d.offsetY == vec.getBlockY()
                    && getBlockZ() + d.offsetZ == vec.getBlockZ())
                return d;
        return null;
    }

    public boolean isZero() {

        return x == 0 && y == 0 && z == 0;
    }

    @Override
    public Vecs3d clone() {

        return new Vecs3d(x, y, z, w);
    }

    public boolean hasTileEntity() {

        if (hasWorld()) {
            return w.getTileEntity((int) x, (int) y, (int) z) != null;
        }
        return false;
    }

    public TileEntity getTileEntity() {

        if (hasTileEntity()) {
            return w.getTileEntity((int) x, (int) y, (int) z);
        }
        return null;
    }

    public boolean isBlock(Block b) {

        return isBlock(b, false);
    }

    public boolean isBlock(Block b, boolean checkAir) {

        if (hasWorld()) {
            Block bl = w.getBlock((int) x, (int) y, (int) z);

            if (b == null && bl == Blocks.air)
                return true;
            if (b == null && checkAir && bl.getMaterial() == Material.air)
                return true;
            if (b == null && checkAir && bl.isAir(w, (int) x, (int) y, (int) z))
                return true;

            return bl.getClass().isInstance(b);
        }
        return false;
    }

    public int getBlockMeta() {

        if (hasWorld()) {
            return w.getBlockMetadata((int) x, (int) y, (int) z);
        }
        return -1;
    }

    public Block getBlock() {

        return getBlock(false);
    }

    public Block getBlock(boolean airIsNull) {

        if (hasWorld()) {
            if (airIsNull && isBlock(null, true))
                return null;
            return w.getBlock((int) x, (int) y, (int) z);

        }
        return null;
    }

    public World getWorld() {

        return w;
    }

    public Vecs3d setWorld(World world) {

        w = world;

        return this;
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    public double getZ() {

        return z;
    }

    public int getBlockX() {

        return (int) Math.floor(x);
    }

    public int getBlockY() {

        return (int) Math.floor(y);
    }

    public int getBlockZ() {

        return (int) Math.floor(z);
    }

    public double distanceTo(Vecs3d vec) {

        return distanceTo(vec.x, vec.y, vec.z);
    }

    public double distanceTo(double x, double y, double z) {

        double dx = x - this.x;
        double dy = y - this.y;
        double dz = z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public void setX(double x) {

        this.x = x;
    }

    public void setY(double y) {

        this.y = y;
    }

    public void setZ(double z) {

        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Vecs3d) {
            Vecs3d vec = (Vecs3d) obj;
            return vec.w == w && vec.x == x && vec.y == y && vec.z == z;
        }
        return false;
    }

    @Override
    public int hashCode() {

        return new Double(x).hashCode() + new Double(y).hashCode() << 8 + new Double(z).hashCode() << 16;
    }

    public Vec3 toVec3() {

        return Vec3.createVectorHelper(x, y, z);
    }

    @Override
    public String toString() {

        String s = "Vector3{";
        if (hasWorld())
            s += "w=" + w.provider.dimensionId + ";";
        s += "x=" + x + ";y=" + y + ";z=" + z + "}";
        return s;
    }

    public ForgeDirection toForgeDirection() {

        if (z == 1)
            return ForgeDirection.SOUTH;
        if (z == -1)
            return ForgeDirection.NORTH;

        if (x == 1)
            return ForgeDirection.EAST;
        if (x == -1)
            return ForgeDirection.WEST;

        if (y == 1)
            return ForgeDirection.UP;
        if (y == -1)
            return ForgeDirection.DOWN;

        return ForgeDirection.UNKNOWN;
    }

    public static Vecs3d fromString(String s) {

        if (s.startsWith("Vector3{") && s.endsWith("}")) {
            World w = null;
            double x = 0, y = 0, z = 0;
            String s2 = s.substring(s.indexOf("{") + 1, s.lastIndexOf("}"));
            StringTokenizer st = new StringTokenizer(s2, ";");
            while (st.hasMoreTokens()) {
                String t = st.nextToken();

                if (t.toLowerCase().startsWith("w")) {
                    int world = Integer.parseInt(t.split("=")[1]);
                    if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                        for (World wo : MinecraftServer.getServer().worldServers) {
                            if (wo.provider.dimensionId == world) {
                                w = wo;
                                break;
                            }
                        }
                    } else {
                        w = getClientWorld(world);
                    }
                }

                if (t.toLowerCase().startsWith("x"))
                    x = Double.parseDouble(t.split("=")[1]);
                if (t.toLowerCase().startsWith("y"))
                    y = Double.parseDouble(t.split("=")[1]);
                if (t.toLowerCase().startsWith("z"))
                    z = Double.parseDouble(t.split("=")[1]);
            }

            if (w != null) {
                return new Vecs3d(x, y, z, w);
            } else {
                return new Vecs3d(x, y, z);
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private static World getClientWorld(int world) {

        if (Minecraft.getMinecraft().theWorld.provider.dimensionId != world)
            return null;
        return Minecraft.getMinecraft().theWorld;
    }

}