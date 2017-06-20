package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;

import javax.annotation.Nullable;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class TileAutoCraftingTable extends TilePowerAcceptor implements IContainerProvider {

	ResourceLocation currentRecipe;

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
		return 0;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing enumFacing) {
		return false;
	}

	@Override
	public BuiltContainer createContainer(EntityPlayer player) {
		return new ContainerBuilder("autocraftingTable").player(player.inventory).inventory().hotbar().addInventory().create();
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
