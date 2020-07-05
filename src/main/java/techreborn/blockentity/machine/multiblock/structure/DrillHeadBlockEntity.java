package techreborn.blockentity.machine.multiblock.structure;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.world.ClientWorld;
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

	private final MinecraftClient client;
	private final SoundManager soundManager;

	private PositionedSoundInstance soundInstance = null;


	public DrillHeadBlockEntity(){
		super(TRBlockEntities.DRILL_HEAD);

		client = MinecraftClient.getInstance();
		soundManager = client.getSoundManager();
	}

	@Override
	public void markRemoved() {
		super.markRemoved();

		if (soundInstance != null) {
			soundManager.stop(soundInstance);
			soundInstance = null;
		}
	}

	@Override
	public void tick() {
		if(world != null && world.isClient) {
			drillAngle += spinSpeed;

			if (isActive) {
				spinSpeed = Math.min(MAX_SPEED, spinSpeed + SPIN_UP_FACTOR);

				// Play sound
				if (world.getTime() % 55 == 0 || soundInstance == null) {
					if(soundInstance != null){
						soundManager.stop(soundInstance);
						soundInstance = null;
					}

					// Play drilling sound and record instance
					float pitch =  0.5f + world.random.nextFloat() * (0.6f - 0.5f);

					soundInstance = new PositionedSoundInstance(ModSounds.DRILLING, SoundCategory.BLOCKS, 0.6F, pitch,
							(double)pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);

					this.client.getSoundManager().play(soundInstance);
				}



			} else {
				spinSpeed = Math.max(0.0f, spinSpeed - SPIN_DOWN_FACTOR);

				// Stop the sound
				if(soundInstance != null){
					soundManager.stop(soundInstance);
					soundInstance = null;
				}

			}
		}
	}
}
