package im.aktive.aktive.serializer;

/*  user: {
    "cover_image_medium": "https://s3-ap-southeast-1.amazonaws.com/kroozor-staging/defaults/default_cover_img_medium.jpg",
    "cover_image": "https://s3-ap-southeast-1.amazonaws.com/kroozor-staging/defaults/default_cover_img.jpg",
    "location": null,
    "external_ids": [],
    "avatar_image_small": "https://s3-ap-southeast-1.amazonaws.com/kroozor-staging/defaults/default_avatar_img_small.png",
    "received_friend_requests": [],
    "avatar_image_medium": "https://s3-ap-southeast-1.amazonaws.com/kroozor-staging/defaults/default_avatar_img_medium.png",
    "cover_image_small": "https://s3-ap-southeast-1.amazonaws.com/kroozor-staging/defaults/default_cover_img_small.jpg",
    "avatar_image": "https://s3-ap-southeast-1.amazonaws.com/kroozor-staging/defaults/default_avatar_img.png",
    "external_calendars": [],
    "id": 1,
    "updated_at": "2014-01-01T15:51:09.000Z",
    "completed_onboarding": true,
    "bio": null,
    "email": "test@testtest.com",
    "need_confirm": true,
    "birth_day": null,
    "created_at": "2014-01-01T15:51:04.000Z",
    "gender": "unknown",
    "full_name": "Test test"
  }*/
import org.json.JSONObject;

import com.google.gson.Gson;
//import im.aktive.aktive.format_utils.ATDateTimeUtils;
//import im.aktive.aktive.manager.ATFriendRequestManager;
import im.aktive.aktive.manager.ATUserManager;
import im.aktive.aktive.model.ATGender;
import im.aktive.aktive.model.ATUser;

import im.aktive.aktive.model.ATUser;

public class ATUserProfileSerializer extends ATBaseSerializer<ATUserProfileSerializer, ATUser>{
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
    public boolean completed_onboarding;

	public ATUser toObject()
	{
		ATUser user = ATUserManager.getInstance().getOrCreateUser(id);
		user.setFullName(full_name);
		user.setGender(ATGender.fromServerString(gender));
		user.setBio(bio);
		user.setEmail(email);
		user.setLocation(location);
		user.setAvatarImage(avatar_image);
		user.setAvatarImageMedium(avatar_image_medium);
		user.setAvatarImageSmall(avatar_image_small);
        user.setCompletedOnboarding(completed_onboarding);
		if (birth_day != null)
		{
			/*user.birthday = ATDateTimeUtils.birthdayStringToDate(birth_day);*/
		}
		/*ATFriendRequestManager.getInstance().updateFromListSerializer(received_friend_requests);*/
		return user;
	}

}