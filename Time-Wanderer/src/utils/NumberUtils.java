package utils;

import main.MainClass;

public class NumberUtils {
    /** round n down to nearest multiple of m */
    public static int roundDown (int n, int m) {
        return n >= 0 ? (n / m) * m : ((n - m + 1) / m ) * m;
    }

    public static int invertOrdinate(int y) {
        return MainClass.WINDOW_HEIGHT - y;
    }

    public static double invertOrdinate(double y) {
        return MainClass.WINDOW_HEIGHT - y;
    }

}
