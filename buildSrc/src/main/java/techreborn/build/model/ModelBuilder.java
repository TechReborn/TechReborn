package techreborn.build.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
		List<Component> components = new ArrayList<>();


		private Model(String name, ModelBuilder modelBuilder) {
			this.name = name;
			this.modelBuilder = modelBuilder;
		}

		public Model machine(Consumer<MachineModelBuilder.MachineTextures> textureConsumer){
			components.addAll(new MachineModelBuilder(this, textureConsumer).getComponents());
			return this;
		}

		public ModelBuilder build(){
			return modelBuilder;
		}
	}

	public static abstract class Component {

	}
}
