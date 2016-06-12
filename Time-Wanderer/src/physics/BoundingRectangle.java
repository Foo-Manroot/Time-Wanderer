package physics;

import java.util.ArrayList;
import levels.tiles.Tile;
import static levels.tiles.Tile.TILE_SIZE;
import utils.NumberUtils;

/**
 * Collision unit with rectangular shape that is always 
 * aligned on both axes. (Won't rotate).
 * Inherits from BoundingShape. Contains methods to check if it is colliding
 * with another BoundingRectangle, and methods to check in which level tiles
 * is this BoundingShape overlapping.
 * @author sergio
 */
public class BoundingRectangle extends BoundingShape {

    public float x;
    public float y;
    public float width;
    public float height;

    public BoundingRectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    /**
     * Sets the relative position of the bounding rectangle.
     * @param newX
     * @param newY 
     */
    public void updatePosition(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }
    /**
     * Sets the relative position of the bounding rectangle.
     * @param x
     * @param y 
     */
    public void movePosition(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    
    /**
     * Returns if this bounding rectangle is colliding with another one in a
     * more efficient way.
     * Compares the 4 sides of a rectangle with the other
     * rectangle. A collision happens when none of these conditions are true:
     * <ul>
     * <li> The left side of 'rect' is to the right of the right side of 'this'.
     * </li>
     * <li> The right side of 'rect' is to the left of the left side of 'this'.
     * </li>
     * <li> The bottom side of 'rect' is to the top of the top side of 'this'.
     * </li>
     * <li> The top side of 'rect' is to the bottom of the bottom side of
     * 'this'.</li>
     * </ul>
     *
     * @param rect
     * @return True if a collision occurs, false if not.
     */
    @Override
    public boolean checkCollision(BoundingRectangle rect) {
        
        return !(rect.x > (this.x + width) || (rect.x + rect.width) < this.x
                || rect.y > (this.y + height) || (rect.y + rect.height) < this.y);
    }

    /**
     * Method to return the tiles that this bounding rectangle is occupying.
     *
     * @param tiles List of tiles in the current room/level
     * @return
     */
    @Override
    public ArrayList<Tile> getTilesOccupying(Tile[][] tiles) {
        ArrayList<Tile> occupiedTiles = new ArrayList<Tile>();
        
        for(int i = NumberUtils.roundDown((int) x,TILE_SIZE); i <= x + width; i += TILE_SIZE) {
            if(i<0 || i/TILE_SIZE>=tiles.length)
                continue;
            for(int j = NumberUtils.roundDown((int) y, TILE_SIZE); j <= y + height; j +=TILE_SIZE) {
                if(j<0 || j/TILE_SIZE >= tiles[0].length)
                    continue;
                occupiedTiles.add(tiles[(i/TILE_SIZE)][(j/TILE_SIZE)]);
            }
        }
        
        return occupiedTiles;
    }
   
    /**
     * Method to return the tiles below the bounding rect, i.e. 'ground tiles'.
     *
     * @param tiles List of tiles in the current room/level
     * @return
     */
    @Override
    public ArrayList<Tile> getGroundTiles(Tile[][] tiles) {
        ArrayList<Tile> tilesUnderneath = new ArrayList<Tile>();
        int j = (int) (y + height + 1);
        
        if(j >= 0 && j/TILE_SIZE < tiles[0].length) {
            for(int i = NumberUtils.roundDown((int) x,TILE_SIZE); i <= x + width; i += TILE_SIZE) {
                if(i<0 || i/TILE_SIZE>=tiles.length) 
                    continue;
                tilesUnderneath.add(tiles[(i/TILE_SIZE)][(j/TILE_SIZE)]);
            }
        }
        return tilesUnderneath;
    }

}
