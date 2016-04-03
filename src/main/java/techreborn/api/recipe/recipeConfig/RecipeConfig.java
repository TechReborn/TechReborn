package techreborn.api.recipe.recipeConfig;

import java.util.ArrayList;

public class RecipeConfig {

    ArrayList<ConfigItem> inputs;

    ArrayList<ConfigItem> outputs;

    Boolean enabled;

    String machine;

    public ArrayList<ConfigItem> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<ConfigItem> inputs) {
        this.inputs = inputs;
    }

    public ArrayList<ConfigItem> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<ConfigItem> outputs) {
        this.outputs = outputs;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public void addInputs(ConfigItem item) {
        if (inputs == null) {
            inputs = new ArrayList<>();
        }
        inputs.add(item);
    }

    public void addOutputs(ConfigItem item) {
        if (outputs == null) {
            outputs = new ArrayList<>();
        }
        outputs.add(item);
    }
}
