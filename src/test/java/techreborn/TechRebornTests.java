package techreborn;

import org.junit.Test;
import reborncore.common.util.VersionChecker;
import techreborn.lib.ModInfo;

import java.io.IOException;

public class TechRebornTests {

    @Test
    public void versionChecker() throws IOException {
        VersionChecker versionChecker = new VersionChecker("TechReborn", new ModInfo());
        versionChecker.checkVersion();
        versionChecker.getChangeLogSinceCurrentVersion();
    }

}
