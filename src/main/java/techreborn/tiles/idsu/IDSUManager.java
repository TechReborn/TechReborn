package techreborn.tiles.idsu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.event.world.WorldEvent;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.TreeMap;


public class IDSUManager {

	public static IDSUManager INSTANCE;

	public static final String savename = "idsu.json";

	public HashMap<World, IDSUWorldSaveData> worldData = new HashMap<World, IDSUWorldSaveData>();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void worldSave(WorldEvent.Save event) {
		if (event.world != null && event.world.getSaveHandler() != null && event.world.getSaveHandler().getWorldDirectory() != null) {
			if (worldData.containsKey(event.world)) {
				worldData.get(event.world).save();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void worldLoad(WorldEvent.Load event) {
		if (event.world != null && event.world.getSaveHandler() != null && event.world.getSaveHandler().getWorldDirectory() != null) {
			if (worldData.containsKey(event.world)) {
				worldData.get(event.world).load();
			} else {
				IDSUWorldSaveData worldSaveData = new IDSUWorldSaveData(event.world);
				worldData.put(event.world, worldSaveData);
				worldSaveData.load();
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void worldClosed(WorldEvent.Unload event) {
		if (event.world != null && event.world.getSaveHandler() != null && event.world.getSaveHandler().getWorldDirectory() != null) {
			if (worldData.containsKey(event.world)) {
				worldData.get(event.world).save();
			}
		}
		//this clears the data ready for a new world
		worldData.clear();
	}

	public IDSUValueSaveData getSaveDataForWorld(World world, String channel) {
		if (worldData.containsKey(world)) {
			return worldData.get(world).getSaves(channel);
		} else {
			IDSUWorldSaveData worldSaveData = new IDSUWorldSaveData(world);
			worldData.put(world, worldSaveData);
			worldSaveData.load();
			return worldSaveData.getSaves(channel);
		}
	}


	public IDSUWorldSaveData getWorldDataFormWorld(World world) {
		if (worldData.containsKey(world)) {
			return worldData.get(world);
		} else {
			IDSUWorldSaveData worldSaveData = new IDSUWorldSaveData(world);
			worldData.put(world, worldSaveData);
			worldSaveData.load();
			return worldSaveData;
		}
	}


	public void loadFromString(String json, World world) {
		if (json.equals("EMPTY")) {
			return;
		}
		IDSUWorldSaveData worldSaveData;
		if (worldData.containsKey(world)) {
			worldSaveData = worldData.get(world);
		} else {
			worldSaveData = new IDSUWorldSaveData(world);
			worldData.put(world, worldSaveData);
		}
		Gson gson = new Gson();
		Type typeOfHashMap = new TypeToken<TreeMap<Integer, IDSUValueSaveData>>() {
		}.getType();
		worldSaveData.idsuValues.clear();
		worldSaveData.idsuValues = gson.fromJson(json, typeOfHashMap);
	}


	public class IDSUWorldSaveData {

		public TreeMap<String, IDSUValueSaveData> idsuValues = new TreeMap<String, IDSUValueSaveData>();

		public World world;

		ISaveHandler saveHandler;

		File folder;

		File file;

		public IDSUWorldSaveData(World world) {
			this.world = world;
			this.saveHandler = world.getSaveHandler();
			folder = new File(saveHandler.getWorldDirectory(), "idsuData");
			file = new File(folder, savename);
		}

		public IDSUValueSaveData getSaves(String udid) {
			if (idsuValues.containsKey(udid)) {
				return idsuValues.get(udid);
			} else {
				IDSUValueSaveData data = new IDSUValueSaveData();
				idsuValues.put(udid, data);
				return data;
			}
		}

		public void load() {
			if (!file.exists()) {
				return;
			}
			try {
				Gson gson = new Gson();
				BufferedReader reader = new BufferedReader(new FileReader(file));
				Type typeOfHashMap = new TypeToken<TreeMap<Integer, IDSUValueSaveData>>() {
				}.getType();
				idsuValues.clear();
				idsuValues = gson.fromJson(reader, typeOfHashMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void save() {
			if (idsuValues.isEmpty()) {
				return;
			}
			if (!file.exists()) {
				if (!folder.exists()) {
					folder.mkdirs();
				}
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(idsuValues);
			try {
				FileWriter writer = new FileWriter(file);
				writer.write(json);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public class IDSUValueSaveData {

		public double storedPower = 0;

		public String name = "";

		public IDSUValueSaveData(double storedPower, String name) {
			this.storedPower = storedPower;
			this.name = name;
		}


		public IDSUValueSaveData(double storedPower) {
			this.storedPower = storedPower;
		}

		public IDSUValueSaveData() {
		}

		public double getStoredPower() {
			return storedPower;
		}

		public void setStoredPower(double storedPower) {
			this.storedPower = storedPower;
		}

		public void addEnergy(double storedPower) {
			this.storedPower += storedPower;
		}
	}
}
