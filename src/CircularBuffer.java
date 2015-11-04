/**
 * Created by walterwei on 11/3/15.
 */
public class CircularBuffer {
    public final int FIXED_SIZE_Buffer;
    private String[] buffer;
    public static int FOURDNUM = 0;
    private int count = -1;
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

        if (count < FIXED_SIZE_Buffer)  count ++;
        else count = 0;

        buffer[count] = String .valueOf(FOURDNUM) + message;
    }

    public String[] getNewest(int numMessages) {
        //TODO : check it (Seriously!!!)
        if (numMessages < 0) { return null; }
        if (numMessages == 0) { return new String[0]; }

        int numAvailable = 0;
        for (String s : buffer) {
            if (s != null) {
                numAvailable ++;
            }
        }

        String[] result = new String[numAvailable];
        int index = count;
        int i = 0;
        while (numMessages - 1 > i) {
            result[i++] = this.buffer[index];
            if (index == 0 && this.buffer[FIXED_SIZE_Buffer] != null ) {
                index = FIXED_SIZE_Buffer - 1;
            } else index--;
        }

        return result;
    }
}
