package techreborn.tiles;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import techreborn.blocks.BlockFlare;
import techreborn.client.particle.ParticleSmoke;

import java.util.Random;

/**
 * Created by modmuss50 on 06/11/2016.
 */
public class TileEntityFlare extends TileEntity implements ITickable {

	Random random = new Random();

	@Override
	public void update() {
		EnumDyeColor color = worldObj.getBlockState(pos).getValue(BlockFlare.COLOR);
		if(worldObj.isRemote && worldObj.isAirBlock(getPos().up())){
			ParticleSmoke particleSmokeLarge = new ParticleSmoke(worldObj , pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0.0D, 0);
			particleSmokeLarge.setMaxAge(250);
			if(color != EnumDyeColor.WHITE){
				float[] rgb = EntitySheep.getDyeRgb(color);
				particleSmokeLarge.setRBGColorF(rgb[0] + (random.nextFloat() /20), rgb[1] + (random.nextFloat() /20), rgb[2] + (random.nextFloat() /20));
			}
			particleSmokeLarge.multipleParticleScaleBy(0.5F);

			Minecraft.getMinecraft().effectRenderer.addEffect(particleSmokeLarge);

			worldObj.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}
	}


}
