package misc;

import levels.Room;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * This class will draw the minimap on the bottom left corner of the game
 */
public class MiniMap {
    
    /**
     * Array of images that will represent each room.
     * The rows are codified in binary, representing each door of the room.
     * (For example, 0010 is room with left door, which will be position 0010,
     * which is position 2 in the array.
     * The column specifies if the room is the one where the player is currently
     * at, or not. (0 not, 1 yes).
     */
    private final Image[][] minimapRooms;
    /**
     * Array of rooms that displays the current map where the player is currently at.
     */
    private Room[][] map;
    
    private int currentRow = 0;
    private int currentColumn = -1; // Coordinates of the current room.
/* -------------------------------------- */
/* ---- END OF ATTRIBUTES DECLARATION --- */
/* -------------------------------------- */
    
    /**
     * Constructor.
     * 
     * @param currentMap 
     *              Matrix containing the rooms of this level.
     * 
     * @throws org.newdawn.slick.SlickException
     */
    public MiniMap (Room[][] currentMap) throws SlickException {
        
        this.map = currentMap;
        
        this.minimapRooms = new Image[][]
                            {{new Image("resources/ui/minimap/empty.png"),
                              new Image("resources/ui/minimap/empty.png")},
                              {new Image("resources/ui/minimap/0001-0.png"),
                              new Image("resources/ui/minimap/0001-1.png")},
                              {new Image("resources/ui/minimap/0010-0.png"),
                              new Image("resources/ui/minimap/0010-1.png")},
                              {new Image("resources/ui/minimap/0011-0.png"),
                              new Image("resources/ui/minimap/0011-1.png")},
                              {new Image("resources/ui/minimap/0100-0.png"),
                              new Image("resources/ui/minimap/0100-1.png")},
                              {new Image("resources/ui/minimap/0101-0.png"),
                              new Image("resources/ui/minimap/0101-1.png")},
                              {new Image("resources/ui/minimap/0110-0.png"),
                              new Image("resources/ui/minimap/0110-1.png")},
                              {new Image("resources/ui/minimap/0111-0.png"),
                              new Image("resources/ui/minimap/0111-1.png")},
                              {new Image("resources/ui/minimap/1000-0.png"),
                              new Image("resources/ui/minimap/1000-1.png")},
                              {new Image("resources/ui/minimap/1001-0.png"),
                              new Image("resources/ui/minimap/1001-1.png")},
                              {new Image("resources/ui/minimap/1010-0.png"),
                              new Image("resources/ui/minimap/1010-1.png")},
                              {new Image("resources/ui/minimap/1011-0.png"),
                              new Image("resources/ui/minimap/1011-1.png")},
                              {new Image("resources/ui/minimap/1100-0.png"),
                              new Image("resources/ui/minimap/1100-1.png")},
                              {new Image("resources/ui/minimap/1101-0.png"),
                              new Image("resources/ui/minimap/1101-1.png")},
                              {new Image("resources/ui/minimap/1110-0.png"),
                              new Image("resources/ui/minimap/1110-1.png")},
                              {new Image("resources/ui/minimap/1111-0.png"),
                              new Image("resources/ui/minimap/1111-1.png")}                              
                            };
        
        for (Image[] minimapRoom : minimapRooms) {
            for (int c = 0; c < minimapRooms[0].length; c++) {
                minimapRoom[c].setFilter(Image.FILTER_NEAREST);
            }
        }
        
        
    }

    public void update() {
    }

    public void render(Graphics g) {
        
        int x = 470;
        int y = 220;
        int r = 0;
        int c = 0;
        
        for(r = 0, y = 220; r < map.length; r++, y += 7) {
            for(c = 0, x = 470; c < map[0].length; c++, x += 8) {
                if(map[r][c] == null) {
                    g.drawImage(minimapRooms[0][0], x, y); // Empty image.
                    continue;
                }
                if(map[r][c].isVisited()) {
                    if(r==currentRow && c==currentColumn) {
                        g.drawImage(minimapRooms[map[r][c].getIntRepresentation()][1], x, y);
                    } else {
                        g.drawImage(minimapRooms[map[r][c].getIntRepresentation()][0], x, y);
                    }
                } else {
                    g.drawImage(minimapRooms[0][0], x, y); // Empty image.
                }
            }
        }
    }

    public void setCurrentMap(Room[][] map) {
        this.map = map;
    }

    public void setCurrentRoom(int r, int c) {
        currentRow = r;
        currentColumn = c;
    }

}
