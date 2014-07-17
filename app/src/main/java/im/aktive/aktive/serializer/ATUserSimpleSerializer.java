package im.aktive.aktive.serializer;

import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATUser;

/**
 * Created by hoangtran on 17/7/14.
 */
public class ATUserSimpleSerializer extends ATBaseSerializer<ATUserSimpleSerializer, ATUser>{
    public int id;
    public String full_name;
    public String gender;
    public String birth_day;
    public String bio;
    public String location;
    public String email;
    public String avatar_image;
    public String avatar_image_medium;
    public String avatar_image_small;

    @Override
    public ATUser toObject() {
        ATUser user = ATUserManager.getInstance().getOrCreateUser(id);
        user.setFullName(full_name);
        user.setBio(bio);
        user.setLocation(location);
        user.setEmail(email);
        user.setAvatarImage(avatar_image);
        user.setAvatarImage(avatar_image_medium);
        user.setAvatarImage(avatar_image_small);
        return user;
    }
}
