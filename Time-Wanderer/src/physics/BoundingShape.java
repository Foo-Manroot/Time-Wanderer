package physics;

import java.util.ArrayList;
import levels.tiles.Tile;

/**
 * Basic unit of a hitbox. Shape that will be used in collision detection.
 * @author sergio
 */
abstract public class BoundingShape {
    
    public boolean checkCollision(BoundingShape shape){
        if(shape instanceof BoundingRectangle)
            return checkCollision((BoundingRectangle) shape);
        return false;
    }
 
    public abstract boolean checkCollision(BoundingRectangle rect);
 
    public abstract void updatePosition(float newX, float newY);
 
    public abstract void movePosition(float x, float y);
 
    public abstract ArrayList<Tile> getTilesOccupying(Tile[][] tiles);
 
    public abstract ArrayList<Tile> getGroundTiles(Tile[][] tiles);
    
    public abstract float getX();
    public abstract float getY();
    public abstract float getWidth();
    public abstract float getHeight();
}
