package im.aktive.aktive.model;

import java.util.Date;

/**
 * Created by hoangtran on 15/7/14.
 */

import java.util.Date;

import im.aktive.aktive.model.ATActivity;
import im.aktive.aktive.model.ATUser;

/**
 * Created by hoangtran on 15/7/14.
 */
public class ATUserActivity {
    private transient ATUser user;
    private transient ATActivity activity;
    private Date deadline;
    private int id;
    private boolean isSuccess;
    private boolean isDone;

    public ATUserActivity(int id, ATUser user, ATActivity activity)
    {
        this.user = user;
        this.activity = activity;
        this.id = id;
    }

    public ATActivity getActivity() {
        return activity;
    }

    public ATUser getUser() {
        return user;
    }

    public String getDeadLineForDisplay()
    {
        return "By tomorrow at 10am";
    }

    public int getId() {
        return id;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
