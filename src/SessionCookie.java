
/**
 * Created by walterwei on 11/3/15.
 */
public class SessionCookie {
    private long ID;
    public static int timeoutLength;
    private long startTime ;

    public SessionCookie(long id) {
        this.ID = id;
        this.startTime = System.currentTimeMillis();
        // TODO : check it
    }



    public boolean hasTimeOut() {
        // TODO : check it
        if ((System.currentTimeMillis() - startTime) > timeoutLength * 1000) {
            return true;
        }
        return false;
    }

    public void updateTimeOfActivity() {
        startTime = System.currentTimeMillis();
        // TODO : check it
    }

    public long getID() { return ID; }

}
