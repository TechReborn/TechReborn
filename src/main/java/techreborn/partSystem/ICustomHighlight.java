package techreborn.partSystem;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public interface ICustomHighlight {

    ArrayList<AxisAlignedBB> getBoxes(World world, int x, int y, int z,
                                      EntityPlayer player);

}
