package techreborn.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import techreborn.init.ModSounds;

public class TileAlarm extends TileEntity  implements ITickable{
	@Override
	public void update() {
		if(!world.isRemote && world.getTotalWorldTime() % 25 == 0 && world.isBlockPowered(getPos())){
			world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), ModSounds.ALARM, SoundCategory.BLOCKS, 4F, 1F);
		}
	}
}
