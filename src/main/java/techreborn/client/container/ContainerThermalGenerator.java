package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotFake;
import techreborn.client.SlotFluid;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileThermalGenerator;

public class ContainerThermalGenerator extends TechRebornContainer {
	public TileThermalGenerator tileThermalGenerator;
	public EntityPlayer player;

	public ContainerThermalGenerator(TileThermalGenerator tileThermalGenerator,
			EntityPlayer player)
	{
		super();
		this.tileThermalGenerator = tileThermalGenerator;
		this.player = player;

		this.addSlotToContainer(new SlotFluid(tileThermalGenerator.inventory, 0, 80,
				17));
		this.addSlotToContainer(new SlotOutput(tileThermalGenerator.inventory,
				1, 80, 53));
		this.addSlotToContainer(new SlotFake(tileThermalGenerator.inventory, 2,
				59, 42, false, false, 1));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9
						+ 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18,
					142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
}
