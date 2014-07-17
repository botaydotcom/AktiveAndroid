package im.aktive.aktive.serializer;

import im.aktive.aktive.manager.ATActivityManager;
import im.aktive.aktive.manager.ATUserActivityManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATUserActivity;

/**
 * Created by hoangtran on 17/7/14.
 */
public class ATUserActivityInListSerializer extends
        ATBaseSerializer<ATUserActivityInListSerializer, ATUserActivity>{
    public int id;
    public String finish_time;
    public String deadline;
    public boolean is_done;
    public boolean is_success;
    public ATUserSimpleSerializer user = null;
    public ATActivitySerializer activity = null;

    @Override
    public ATUserActivity toObject()
    {
        ATUser anUser = ATUserManager.getInstance().updateFromSerializer(user);
        ATActivity anActivity = ATActivityManager.getInstance().updateFromSerializer(activity);
        ATUserActivity userActivity = ATUserActivityManager.getInstance().getOrCreateUserActivity(id, anUser, anActivity);
        //userActivity.setFinishTime();
        userActivity.setDone(is_done);
        userActivity.setSuccess(is_success);
        return userActivity;
    }
}
