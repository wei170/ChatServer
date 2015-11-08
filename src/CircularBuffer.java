import java.util.Arrays;

/**
 * Created by walterwei on 11/3/15.
 */
public class CircularBuffer {
    public final int FIXED_SIZE_Buffer;
    private String[] buffer;
    public static int FOURDNUM = -1;
    private int count = 0;
    // Count is for counting the the current message at which position in buffer (Index)

    public CircularBuffer(int size) {
        //TODO : check it
        FIXED_SIZE_Buffer = size;
        this.buffer = new String[FIXED_SIZE_Buffer];
    }

    public void put(String message) {
        //TODO : check it
        if (FOURDNUM == 9999) {
            FOURDNUM = 0;
        } else { FOURDNUM++; }

        buffer[count++] = String.format("%04d) %s", FOURDNUM, message);
        if (count == FIXED_SIZE_Buffer)  count = 0;
    }

    public String[] getNewest(int numMessages) {
        //TODO : check it (Seriously!!!)
        if (numMessages < 0) { return null; }
        if (numMessages == 0) { return new String[0]; }

        int numAvailable = 0;
        for (String s : buffer) {
            if (s != null) {
                numAvailable++;
            }
        }
        final int numResult = Math.min(numAvailable, numMessages);

        return Arrays.copyOf(buffer, numResult);
    }
}
