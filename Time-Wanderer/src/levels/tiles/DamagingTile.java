package levels.tiles;

import physics.BoundingRectangle;

/**
 * Class that represents a damaging tile.
 * Damaging tiles will damage any character in contact with it.
 * @author sergio
 */
public class DamagingTile extends Tile {

    // Damage dealt by this tile.
    public static final int DAMAGE = 10;
    
    public DamagingTile(int x, int y) {
        super(x, y);
        this.boundingShape = new BoundingRectangle(x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);
    }
}
