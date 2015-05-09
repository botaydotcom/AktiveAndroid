package im.aktive.aktive.model;

/**
 * Created by hoangtran on 22/7/14.
 */
public enum ATActivityAction {
    CHOSEN, DEFER, NOT_CHOSEN;

    public static ATActivityAction fromServerString(String activityAction)
    {
        if (activityAction.toUpperCase().equals("CHOSEN"))
        {
            return ATActivityAction.CHOSEN;
        }
        if (activityAction.toUpperCase().equals("DEFER"))
        {
            return ATActivityAction.DEFER;
        }
        if (activityAction.toUpperCase().equals("NOT_CHOSEN"))
        {
            return ATActivityAction.NOT_CHOSEN;
        }
        return ATActivityAction.CHOSEN;
    }

    public String toServerString()
    {
        switch (this)
        {
            case CHOSEN:
                return "chosen";
            case DEFER:
                return "defer";
            case NOT_CHOSEN:
                return "not_chosen";
        }
        return "";
    }
}
