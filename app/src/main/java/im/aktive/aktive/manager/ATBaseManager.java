package im.aktive.aktive.manager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.aktive.aktive.ATApplication;
import im.aktive.aktive.api_requester.ATAPICallWrapper;
import im.aktive.aktive.model.ATModelUpdateCallback;
import im.aktive.aktive.model.ATModelRequestCallback;
import im.aktive.aktive.model.ATUser;
import im.aktive.aktive.model.ATWrappedModelRequestCallback;
import im.aktive.aktive.network.ATNetworkCallback;
import im.aktive.aktive.notification.ATGlobalEventDispatchManager;
import im.aktive.aktive.serializer.ATSerializer;
import im.aktive.vendor.ICDispatch.ICBlock;
import im.aktive.vendor.ICDispatch.ICDispatch;

/**
 * Created by hoangtran on 14/7/14.
 */
public class ATBaseManager<T> implements ATGlobalEventCallbackInterface {
        /**
         * OBSERVER METHODS
         */
        protected ArrayList<ATModelUpdateCallback> listCallback = new ArrayList<ATModelUpdateCallback>();

        protected Map<Integer, WeakReference<ATAPICallWrapper>> requestMap =
                new HashMap<Integer, WeakReference<ATAPICallWrapper>>();
        protected volatile int currentRequestId;

        public synchronized int addRequestCallback(ATWrappedModelRequestCallback cb)
        {
            WeakReference<ATAPICallWrapper> apiCallWrapperRef = cb.getCallWrapperRef();
            ATAPICallWrapper callWrapper = apiCallWrapperRef.get();
            int requestId = currentRequestId++;
            requestMap.put(Integer.valueOf(requestId), apiCallWrapperRef);
            callWrapper.addCallbackForRequestId(requestId, cb);
            return requestId;
        }

        public void deliverResult(int requestId, boolean isSuccess,
                                               Object result, String message)
        {
            WeakReference<ATAPICallWrapper> apiCallWrapperRef = null;
            synchronized (this)
            {
                apiCallWrapperRef = requestMap.get(Integer.valueOf(requestId));
                requestMap.remove(Integer.valueOf(requestId));
            }
            if (apiCallWrapperRef == null || apiCallWrapperRef.get() == null)
            {
                return;
            }
            ATAPICallWrapper apiCallWrapper = apiCallWrapperRef.get();
            if (isSuccess) {
                apiCallWrapper.onRequestSuccessful(requestId, result);
            } else {
                apiCallWrapper.onRequestFailed(requestId, message);
            }
        }

        public synchronized void addObserver(ATModelUpdateCallback observer)
        {
            if (!listCallback.contains(observer))
            {
                listCallback.add(observer);
            }
        }

        public synchronized void removeObserver(ATModelUpdateCallback observer)
        {
            if (listCallback.contains(observer))
            {
                listCallback.remove(observer);
            }
        }

        public void notifyObserver()
        {
            ATApplication.executeOn(ICDispatch.MAIN, new ICBlock() {

                @Override
                public void run() {
                    List<ATModelUpdateCallback> listCallbackToCall = new ArrayList<ATModelUpdateCallback>();
                    synchronized (this)
                    {
                        listCallbackToCall.addAll(listCallback);
                    }
                    for (int i = 0; i < listCallbackToCall.size(); i++)
                    {
                        ATModelUpdateCallback callback = listCallbackToCall.get(i);
                        callback.onModelUpdated();
                    }
                }
            });
        }

        public void callSuccessCallbackOnUIThread(final ATModelRequestCallback cb, final Object object)
        {
            ATApplication.executeOn(ICDispatch.MAIN, new ICBlock() {

                @Override
                public void run() {
                    cb.onSuccess(object);
                }
            });
        }

        public void callFailedCallbackOnUIThread(final ATModelRequestCallback cb,
                                                 final String message)
        {
            ATApplication.executeOn(ICDispatch.MAIN, new ICBlock() {

                @Override
                public void run() {
                    cb.onFailed(message);
                }
            });
        }

        public List<T> updateFromListSerializer(ATSerializer<T> serializerList[])
        {
            if (serializerList == null)
            {
                return null;
            }
            //DBManager();
            ArrayList<T> result = new ArrayList<T>();
            for (int i = 0; i < serializerList.length; i++)
            {
                ATSerializer<T> serializer = serializerList[i];
                T object = serializer.toObject();
			/*if (object != null) {
				serializer.updateObject(object);
			}*/
                result.add(object);
            }

            return result;
        }

        public T updateFromSerializer(ATSerializer<T> serializer)
        {
            if (serializer == null)
            {
                return null;
            }
            T object = serializer.toObject();
//		if (object != null) {
//			serializer.updateObject(object);
//		}
            return object;
        }


        @Override
        public void onPreLogin() {
            // Implement in subclass pls
        }

        @Override
        public void onPostLogin(ATUser user) {
            // Implement in subclass pls
        }

        @Override
        public void onPostLogout() {
            listCallback.clear();
        }

        @Override
        public void onPreLogout(ATUser user) {
            // Implement in subclass pls
        }

        public void init() {
            ATGlobalEventDispatchManager.getInstance().registerCallbackForDispatch(this);
        }

        public ATBaseManager()
        {
            init();
        }
    }

