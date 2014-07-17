package im.aktive.aktive.serializer;

import im.aktive.aktive.manager.ATActivityManager;
import im.aktive.aktive.model.ATActivity;

/**
 * Created by hoangtran on 17/7/14.
 */
public class ATActivitySerializer extends ATBaseSerializer<ATActivitySerializer, ATActivity>{
    public int id;
    public String name;
    public String description;
    @Override
    public ATActivity toObject() {
        ATActivity activity = ATActivityManager.getInstance().getOrCreateActivity(id);
        activity.setName(name);
        activity.setDescription(description);
        return activity;
    }
}
