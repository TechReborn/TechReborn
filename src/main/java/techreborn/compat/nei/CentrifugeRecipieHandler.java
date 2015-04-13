package techreborn.compat.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;

public class CentrifugeRecipieHandler extends TemplateRecipeHandler{

	@Override
	public String getRecipeName() 
	{
		return "techreborn.centrifuge.recipe";
	}

	@Override
	public String getGuiTexture() 
	{
		return "techreborn:textures/gui/centrifuge.png";
	}

}
