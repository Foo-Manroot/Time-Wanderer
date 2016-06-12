package levels.tiles;

import physics.BoundingRectangle;

/**
 * Class that represents a platform tile.
 * @author sergio
 */
public class PlatformTile extends Tile {

    public PlatformTile(int x, int y) {
        super(x, y);
        this.boundingShape = new BoundingRectangle(x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);
    }

}
