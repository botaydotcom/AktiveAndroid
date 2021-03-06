package im.aktive.aktive.manager;

import java.util.HashMap;
import java.util.Map;

import im.aktive.aktive.model.ATTagValue;

/**
 * Created by hoangtran on 18/7/14.
 */
public class ATTagValueManager extends ATBaseManager<ATTagValue>{
    private static ATTagValueManager instance = null;
    private Map<Integer, ATTagValue> mapTagValue = new HashMap<Integer, ATTagValue>();
    private static final int SKIP_ID = -1;
    private static ATTagValue sSkipTagValue = null;

    private ATTagValueManager()
    {
    }

    public static ATTagValueManager getInstance() {
        if (instance == null)
        {
            instance = new ATTagValueManager();
        }
        return instance;
    }

    public ATTagValue getTagValue(int id) {
        return mapTagValue.get(Integer.valueOf(id));
    }

    public ATTagValue getOrCreateTagValue(int id) {
        ATTagValue tagValue = getTagValue(id);
        if (tagValue == null)
        {
            tagValue = new ATTagValue(id);
        }
        return tagValue;
    }

    public ATTagValue getSkipTagValue() {
        if (sSkipTagValue == null)
        {
            sSkipTagValue = new ATTagValue(SKIP_ID);
            sSkipTagValue.setName("Skip");
        }
        return sSkipTagValue;
    }
}
