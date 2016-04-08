package techreborn.manual.saveFormat;

import java.util.List;

/**
 * Created by Mark on 05/04/2016.
 */
public class ManualFormat {

    public String name;

    public String modId;

    public List<Entry> entries;

    public ManualFormat(String name, String modId, List<Entry> entries) {
        this.name = name;
        this.modId = modId;
        this.entries = entries;
    }

    public ManualFormat() {
    }
}
