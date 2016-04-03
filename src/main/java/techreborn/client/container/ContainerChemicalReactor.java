package techreborn.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import reborncore.client.gui.SlotOutput;
import techreborn.api.gui.SlotUpgrade;
import techreborn.tiles.TileChemicalReactor;

public class ContainerChemicalReactor extends ContainerCrafting
{

	public int tickTime;
	EntityPlayer player;
	TileChemicalReactor tile;

	public ContainerChemicalReactor(TileChemicalReactor tilechemicalReactor, EntityPlayer player)
	{
		super(tilechemicalReactor.crafter);
		tile = tilechemicalReactor;
		this.player = player;

		// input
		this.addSlotToContainer(new Slot(tilechemicalReactor.inventory, 0, 70, 21));
		this.addSlotToContainer(new Slot(tilechemicalReactor.inventory, 1, 90, 21));
		// outputs
		this.addSlotToContainer(new SlotOutput(tilechemicalReactor.inventory, 2, 80, 51));
		// battery
		this.addSlotToContainer(new Slot(tilechemicalReactor.inventory, 3, 8, 51));
		// upgrades
		this.addSlotToContainer(new SlotUpgrade(tilechemicalReactor.inventory, 4, 152, 8));
		this.addSlotToContainer(new SlotUpgrade(tilechemicalReactor.inventory, 5, 152, 26));
		this.addSlotToContainer(new SlotUpgrade(tilechemicalReactor.inventory, 6, 152, 44));
		this.addSlotToContainer(new SlotUpgrade(tilechemicalReactor.inventory, 7, 152, 62));

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

}
