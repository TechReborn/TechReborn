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
		ModelBuilder modelBuilder = new ModelBuilder(outputDir)

			.model("techreborn:grdiner")
			.build()

			.generate();
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}
}
