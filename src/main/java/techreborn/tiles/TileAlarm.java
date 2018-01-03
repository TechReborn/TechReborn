package techreborn.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import techreborn.blocks.BlockAlarm;
import techreborn.init.ModSounds;

public class TileAlarm extends TileEntity  implements ITickable {
boolean state = false;
int selectedSound;
    @Override
    public void update() {
        if (!world.isRemote && world.getTotalWorldTime() % 25 == 0 && world.isBlockPowered(getPos())) {
            BlockAlarm.setActive(true, world, pos);
            state = true;
        } else if(!world.isRemote && world.getTotalWorldTime() % 25 == 0 ) {
            BlockAlarm.setActive(false, world, pos);
            state = false;
        }
    }
}
