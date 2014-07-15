package im.aktive.aktive.model;

/**
 * Created by hoangtran on 13/7/14.
 */
public interface ATModelRequestCallback {
    public void onSuccess(Object object);
    public void onFailed(String msg);
}
