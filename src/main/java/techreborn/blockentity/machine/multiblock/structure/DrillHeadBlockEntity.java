package techreborn.blockentity.machine.multiblock.structure;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Tickable;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;

public class DrillHeadBlockEntity extends BlockEntity implements Tickable {
	public float drillAngle;

	public boolean isActive;
	public float spinSpeed;

	public float MAX_SPEED = 0.6F;
	public float SPIN_UP_FACTOR = 0.015f;
	public float SPIN_DOWN_FACTOR = 0.020f;


	public DrillHeadBlockEntity(){
		super(TRBlockEntities.DRILL_HEAD);
	}

	@Override
	public void tick() {
		if(world.isClient) {
			drillAngle += spinSpeed;

			if (isActive) {
				spinSpeed = Math.min(MAX_SPEED, spinSpeed + SPIN_UP_FACTOR);
			} else {
				spinSpeed = Math.max(0.0f, spinSpeed - SPIN_DOWN_FACTOR);
			}
		}

		if (world.getTime() % 60 != 0) {
			return;
		}

		if(isActive && !world.isClient){
			// Number between 0.5 and 0.6
			float pitch =  0.5f + world.random.nextFloat() * (0.6f - 0.5f);
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.DRILLING, SoundCategory.BLOCKS, 1F, pitch);
		}
	}
}
