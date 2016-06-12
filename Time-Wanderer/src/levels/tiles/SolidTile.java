package levels.tiles;

import physics.BoundingRectangle;

/** Class that represents a tile that will have solid properties.
 * i.e. things wont be able to go inside it, collisions are restrictive.
 * @author sergio
 */
public class SolidTile extends Tile {

    public SolidTile(int x, int y) {
        super(x,y);
        this.boundingShape = new BoundingRectangle(x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE,TILE_SIZE);
    }

}
