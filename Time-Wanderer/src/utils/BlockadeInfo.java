package utils;

import levels.Room;
/**
 * Auxiliary class used to display the info of a blockade (the rooms between
 * the blockade, and the direction relative to the first room). Used to let
 * buttons know which wall to destroy.
 * @author sergio
 */
public class BlockadeInfo {
    private Room room1;
    private Room room2;
    private int direction;
    
    public BlockadeInfo(Room room1, Room room2, int direction) {
        this.room1 = room1;
        this.room2 = room2;
        this.direction = direction;
    }

    public Room getRoom1() {
        return room1;
    }

    public void setRoom1(Room room1) {
        this.room1 = room1;
    }

    public Room getRoom2() {
        return room2;
    }

    public void setRoom2(Room room2) {
        this.room2 = room2;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    
}
