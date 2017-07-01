package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModSounds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 20/06/2017.
 */
public class TileAutoCraftingTable extends TilePowerAcceptor implements IContainerProvider, IInventoryProvider, ISidedInventory {

	ResourceLocation currentRecipe;

	public Inventory inventory = new Inventory(10, "TileAutoCraftingTable", 64, this);
	public int progress;
	public int maxProgress = 120;
	public int euTick = 10;
	public Pair<ResourceLocation, IRecipe> cachedRecipe;
	public boolean customRecipe = true;
	InventoryCrafting inventoryCrafting = null;
	IRecipe lastCustomRecipe = null;

	public void setCurrentRecipe(IRecipe recipe, boolean customRecipe) {
		if (recipe != null) {
			currentRecipe = recipe.getRegistryName();
		} else {
			currentRecipe = null;
		}

		this.customRecipe = customRecipe;
		cachedRecipe = null;
	}

	public void setCurrentRecipe(ResourceLocation recipe, boolean customRecipe) {
		currentRecipe = recipe;
		this.customRecipe = customRecipe;
		cachedRecipe = null;
	}

	@Nullable
	public IRecipe getIRecipe() {
		if (customRecipe) {
			InventoryCrafting crafting = getCraftingInventory();
			for (IRecipe testRecipe : CraftingManager.REGISTRY) {
				if (testRecipe.matches(crafting, world)) {
					return testRecipe;
				}
			}
		}
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

	public InventoryCrafting getCraftingInventory() {
		if (inventoryCrafting == null) {
			inventoryCrafting = new InventoryCrafting(new Container() {
				@Override
				public boolean canInteractWith(EntityPlayer playerIn) {
					return false;
				}
			}, 3, 3);
		}
		for (int i = 0; i < 9; i++) {
			inventoryCrafting.setInventorySlotContents(i, inventory.getStackInSlot(i));
		}
		return inventoryCrafting;
	}

	@Override
	public void update() {
		super.update();
		if (world.isRemote) {
			return;
		}
		IRecipe recipe = getIRecipe();
		if (recipe != null || customRecipe) {
			if (progress >= maxProgress) {
				if (make(recipe)) {
					progress = 0;
				}
			} else {
				if (canMake(recipe)) {
					if (canUseEnergy(euTick)) {
						progress++;
						if(progress == 1){
							world.playSound(null, pos.getX(), pos.getY(),
								pos.getZ(), ModSounds.AUTO_CRAFTING,
								SoundCategory.BLOCKS, 0.3F, 0.8F);
						}
						useEnergy(euTick);
					}
				}
			}
		}
		if (recipe == null) {
			progress = 0;
		}
	}

	public boolean canMake(IRecipe recipe) {
		if (customRecipe) {
			recipe = getIRecipe();
		}
		if (recipe != null && recipe.canFit(3, 3)) {
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
						int requiredSize = customRecipe ? 1 : 0;
						if (stacksInSlots[i] > requiredSize) {
							if (ingredient.apply(stack)) {
								foundIngredient = true;
								stacksInSlots[i]--;
								break;
							}
						}
					}
					if (!foundIngredient) {
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
			if (stack.getMaxStackSize() > stack.getCount() + output.getCount()) {
				return true;
			}
		}
		return false;
	}

	public boolean make(IRecipe recipe) {
		if (canMake(recipe)) {
			if (recipe == null && customRecipe) {
				if (lastCustomRecipe == null) {
					return false;
				}//Should be uptodate as we just set it in canMake
				recipe = lastCustomRecipe;
			}
			for (int i = 0; i < recipe.getIngredients().size(); i++) {
				Ingredient ingredient = recipe.getIngredients().get(i);
				//Looks for the best slot to take it from
				ItemStack bestSlot = inventory.getStackInSlot(i);
				if (ingredient.apply(bestSlot)) {
					bestSlot.shrink(1);
				} else {
					for (int j = 0; j < 9; j++) {
						ItemStack stack = inventory.getStackInSlot(j);
						if (ingredient.apply(stack)) {
							stack.shrink(1); //TODO is this right? or do I need to use it as an actull crafting grid
							//TOOD check items to be retuned
							break;
						}
					}
				}
			}
			ItemStack output = inventory.getStackInSlot(9);
			//TODO fire forge recipe event
			ItemStack ouputStack = recipe.getCraftingResult(getCraftingInventory());
			if (output.isEmpty()) {
				inventory.setInventorySlotContents(9, ouputStack.copy());
			} else {
				//TODO use ouputStack in someway?
				output.grow(recipe.getRecipeOutput().getCount());
			}
			return true;
		}
		return false;
	}

	public boolean hasIngredient(Ingredient ingredient) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if (ingredient.apply(stack)) {
				return true;
			}
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
			.outputSlot(9, 145, 42).syncEnergyValue()
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

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		if (currentRecipe != null) {
			tag.setString("currentRecipe", currentRecipe.toString());
		}
		tag.setBoolean("customRecipe", customRecipe);
		return super.writeToNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("currentRecipe")) {
			currentRecipe = new ResourceLocation(tag.getString("currentRecipe"));
		}
		customRecipe = tag.getBoolean("customRecipe");
		super.readFromNBT(tag);
	}

	public boolean isItemValidForRecipeSlot(IRecipe recipe, ItemStack stack, int slotID) {
		if (recipe == null) {
			return true;
		}
		int bestSlot = findBestSlotForStack(recipe, stack);
		if (bestSlot != -1) {
			return bestSlot == slotID;
		}
		return true;
	}

	public int findBestSlotForStack(IRecipe recipe, ItemStack stack) {
		if (recipe == null) {
			return -1;
		}
		List<Integer> possibleSlots = new ArrayList<>();
		for (int i = 0; i < recipe.getIngredients().size(); i++) {
			ItemStack stackInSlot = inventory.getStackInSlot(i);
			Ingredient ingredient = recipe.getIngredients().get(i);
			if (ingredient != Ingredient.EMPTY && ingredient.apply(stack)) {
				if (stackInSlot.isEmpty()) {
					possibleSlots.add(i);
				} else if (stackInSlot.getItem() == stack.getItem() && stackInSlot.getItemDamage() == stack.getItemDamage()) {
					if (stackInSlot.getMaxStackSize() >= stackInSlot.getCount() + stack.getCount()) {
						possibleSlots.add(i);
					}
				}
			}
		}
		//Slot, count
		Pair<Integer, Integer> smallestCount = null;
		for (Integer slot : possibleSlots) {
			ItemStack slotStack = inventory.getStackInSlot(slot);
			if (slotStack.isEmpty()) {
				return slot;
			}
			if (smallestCount == null) {
				smallestCount = Pair.of(slot, slotStack.getCount());
			} else if (smallestCount.getRight() >= slotStack.getCount()) {
				smallestCount = Pair.of(slot, slotStack.getCount());
			}
		}
		if (smallestCount != null) {
			return smallestCount.getLeft();
		}
		return -1;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		int bestSlot = findBestSlotForStack(getIRecipe(), stack);
		if (bestSlot != -1) {
			return index == bestSlot;
		}
		return super.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		if (index == 9) {
			return false;
		}
		int bestSlot = findBestSlotForStack(getIRecipe(), itemStackIn);
		if (bestSlot != -1) {
			return index == bestSlot;
		}
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 9;
	}
}
