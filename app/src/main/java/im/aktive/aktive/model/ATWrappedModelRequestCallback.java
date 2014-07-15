package im.aktive.aktive.model;

import java.lang.ref.WeakReference;

import im.aktive.aktive.api_requester.ATAPICallWrapper;

/**
 * Created by hoangtran on 14/7/14.
 */
public abstract class ATWrappedModelRequestCallback implements ATModelRequestCallback{
    private WeakReference<ATAPICallWrapper> mCallWrapperRef = null;

    public ATWrappedModelRequestCallback(ATAPICallWrapper callWrapper)
    {
        mCallWrapperRef = new WeakReference<ATAPICallWrapper>(callWrapper);
    }

    public WeakReference<ATAPICallWrapper> getCallWrapperRef()
    {
        return mCallWrapperRef;
    }
}
