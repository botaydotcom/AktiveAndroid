package im.aktive.aktive.serializer;

import im.aktive.aktive.manager.ATTagValueManager;
import im.aktive.aktive.model.ATTagValue;

/**
 * Created by hoangtran on 18/7/14.
 */
public class ATTagValueSerializer extends ATBaseSerializer<ATTagValueSerializer, ATTagValue>{
    public int id;
    public String name;
    public String description;


    @Override
    public ATTagValue toObject() {
        ATTagValue tagValue = ATTagValueManager.getInstance().getOrCreateTagValue(id);
        tagValue.setName(name);
        tagValue.setDescription(description);
        return tagValue;
    }
}
