package ic2.api.info;

import net.minecraft.world.World;

public interface IWindTicker
{
	/**
	 * Like on IC2 Exp the numbers are between 0-30 (0 is stillstanding, 30 = thundering/close to it)
	 */
	public int getWindStrenght(World world);
}
