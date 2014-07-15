package im.aktive.aktive.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hoangtran on 14/7/14.
 */
public class ATUser implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 2979944880330382806L;
    public int id;
    public String fullName;
    public String email;
    public String bio;
    public String location;
    public Date birthday;
    public ATGender gender;

    public String beingFullName;
    public String beingBio;
    public String beingLocation;
    public Date beingBirthday;
    public ATGender beingGender;

    public String avatarImage;
    public String avatarImageSmall;
    public String avatarImageMedium;
    public String coverImage;
    public String coverImageSmall;
    public String coverImageMedium;
    public int numFriends;
    public boolean friendRequestSent;

    public void prepareEditing()
    {
        beingFullName = fullName;
        beingBio = bio;
        beingGender = gender;
        beingBirthday = birthday;
        beingLocation = location;
    }

    public void commitEditing()
    {
        fullName = beingFullName;
        bio = beingBio;
        gender = beingGender;
        birthday = beingBirthday;
        location = beingLocation;
    }

    public boolean isEdited()
    {
        return (!fullName.equals(beingFullName))
                || (bio == null && beingBio != null && beingBio.length() > 0)
                || (bio != null && !bio.equals(beingBio))
                || (location == null && beingLocation != null && beingLocation.length() > 0)
                || (location != null && !location.equals(beingLocation))
                || (birthday == null && beingBirthday != null)
                || (birthday != null && !birthday.equals(beingBirthday))
                || (gender != beingGender);
    }
}