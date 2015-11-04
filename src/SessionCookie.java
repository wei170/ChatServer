import java.util.Random;

/**
 * Created by walterwei on 11/3/15.
 */
public class SessionCookie {
    private long ID;
    Random random = new Random();
    public int cookieIdentifier = random.nextInt(10000);
    // TODO : Error => ID must be distinct
    public static int timeoutLength = 300;
    public static final long startTime = System.currentTimeMillis();
    public static long currentTime;
    // System.currentTimeMills();

    public SessionCookie(long id) {
        this.ID = id;
        // TODO : check it
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
