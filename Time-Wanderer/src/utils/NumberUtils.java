package utils;

import main.MainClass;
import org.newdawn.slick.geom.Vector2f;

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

    /**
     *  Maybe this will require further research, but the level seems to be
     * scaled, so the main window is 1152x648, but the position of the objects
     * is made within a 535x301 window. Because of this, the position of the 
     * mouse must be corrected by dividing it by a factor of 2.15.
     * 
     *  This method returns the new coordinates already adjusted, even with the
     * inversion of the Y axis.
     * 
     * @param original 
     *              Vector with the original coordinates.
     * 
     * @return 
     *              A new vector with the adjusted coordinates.
     */
    public static Vector2f adjustCoordinates (Vector2f original) {
        
        /* Maybe this will require further research, but the level seems to be
        scaled, so the main window is 1152x648, but the position of the objects
        is made within a 535x301 window. Because of this, the position of the 
        mouse must be corrected by dividing it by a factor of 2.15 */
        return new Vector2f ((float) (original.x / 2.15),
                             (float) (invertOrdinate(original.y) / 2.15));
    }
}
