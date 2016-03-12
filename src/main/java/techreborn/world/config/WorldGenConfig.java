package techreborn.world.config;

import java.util.List;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class WorldGenConfig {

    public boolean generateTechRebornFeatures = true;

    public boolean generateOres = true;

    public boolean retroGenOres = false;

    public List<OreConfig> overworldOres;

    public List<OreConfig> neatherOres;

    public List<OreConfig> endOres;

    public RubberTreeConfig rubberTreeConfig = new RubberTreeConfig();
}
