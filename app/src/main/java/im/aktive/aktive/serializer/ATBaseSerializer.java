package im.aktive.aktive.serializer;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;

/**
 * Created by hoangtran on 17/7/14.
 */
public abstract class ATBaseSerializer<V, T> implements ATSerializer<T> {
    // public Object toObject(); // need implement in subclass
    private Class<V> type;
    public V deserialize(JSONObject jsonObject)
    {
        this.type = (Class<V>)
                ((ParameterizedType)getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];
        Gson gson = new Gson();
        V result = gson.fromJson(jsonObject.toString(), type);
        return result;
    }

    public JSONObject serialize(T object)
    {
        return null;
    }
}
