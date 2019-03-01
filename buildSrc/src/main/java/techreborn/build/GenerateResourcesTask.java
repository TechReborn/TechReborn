package techreborn.build;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import techreborn.build.model.ModelBuilder;

import java.io.File;
public class GenerateResourcesTask extends DefaultTask {

	private File outputDir;

	@TaskAction
	public void run(){
		System.out.println("Generating Resources for " + getProject().getName());

		new ModelBuilder(outputDir)
			.model("techreborn:grinder")
			.machine(texture -> {
				texture.top_on = "techreborn:block/machines/tier1_machines/grinder_top_on";
				texture.front_on = "techreborn:block/machines/tier1_machines/grinder_front_on";
				texture.side_on = "techreborn:block/machines/tier1_machines/machine_side";

				texture.top_off = "techreborn:block/machines/tier1_machines/grinder_top_off";
				texture.front_off = "techreborn:block/machines/tier1_machines/grinder_front_off";
				texture.side_off = "techreborn:block/machines/tier1_machines/machine_side";
			})
			.build()

			.model("techreborn:alloy_smelter")
			.machine(texture -> {
				texture.top_on = "techreborn:blocks/machines/tier1_machines/machine_top";
				texture.front_on = "techreborn:blocks/machines/tier1_machines/electric_alloy_smelter_front_on";
				texture.side_on = "techreborn:blocks/machines/tier1_machines/machine_side";
				texture.bottom_on = "techreborn:blocks/machines/tier1_machines/machine_bottom";

				texture.front_off = "techreborn:blocks/machines/tier1_machines/electric_alloy_smelter_front_off";

				//TODO possibly not have to do this
				texture.top_off = texture.top_on;
				texture.side_off = texture.side_on;
				texture.bottom_off = texture.bottom_on;
			})
			.build()

			//Generates the json files
			.generate();
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}
}
