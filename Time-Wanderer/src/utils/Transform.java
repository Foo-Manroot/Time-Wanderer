package utils;

import org.newdawn.slick.geom.Vector2f;

/**
 * This class implements a method that, through a matrix, transforms a Vector2f,
 * rotating and translating it into another point (Vector2f).
 * 
 * @author foo
 */
public class Transform {
    
    /**
     * Rotates the desired point the given number of degrees, and translates it
     * with the given factor. This method is useful with unit vectors.
     * 
     * 
     * @param oldVector
     *              The old to be rotated.
     * @param degrees
     *              The angle, in degrees, to rotate the point.
     * @param displacement
     *              The displacement of the new vector. If no additional 
     *              displacement is required, this argument can be null.
     * 
     * @return 
     *          A new vector, representing the converted point.
     */
    public static Vector2f rotate (Vector2f oldVector, float degrees, 
                                   Vector2f displacement) {
        
        Vector2f transformed = new Vector2f();
        double A = Math.toRadians(degrees);
        Vector2f disp = (displacement == null)? new Vector2f(0, 0): displacement;
        
        /* The new coordinates are (x2, y2), the old ones are (x1, y1), the 
        rotation angle is A, and the displacement is given by (disp.X, disp.Y).
        The following is the conversion matrix to be used:
                |x2|   |cos A  -sin A    disp.X   |   |x1|
                |y2| = |sin A   cos A    disp.Y   | · |y1|
                |1 |   |  0       0        1      |   |1 |
        */
        double [][] matrix = {{Math.cos(A), -Math.sin(A), disp.x}, 
                              {Math.sin(A),  Math.cos(A), disp.y},
                              {0, 0, 1}};
        
        /* Makes the matricial product between the previous matrix and the old 
        vector */
        transformed.x = (float) (matrix[0][0] * oldVector.x + 
                                 matrix[0][1] * oldVector.y + 
                                 matrix[0][2] * 1);
        
        transformed.y = (float) (matrix[1][0] * oldVector.x + 
                                 matrix[1][1] * oldVector.y + 
                                 matrix[1][2] * 1);
        
        return transformed;
    }
}
