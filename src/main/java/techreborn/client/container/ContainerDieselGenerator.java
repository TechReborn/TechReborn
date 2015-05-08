package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import techreborn.client.SlotFake;
import techreborn.client.SlotOutput;
import techreborn.tiles.TileDieselGenerator;
import techreborn.tiles.TileThermalGenerator;

public class ContainerDieselGenerator extends TechRebornContainer {
	public TileDieselGenerator tiledieselGenerator;
	public EntityPlayer player;

	public ContainerDieselGenerator(TileDieselGenerator tiledieselGenerator,
			EntityPlayer player)
	{
		super();
		this.tiledieselGenerator = tiledieselGenerator;
		this.player = player;

		this.addSlotToContainer(new Slot(tiledieselGenerator.inventory, 0, 80,
				17));
		this.addSlotToContainer(new SlotOutput(tiledieselGenerator.inventory,
				1, 80, 53));
		this.addSlotToContainer(new SlotFake(tiledieselGenerator.inventory, 2,
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
