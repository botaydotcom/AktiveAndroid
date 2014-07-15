package im.aktive.aktive.model;

/**
 * Created by hoangtran on 14/7/14.
 */
public enum ATGender {
    MALE, FEMALE, UNKNOWN, OTHER;

    public static ATGender fromServerString(String gender)
    {
        if (gender.toUpperCase().equals("MALE"))
        {
            return ATGender.MALE;
        }
        if (gender.toUpperCase().equals("FEMALE"))
        {
            return ATGender.FEMALE;
        }
        if (gender.toUpperCase().equals("OTHER"))
        {
            return ATGender.OTHER;
        }
        if (gender.toUpperCase().equals("UNKNOWN"))
        {
            return ATGender.UNKNOWN;
        }
        return ATGender.OTHER;
    }

    public String toServerString()
    {
        switch (this)
        {
            case MALE:
                return "male";
            case FEMALE:
                return "female";
            case OTHER:
                return "other";
            case UNKNOWN:
                return "unknown";
        }
        return "";
    }
}
