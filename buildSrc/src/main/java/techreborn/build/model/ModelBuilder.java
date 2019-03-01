package techreborn.build.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModelBuilder {

	File outputDir;
	private List<Model> models = new ArrayList<>();

	public ModelBuilder(File outputDir) {
		this.outputDir = outputDir;
	}

	public ModelBuilder generate(){
		//TODO write out all the files
		return this;
	}

	public Model model(String name){
		Model model = new Model(name, this);
		models.add(model);
		return model;
	}

	public static class Model {
		String name;
		ModelBuilder modelBuilder;

		private Model(String name, ModelBuilder modelBuilder) {
			this.name = name;
			this.modelBuilder = modelBuilder;
		}

		public ModelBuilder build(){
			return modelBuilder;
		}
	}
}
