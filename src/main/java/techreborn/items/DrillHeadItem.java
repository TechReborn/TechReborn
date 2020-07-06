package techreborn.items;

import net.minecraft.item.Item;

public class DrillHeadItem extends Item {
	public int range;

	// Consume variables
	public double energyPerBlock;
	public int fluidPerBlock;
	public int durabilityPerBlock;
	public double speedFactor;

	// Modifiers
	public boolean silkTouch = false;
	public boolean voidTrash = false;
	public boolean voidAll = false;
	public int fortune = 0;


	public DrillHeadItem(Settings settings, int range, double energyPerBlock, int fluidPerBlock, int durabilityPerBlock, double speedFactor) {
		super(settings);
		this.range = range;
		this.energyPerBlock = energyPerBlock;
		this.fluidPerBlock = fluidPerBlock;
		this.durabilityPerBlock = durabilityPerBlock;
		this.speedFactor = speedFactor;
	}


	// Modifier constructor
	public DrillHeadItem(Settings settings, int range, double energyPerBlock, int fluidPerBlock, int durabilityPerBlock, int speedFactor,
						 boolean silkTouch, boolean voidTrash, int fortune, boolean voidAll) {
		this(settings, range, energyPerBlock, fluidPerBlock, durabilityPerBlock, speedFactor);
		this.silkTouch = silkTouch;
		this.voidTrash = voidTrash;
		this.fortune = fortune;
		this.voidAll = voidAll;
	}
}
