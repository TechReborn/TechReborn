/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
