package techreborn.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.TechReborn;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.init.TRContent.StorageUnit;
import techreborn.init.TRContent.TankUnit;

public class UpgraderItem extends Item {

	public UpgraderItem() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockEntity oldBlockEntity = world.getBlockEntity(blockPos);
		if (oldBlockEntity == null){
			return ActionResult.PASS;
		}
		if (!(oldBlockEntity instanceof StorageUnitBaseBlockEntity) && !(oldBlockEntity instanceof TankUnitBaseBlockEntity)){
			return ActionResult.PASS;
		}
		BlockState oldBlockState = world.getBlockState(blockPos);
		BlockState newBlockState = null;
		String newType = "";
		// if no storage upgrader, the isOf compares with null and returns false
		if (oldBlockState.isOf(StorageUnit.getUpgradableFor(this).map(StorageUnit::asBlock).orElse(null))) {
			// upgradable is now guaranteed to be present, or something is seriously wrong
			// we want to get the next unit in the enum, hence ordinal()+1
			newBlockState = StorageUnit.values()[StorageUnit.getUpgradableFor(this).orElseThrow().ordinal()+1].asBlock().getStateWithProperties(oldBlockState);
			newType = StorageUnit.values()[StorageUnit.getUpgradableFor(this).orElseThrow().ordinal()+1].name();
		}
		// same for the tank
		else if (oldBlockState.isOf(TankUnit.getUpgradableFor(this).map(TankUnit::asBlock).orElse(null))) {
			newBlockState = TankUnit.values()[TankUnit.getUpgradableFor(this).orElseThrow().ordinal()+1].asBlock().getDefaultState();
			newType = TankUnit.values()[TankUnit.getUpgradableFor(this).orElseThrow().ordinal()+1].name();
		}
		if (newBlockState == null) {
			return ActionResult.PASS;
		}

		NbtCompound data = oldBlockEntity.createNbt();
		data.putString("unitType", newType);

		// empty storage to prevent item spill
		oldBlockEntity.readNbt(new NbtCompound());

		world.setBlockState(blockPos, newBlockState);

		// restore content and set a new storage type
		BlockEntity newBlockEntity = world.getBlockEntity(blockPos);
		if (newBlockEntity != null){
			newBlockEntity.readNbt(data);
			((MachineBaseBlockEntity) newBlockEntity).syncWithAll();
		}

		ItemStack stack = context.getStack();
		stack.decrement(1);

		return ActionResult.SUCCESS;
	}
}
