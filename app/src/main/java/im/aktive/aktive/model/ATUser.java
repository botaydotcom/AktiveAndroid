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

    private int id;
    private String fullName;
    private String email;
    private String bio;
    private String location;
    private Date birthday;
    private ATGender gender;
    private String beingFullName;
    private String beingBio;
    private String beingLocation;
    private Date beingBirthday;
    private ATGender beingGender;

    private String avatarImage;
    private String avatarImageSmall;
    private String avatarImageMedium;
    private String coverImage;
    private String coverImageSmall;
    private String coverImageMedium;
    private int numFriends;

    private boolean completedOnboarding;

    public ATUser(int id) {
        this.id = id;
    }

    private boolean friendRequestSent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio()
    {
        return bio;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setGender(ATGender gender)
    {
        this.gender = gender;
    }

    public ATGender getGender()
    {
        return gender;
    }

    public String getBeingFullName() {
        return beingFullName;
    }

    public void setBeingFullName(String beingFullName) {
        this.beingFullName = beingFullName;
    }

    public String getBeingBio() {
        return beingBio;
    }

    public void setBeingBio(String beingBio) {
        this.beingBio = beingBio;
    }

    public String getBeingLocation() {
        return beingLocation;
    }

    public void setBeingLocation(String beingLocation) {
        this.beingLocation = beingLocation;
    }

    public Date getBeingBirthday() {
        return beingBirthday;
    }

    public void setBeingBirthday(Date beingBirthday) {
        this.beingBirthday = beingBirthday;
    }

    public ATGender getBeingGender() {
        return beingGender;
    }

    public void setBeingGender(ATGender beingGender) {
        this.beingGender = beingGender;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getAvatarImageSmall() {
        return avatarImageSmall;
    }

    public void setAvatarImageSmall(String avatarImageSmall) {
        this.avatarImageSmall = avatarImageSmall;
    }

    public String getAvatarImageMedium() {
        return avatarImageMedium;
    }

    public void setAvatarImageMedium(String avatarImageMedium) {
        this.avatarImageMedium = avatarImageMedium;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCoverImageSmall() {
        return coverImageSmall;
    }

    public void setCoverImageSmall(String coverImageSmall) {
        this.coverImageSmall = coverImageSmall;
    }

    public String getCoverImageMedium() {
        return coverImageMedium;
    }

    public void setCoverImageMedium(String coverImageMedium) {
        this.coverImageMedium = coverImageMedium;
    }

    public int getNumFriends() {
        return numFriends;
    }

    public void setNumFriends(int numFriends) {
        this.numFriends = numFriends;
    }

    public boolean isFriendRequestSent() {
        return friendRequestSent;
    }

    public void setFriendRequestSent(boolean friendRequestSent) {
        this.friendRequestSent = friendRequestSent;
    }

    public boolean isCompletedOnboarding() {
        return completedOnboarding;
    }

    public void setCompletedOnboarding(boolean completedOnboarding) {
        this.completedOnboarding = completedOnboarding;
    }

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