package techreborn;

import org.junit.Test;
import techreborn.util.VersionChecker;

import java.io.IOException;

public class TechRebornTests {

    @Test
    public void versionChecker() throws IOException {
        VersionChecker versionChecker = new VersionChecker("TechReborn");
        versionChecker.checkVersion();
        versionChecker.getChangeLogSinceCurrentVersion();
    }

}
