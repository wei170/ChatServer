/**
 * Created by walterwei on 11/3/15.
 */
public class SessionCookie {
    private long ID;
    public int cookieIdentifier;
    public static int timeoutLength;
    // System.currentTimeMills();
    // TODO

    public SessionCookie(long id) {
        this.ID = id;
        // TODO
    }



    public boolean hasTimeOut() {
        // TODO
        return true;
    }

    public void updateTimeOfActivity() {
        // TODO
    }

    public long getID() {
        // TODO
        return ID;
    }

}
