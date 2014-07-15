package im.aktive.aktive.serializer;

import org.json.JSONObject;

public interface ATSerializer<T> {
	T toObject();
	//void updateObject(T object);
	//int objectId();
}
