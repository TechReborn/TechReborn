package techreborn.blockentity.machine.multiblock.structure;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Tickable;
import reborncore.common.util.ChatUtils;
import techreborn.init.ModSounds;
import techreborn.init.TRBlockEntities;

public class DrillHeadBlockEntity extends BlockEntity implements Tickable {
	public boolean isActive = true;
	public float drillAngle;
	public float spinSpeed;
	public DrillHeadBlockEntity(){
		super(TRBlockEntities.DRILL_HEAD);
	}

	@Override
	public void tick() {
		isActive = true;
		if(world.isClient) {
			drillAngle += spinSpeed;

			if (isActive) {
				spinSpeed = Math.min(0.6F, spinSpeed + 0.015f);
			} else {
				spinSpeed = Math.max(0.0f, spinSpeed - 0.020f);
			}
		}

		if (world.getTime() % 60 != 0) {
			return;
		}

		if(isActive && !world.isClient){
			// Number between 0.5 and 0.6
			float pitch =  0.5f + world.random.nextFloat() * (0.6f - 0.5f);
			System.out.println(pitch);
			ChatUtils.sendNoSpamMessages(1,new LiteralText(pitch + " "));
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.DRILLING, SoundCategory.BLOCKS, 1F, pitch);
		}
	}
}
