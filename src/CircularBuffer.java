import java.util.Arrays;

/**
 * Created by walterwei on 11/3/15.
 */
public class CircularBuffer {
    public final int fixed;
    private String[] buffer;
    public int fourNum = -1;
    private int count = 0;
    // Count is for counting the the current message at which position in buffer (Index)

    public CircularBuffer(int size) {
        //TODO : check it
        fixed = size;
        this.buffer = new String[fixed];
    }

    public void put(String message) {
        //TODO : check it
        if (fourNum == 9999) {
            fourNum = 0;
        } else { fourNum++; }

        buffer[count++] = String.format("%04d) %s", fourNum, message);
        if (count == fixed)  count = 0;
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
        if (count - numResult >= 0) return Arrays.copyOfRange(buffer, count - numResult, count);
        else {
            String[] s = new String[numResult];
            for (int i = 0; i < numResult - count; i++) {
                s[i] = buffer[buffer.length + count - numResult + i];
            }
            for (int i = numResult - count; i < numResult; i++) {
                s[i] = buffer[i - numResult + count];
            }
            return s;
        }
    }
}
