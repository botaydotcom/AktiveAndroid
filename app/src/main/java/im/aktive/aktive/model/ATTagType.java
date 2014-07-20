package im.aktive.aktive.model;

/**
 * Created by hoangtran on 18/7/14.
 */
public enum ATTagType {
    BINARY, NUMERICAL, MULTI_VALUE;

    public static ATTagType fromServerString(String gender)
    {
        if (gender.toUpperCase().equals("BINARY"))
        {
            return ATTagType.BINARY;
        }
        if (gender.toUpperCase().equals("NUMERICAL_VALUE"))
        {
            return ATTagType.NUMERICAL;
        }
        if (gender.toUpperCase().equals("MULTI_VALUE"))
        {
            return ATTagType.MULTI_VALUE;
        }
        return ATTagType.MULTI_VALUE;
    }

    public String toServerString()
    {
        switch (this)
        {
            case BINARY:
                return "binary";
            case NUMERICAL:
                return "numerical_value";
            case MULTI_VALUE:
                return "multi_value";
        }
        return "";
    }
}
