
/**
 * Created by walterwei on 11/3/15.
 */
public class SessionCookie {
    private long ID;
    public static int timeoutLength = 300;
    private long startTime ;

    public SessionCookie(long id) {
        this.ID = id;
        this.startTime = System.currentTimeMillis();
        // TODO : check it
    }



    public boolean hasTimedOut() {
        // TODO : check it

        if (((int) System.currentTimeMillis() - (int) startTime) > timeoutLength * 1000) {
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
