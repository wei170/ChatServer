import java.util.Random;

/**
 * Created by walterwei on 11/3/15.
 */
public class SessionCookie {
    private long ID;
    Random random = new Random();
    public int cookieIdentifier;
    public static int timeoutLength;
    public final long startTime = System.currentTimeMillis();
    private long currentTime;

    public SessionCookie(long id) {
        this.ID = id;
        // TODO : check it
        this.cookieIdentifier = random.nextInt(10000);
        // TODO : Error => ID must be distinct
    }



    public boolean hasTimeOut() {
        // TODO : check it
        if ((currentTime - startTime) > timeoutLength * 1000) {
            return false;
        }
        return true;
    }

    public void updateTimeOfActivity() {
        currentTime = System.currentTimeMillis();
        // TODO : check it
    }

    public long getID() { return ID; }

}
