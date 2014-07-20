package im.aktive.aktive.serializer;

import java.util.List;

import im.aktive.aktive.manager.ATTagManager;
import im.aktive.aktive.manager.ATTagValueManager;
import im.aktive.aktive.model.ATTag;
import im.aktive.aktive.model.ATTagType;
import im.aktive.aktive.model.ATTagValue;

/**
 * Created by hoangtran on 18/7/14.
 */
public class ATTagSerializer extends ATBaseSerializer<ATTagSerializer, ATTag>{
    public int id;
    public String name;
    public String question;
    public String tag_type;
    public boolean can_be_used_for_first_time;
    public ATTagValueSerializer[] tag_values;
    @Override
    public ATTag toObject() {
        ATTag tag = ATTagManager.getInstance().getOrCreateTag(id);
        tag.setName(name);
        tag.setQuestion(question);
        tag.setTagType(ATTagType.fromServerString(tag_type));
        List<ATTagValue> listTagValue = ATTagValueManager.getInstance().updateFromListSerializer(tag_values);
        tag.setTagValues(listTagValue);
        return tag;
    }
}
