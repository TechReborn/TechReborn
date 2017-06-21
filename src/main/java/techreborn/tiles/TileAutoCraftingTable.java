package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;

import javax.annotation.Nullable;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class TileAutoCraftingTable extends TilePowerAcceptor implements IContainerProvider, IInventoryProvider {

	ResourceLocation currentRecipe;

	public Inventory inventory = new Inventory(10, "TileAutoCraftingTable", 64, this);

	public void setCurrentRecipe(ResourceLocation recipe){
		currentRecipe = recipe;
	}

	@Nullable
	public IRecipe getIRecipe(){
		if(currentRecipe == null){
			return null;
		}
		return ForgeRegistries.RECIPES.getValue(currentRecipe);
	}

	public TileAutoCraftingTable() {
		super();
	}

	@Override
	public double getBaseMaxPower() {
		return 10000;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 32;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing enumFacing) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public BuiltContainer createContainer(EntityPlayer player) {
		return new ContainerBuilder("autocraftingTable").player(player.inventory).inventory().hotbar()
			.addInventory().tile(this)
			.slot(0, 28, 25).slot(1, 46, 25).slot(2, 64, 25)
			.slot(3, 28, 43).slot(4, 46, 43).slot(5, 64, 43)
			.slot(6, 28, 61).slot(7, 46, 61).slot(8, 64, 61)
			.outputSlot(9, 145, 42).addInventory()
			.create();
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}
}
