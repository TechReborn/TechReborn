package techreborn.tiles.iesu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraftforge.event.world.WorldEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class IDSUManager {


	public static ArrayList<IDSUWorldSaveData> worldData = new ArrayList<IDSUWorldSaveData>();

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void worldSave(WorldEvent.Save event){
		if(event.world != null && event.world.getSaveHandler() != null && event.world.getSaveHandler().getWorldDirectory() != null){
			for(IDSUWorldSaveData saveData : worldData){
				if(saveData.saveHandler == event.world.getSaveHandler()){
					saveData.save();
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void worldLoad(WorldEvent.Load event){
		if(event.world != null && event.world.getSaveHandler() != null && event.world.getSaveHandler().getWorldDirectory() != null){
			for(IDSUWorldSaveData saveData : worldData){
				if(saveData.saveHandler == event.world.getSaveHandler()){
					saveData.load();
					return;
				}
			}
			IDSUWorldSaveData worldSaveData = new IDSUWorldSaveData(event.world);
			worldData.add(worldSaveData);
			worldSaveData.load();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void worldClosed(WorldEvent.Unload event){
		if(event.world != null && event.world.getSaveHandler() != null && event.world.getSaveHandler().getWorldDirectory() != null){
			for(IDSUWorldSaveData saveData : worldData){
				if(saveData.saveHandler == event.world.getSaveHandler()){
					saveData.save();
				}
			}
		}
		worldData.clear();
	}


	public class IDSUWorldSaveData {

		public HashMap<Integer, IDSUValueSaveData> idsuValues = new HashMap<Integer, IDSUValueSaveData>();

		public World world;

		ISaveHandler saveHandler;

		File folder;

		File file;

		public IDSUWorldSaveData(World world) {
			this.world = world;
			this.saveHandler = world.getSaveHandler();
			folder = new File(saveHandler.getWorldDirectory(), "iesuData");
			file = new File(folder,"idsu.json");
			idsuValues.put(0, new IDSUValueSaveData());
		}

		public IDSUValueSaveData getSaves(int i){
			if(idsuValues.containsKey(i)){
				return idsuValues.get(i);
			}
			return null;
		}

		public void load(){
			//TODO load values from file
			if(!file.exists()){
				return;
			}
			try {
				Gson gson = new Gson();
				BufferedReader reader = new BufferedReader(new FileReader(file));
				Type typeOfHashMap = new TypeToken<Map<Integer, IDSUValueSaveData>>() { }.getType();
				idsuValues = gson.fromJson(reader, typeOfHashMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void save(){
			if(idsuValues.isEmpty()){
				return;
			}
			if(!file.exists()){
				if(!folder.exists()){
					folder.mkdirs();
				}
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Gson gson = new Gson();
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


		public int id = 1;



	}
}
