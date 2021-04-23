package mysig;

import java.math.BigInteger;

public class Util {
    public static String toHex(byte[] bytes){
        return String.format("%032x", new BigInteger(1, bytes));
    }
}
