package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
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
	public int progress;
	public int maxProgress = 120;
	public int euTick = 10;
	public Pair<ResourceLocation, IRecipe> cachedRecipe;

	public void setCurrentRecipe(ResourceLocation recipe) {
		currentRecipe = recipe;
	}

	@Nullable
	public IRecipe getIRecipe() {
		if (currentRecipe == null) {
			return null;
		}
		if (cachedRecipe == null || !cachedRecipe.getLeft().equals(currentRecipe)) {
			IRecipe recipe = ForgeRegistries.RECIPES.getValue(currentRecipe);
			if (recipe != null) {
				cachedRecipe = Pair.of(currentRecipe, recipe);
				return recipe;
			}
			cachedRecipe = null;
			return null;
		}
		return cachedRecipe.getRight();
	}

	@Override
	public void update() {
		super.update();
		IRecipe recipe = getIRecipe();
		if (recipe != null) {
			if (progress >= maxProgress) {
				if (make(recipe)) {
					progress = 0;
				}
			} else {
				if (canMake(recipe)) {
					if (canUseEnergy(euTick)) {
						progress++;
						useEnergy(euTick);
					}
				}
			}
		}
	}

	public boolean canMake(IRecipe recipe) {
		if (recipe.canFit(3, 3)) {
			boolean missingOutput = false;
			int[] stacksInSlots = new int[9];
			for (int i = 0; i < 9; i++) {
				stacksInSlots[i] = inventory.getStackInSlot(i).getCount();
			}
			for (Ingredient ingredient : recipe.getIngredients()) {
				if (ingredient != Ingredient.EMPTY) {
					boolean foundIngredient = false;
					for (int i = 0; i < 9; i++) {
						ItemStack stack = inventory.getStackInSlot(i);
						if(stacksInSlots[i] > 0){
							if (ingredient.apply(stack)) {
								foundIngredient = true;
								stacksInSlots[i] --;
								break;
							}
						}
					}
					if(!foundIngredient){
						missingOutput = true;
					}
				}
			}
			if (!missingOutput) {
				if (hasOutputSpace(recipe.getRecipeOutput())) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public boolean hasOutputSpace(ItemStack output) {
		ItemStack stack = inventory.getStackInSlot(9);
		if (stack.isEmpty()) {
			return true;
		}
		if (ItemUtils.isItemEqual(stack, output, true, true)) {
			if (stack.getMaxStackSize() < stack.getCount() + output.getCount()) {
				return true;
			}
		}
		return false;
	}

	public boolean make(IRecipe recipe) {
		if (canMake(recipe)) {
			for (Ingredient ingredient : recipe.getIngredients()) {
				for (int i = 0; i < 9; i++) {
					ItemStack stack = inventory.getStackInSlot(i);
					if (ingredient.apply(stack)) {
						stack.shrink(1); //TODO is this right? or do I need to use it as an actull crafting grid
						break;
					}
				}
			}
			ItemStack output = inventory.getStackInSlot(9);
			if (output.isEmpty()) {
				inventory.setInventorySlotContents(9, recipe.getRecipeOutput().copy());
			} else {
				output.grow(recipe.getRecipeOutput().getCount());
			}
			return true;
		}
		return false;
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
			.outputSlot(9, 145, 42)
			.syncIntegerValue(this::getProgress, this::setProgress)
			.syncIntegerValue(this::getMaxProgress, this::setMaxProgress)
			.addInventory().create();
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getMaxProgress() {
		if (maxProgress == 0) {
			maxProgress = 1;
		}
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}
}
