package techreborn.blockentity.conduit;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Pair;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import reborncore.common.network.ClientBoundPackets;
import reborncore.common.network.NetworkManager;
import reborncore.common.util.IDebuggable;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseConduit extends BlockEntity implements Tickable, IDebuggable {

	// Neighbouring conduits which are we are connected to
	protected final Map<Direction, IConduit> conduits = new HashMap<>();

	// Speciality faces
	protected final Map<Direction, ConduitMode> functionalFaces = new HashMap<>();

	private int ticktime = 0;

	// Round robin variable
	private int outputIndex = 0;

	public BaseConduit(BlockEntityType<?> type) {
		super(type);
	}

	// Abstract functions
	abstract void importFace(Direction face);
	abstract void exportFace(Direction face);


	protected void onServerLoad(){
		// Conduits populating if read from NBT, note will be conduit type, checked elsewhere
		for(Direction direction : conduits.keySet()){
			if(world == null) return;

			BlockEntity entity = world.getBlockEntity(this.pos.offset(direction));

			if(entity instanceof IConduit){
				conduits.put(direction, (IConduit) entity);
			}else {
				conduits.remove(direction);
			}
		}
	}

	protected void clientTick(){

	}

	protected void serverTick(){
		if(ticktime == 0){
			onServerLoad();
		}
		ticktime++;

		// Loop through each mode and perform action
		for (Map.Entry<Direction, ConduitMode> entry : getFunctionalFaces().entrySet()) {

			switch (entry.getValue()){
				case INPUT:
					importFace(entry.getKey());
					break;
				case OUTPUT:
					exportFace(entry.getKey());
					break;
				case BLOCK:
					// No functionality
					break;
			}
		}

	}

	@Override
	public void tick() {
		if(world == null){
			return;
		}

		if(world.isClient()){
			clientTick();
		}else{
			serverTick();
		}
	}

	public void addConduit(Direction direction, IConduit conduit){
		conduits.put(direction, conduit);
	}

	public void removeConduit(Direction direction){
		conduits.remove(direction);
	}

	// Returns a direction which is next up for transfer
	Pair<IConduit, Direction> getDestinationConduit(Direction from) {
		if (conduits.isEmpty()) {
			return null;
		}

		HashMap<Direction, IConduit> tempConduit = new HashMap<>(conduits);

		// Don't send to where we've received.
		tempConduit.remove(from);

		// If pipe's changed or round robin round is finished, reset index
		if(outputIndex >= tempConduit.size()){
			outputIndex = 0;
		}

		// Round robin crap
		int position = 0;

		for (Map.Entry<Direction, IConduit> entry : tempConduit.entrySet()) {
			if(position == outputIndex) {
				outputIndex++;
				return new Pair<>(entry.getValue(), entry.getKey());
			}

			// Increment if not right index
			position++;
		}

		return null;
	}

	// Returns true if can connect to that direction from this entity
	public boolean canConnect(Direction direction){

		// Can't connect to direction which has a IO/Block mode
		if(getFunctionalFaces().containsKey(direction)){
			return false;
		}

		return true;
	}

	// Cycles the mode
	public void changeMode(Direction face){
		if(getFunctionalFaces().containsKey(face)){
			ConduitMode prevMode = getFunctionalFaces().get(face);
			getFunctionalFaces().remove(face);
			switch (prevMode){
				case OUTPUT:
					getFunctionalFaces().put(face, ConduitMode.INPUT);
					break;
				case INPUT:
					getFunctionalFaces().put(face, ConduitMode.BLOCK);
					break;
				case BLOCK:
					// Don't do anything, will remove
					break;
			}

		}else{
			getFunctionalFaces().put(face,ConduitMode.OUTPUT);
		}

	}

	// Returns a map containing faces which are functional (IO/specials)
	public Map<Direction, ConduitMode> getFunctionalFaces() {
		return functionalFaces;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);

		if(!conduits.isEmpty()){
			ListTag conduitFacesList= new ListTag();

			int index = 0;
			for (Map.Entry<Direction, IConduit> entry : conduits.entrySet()) {
				CompoundTag sidedConduit = new CompoundTag();
				sidedConduit.putInt("direction", entry.getKey().getId());

				conduitFacesList.add(index, sidedConduit);

				index++;
			}

			tag.put("conduit", conduitFacesList);
		}

		if(!getFunctionalFaces().isEmpty()){
			ListTag functionalFacesList = new ListTag();

			int index = 0;
			for (Map.Entry<Direction, ConduitMode> entry : getFunctionalFaces().entrySet()) {
				CompoundTag sidedIO = new CompoundTag();
				sidedIO.putInt("direction", entry.getKey().getId());
				sidedIO.putInt("mode", entry.getValue().ordinal());

				functionalFacesList.add(index, sidedIO);

				index++;
			}

			tag.put("functional", functionalFacesList);
		}

		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);

		conduits.clear();
		getFunctionalFaces().clear();

		if(tag.contains("functional")){
			ListTag IOList = tag.getList("functional", NbtType.COMPOUND);

			for (int i = 0; i < IOList.size(); i++) {
				CompoundTag compoundTag = IOList.getCompound(i);

				Direction direction = Direction.byId(compoundTag.getInt("direction"));
				ConduitMode conduitMode = ConduitMode.values()[compoundTag.getInt("mode")];

				getFunctionalFaces().put(direction, conduitMode);
			}
		}

		if(tag.contains("conduit")){
			ListTag conduitList = tag.getList("conduit", NbtType.COMPOUND);

			for (int i = 0; i < conduitList.size(); i++) {
				CompoundTag compoundTag = conduitList.getCompound(i);
				conduits.put(Direction.byId(compoundTag.getInt("direction")), null);

			}
		}
	}

	@Override
	public String getDebugText() {
		String s = "";
		s += IDebuggable.propertyFormat("Conduit count: ", conduits.size() + "\n");
		s += IDebuggable.propertyFormat("Functional faces count", functionalFaces.size() + "\n");

		if(functionalFaces.size() > 0){
			s += IDebuggable.propertyFormat("Functional face (0)", functionalFaces.values().iterator().next() + "\n");
		}

		s += IDebuggable.propertyFormat("OutputIndex: ", String.valueOf(outputIndex)) + "\n";

		return s;
	}

	protected void sync() {
		NetworkManager.sendToTracking(ClientBoundPackets.createCustomDescriptionPacket(this), this);
	}
}
