package techreborn.build.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MachineModelBuilder {

	private ModelBuilder.Model model;
	Consumer<MachineTextures> textureConsumer;

	MachineModelBuilder(ModelBuilder.Model model, Consumer<MachineTextures> textureConsumer) {
		this.model = model;
		this.textureConsumer = textureConsumer;
	}

	public List<ModelBuilder.Component> getComponents(){
		//Loads the textures in
		MachineTextures textures = new MachineTextures(this);
		textureConsumer.accept(textures);

		return Collections.emptyList();
	}

	public static class MachineTextures {

		MachineModelBuilder modelBuilder;

		public String top_off;
		public String top_on;


		public String front_off;
		public String front_on;

		public String side_off;
		public String side_on;

		public String bottom_off;
		public String bottom_on;

		public MachineTextures(MachineModelBuilder modelBuilder) {
			this.modelBuilder = modelBuilder;
		}
	}


}
