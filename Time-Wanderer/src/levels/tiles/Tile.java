package levels.tiles;

import physics.BoundingShape;

/** 
 * Abstract class that represents each of the divisions of the grid of a map.
 * @author sergio
 */
abstract public class Tile {
    public static final int TILE_SIZE = 32;
    /** Coordinate X in the tilemap grid */
    private int x;
    /** Coordinate Y in the tilemap grid */
    private int y;
    /** Bounding shape to calculate collisions */
    protected BoundingShape boundingShape = null;
    
    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.boundingShape = null;
        
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BoundingShape getBoundingShape() {
        return boundingShape;
    }

    public void setBoundingShape(BoundingShape boundingShape) {
        this.boundingShape = boundingShape;
    }
    
    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
    
}
