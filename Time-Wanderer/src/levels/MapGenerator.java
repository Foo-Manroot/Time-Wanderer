package levels;

import entities.LevelButton;
import utils.Coordinates;
import java.util.ArrayList;
import java.util.Random;
import utils.BlockadeInfo;

/**
 * Class that generates the procedurally generated map.
 * Using backtracking methods, links different kind of rooms if they
 * door booleans are compatible (i.e. left can be connected with right).
 * @author sergio
 */
public class MapGenerator {

    public static final int rows = 5;
    public static final int columns = 7;
    private final int prob = 20;        // Probability of a room to be empty. 1/prob = % to be empty
                                        // Set to a very high value to avoid sparsity.
    
    private final int initialRow = 0;   // Initial coordinates to start generation.
    private final int initialColumn = 0;
    
    /* The boss's room will be selected "randomly" */
    public static final int bossRoomRow = (int) (Math.random() * (rows - 1) + 1);
    public static final int bossRoomColumn = (int) ((columns / 2)
                                                   + Math.random()
                                                   * (columns / 2));
   
    private final Random randomSeed; // Random number generator

    /** Map without being yet converted to rooms.
     *  The third coordinate takes note of the characteristics for backtracking:
        [0], [1], [2], [3] set to 'true' means the room has left, up, right, down door.
        [4] means if the backtracking algorithm has checked that room. */
    private boolean[][][] preMap;
    
    public MapGenerator() {
        this.randomSeed = new Random();
    }
    
    public void generateMap() {
        
        preMap = new boolean[rows][columns][5];
        // The third coordinate takes note of the characteristics for backtracking:
        //  [0], [1], [2], [3] set to 'true' means the room has left, up, right, down door.
        //  [4] means if the backtracking algorithm has checked that room.
        
        generateSparsity(preMap); // Sets [4] to true depending on the probability.
        
        ArrayList<Coordinates> history = new ArrayList<>(); // Stores coordinates to be able to go back to them.
        ArrayList<String> check = new ArrayList<>(); // Auxiliary, used to tell where can be a node connected.

        int r = initialRow, c = initialColumn;
        
        preMap[bossRoomRow][bossRoomColumn][4] = true;
        preMap[bossRoomRow][bossRoomColumn][0] = true;
        preMap[bossRoomRow][bossRoomColumn-1][2] = true;
        preMap[bossRoomRow][bossRoomColumn-1][1] = true;
        preMap[bossRoomRow-1][bossRoomColumn-1][3] = true;

        do {
            preMap[r][c][4] = true;
            check.clear();
            
            // Check validity of [r][c-1] (go left) and check if we have checked that node.
            if (c > 0 && preMap[r][c - 1][4] == false) {
                check.add("L");
            }
            // Check validity of [r-1][c] (go up) and check if we have checked that node.
            if (r > 0 && preMap[r - 1][c][4] == false) {
                check.add("U");
            }
            // Check validity of [r][c+1] (go right) and check if we have checked that node.
            if (c < columns - 1 && preMap[r][c + 1][4] == false) {
                check.add("R");
            }
            // Check validity of [r+1][c] (go down) and check if we have checked that node.
            if (r < rows - 1 && preMap[r + 1][c][4] == false) {
                check.add("D");
            }

            // If we can move. (i.e. we added some direction to 'check'
            if (!check.isEmpty()) {
                history.add(new Coordinates(r, c));
                String moveDirection = check.get(randomSeed.nextInt(check.size()));
                switch (moveDirection) {
                    case "L": // Left movement chosen
                        preMap[r][c][0] = true; // Set this room to have a left door.
                        c = c - 1;              // Move left
                        preMap[r][c][2] = true; // Set the new room to have a right door.
                        break;
                    case "U": // Up movement chosen
                        preMap[r][c][1] = true; // Set this room to have an up door.
                        r = r - 1;              // Move up
                        preMap[r][c][3] = true; // Set this room to have a down door.
                        break;
                    case "R": // Right movement chosen
                        preMap[r][c][2] = true; // Set this room to have a right door.
                        c = c + 1;              // Move right
                        preMap[r][c][0] = true; // Set this room to have a left door.
                        break;
                    case "D": // Down movement chosen
                        preMap[r][c][3] = true; // Set this room to have a down door.
                        r = r + 1;              // Move down
                        preMap[r][c][1] = true; // Set this room to have an up door.
                        break;  
                }
            } else { // If can't move, go back to previous room
                Coordinates aux = history.get(history.size() - 1);
                c = aux.c;
                r = aux.r;
                history.remove(history.size() - 1);
            }
        } while (!history.isEmpty());
        
    }

    /**
     * Using the 3d matrix of booleans, turns the values into a matrix of
     * rooms (Room[][]) that represent the map.
     * Using the boolean values stored in each row-column combination,
     * matches them with a room that correspond with the same 'door booleans',
     * creating the map of actual Levels
     * @param tileset Tileset to be used in the creation of rooms.
     * @return 2D array of Rooms that represent a map.
     */
    
    public Room[][] convertMap(String tileset) {
        
        ArrayList<Room> levels = new ArrayList<>();
        
        levels.add(new Room("U","1000-1",tileset, true, false, false, false));
        levels.add(new Room("D","0100-1",tileset, false, true, false, false));
        levels.add(new Room("L","0010-1",tileset, false, false, true, false));
        levels.add(new Room("R","0001-1",tileset, false, false, false, true));
        levels.add(new Room("║","1100-1",tileset, true, true, false, false));
        levels.add(new Room("╗","0110-1",tileset, false, true, true, false));
        levels.add(new Room("╚","1001-1",tileset, true, false, false, true));
        levels.add(new Room("╦","0111-1",tileset, false, true, true, true));
        levels.add(new Room("╣","1110-1",tileset, true, true, true, false));
        levels.add(new Room("╬","1111-1",tileset, true, true, true, true));
        levels.add(new Room("═","0011-1",tileset, false, false, true, true));
        levels.add(new Room("╝","1010-1",tileset, true, false, true, false));
        levels.add(new Room("╔","0101-1",tileset, false, true, false, true));
        levels.add(new Room("╩","1011-1",tileset, true, false, true, true));
        levels.add(new Room("╠","1101-1",tileset, true, true, false, true));
        
        Room[][] map = new Room[rows][columns];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(i == bossRoomRow && j == bossRoomColumn) {
                    map[i][j] = new Room("J","boss",tileset,false,false,true,false);
                    map[i][j].fillObjectLayer();
                    map[i][j].setRightBlockade(true);
                } else {
                    map[i][j] = getValidLevel(preMap[i][j],levels);
                }
            }
        }
//        printMap(map);
        return map;
    }

    /**
     * Calls the method validLevel from the Level with all of the possible levels
     * to obtain a level that matches the doors
     * @param info
     * @return 
     */
    private Room getValidLevel(boolean[] info, ArrayList<Room> levels) {

        Room returnLevel = null;

        for (Room level : levels) {
            if (level.validLevel(info)) {
                returnLevel = level.makeRoomCopy();
                returnLevel.fillObjectLayer();
                break;
            }
        }

        return returnLevel;
    }
    
    public ArrayList<BlockadeInfo> generateBlockades(Room[][] finishedMap) {
        ArrayList<BlockadeInfo> returnArray = new ArrayList<>();
        
        int probability = 35; // 35 percent.
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(finishedMap[i][j] != null) {
                    if(100*Math.random() <= probability && 
                            !(j == bossRoomColumn && i == bossRoomRow)) {
                        ArrayList<Integer> check = new ArrayList<>();
                        if(finishedMap[i][j].hasUpDoor()) {
                            check.add(0);
                        }
                        if(finishedMap[i][j].hasDownDoor()) {
                            check.add(1);
                        }
                        if(finishedMap[i][j].hasLeftDoor()) {
                            check.add(2);
                        }
                        if(finishedMap[i][j].hasRightDoor()) {
                            check.add(3);
                        }

                        int directionChosen = check.get(randomSeed.nextInt(check.size()));
                        
                        Room room1 = finishedMap[i][j];
                        Room room2;
                        switch(directionChosen) {
                            case 0: // UP
                                room1.setUpBlockade(true);
                                room2 = finishedMap[i-1][j];
                                room2.setDownBlockade(true);
                                break;
                            case 1: // DOWN
                                room1.setDownBlockade(true);
                                room2 = finishedMap[i+1][j];
                                room2.setUpBlockade(true);
                                break;
                            case 2: // LEFT
                                room1.setLeftBlockade(true);
                                room2 = finishedMap[i][j-1];
                                room2.setRightBlockade(true);
                                break;
                            case 3: // RIGHT
                                room1.setRightBlockade(true);
                                room2 = finishedMap[i][j+1];
                                room2.setLeftBlockade(true);
                                break;
                            default: // Other (shouldn't happen).
                                room2 = room1; 
                                break;
                        }
                        returnArray.add(new BlockadeInfo(room1,room2,directionChosen));
                    }

                }
            }
        }
        
        return returnArray;
        
    }
    
    public void generateButtons(Room[][] finishedMap, ArrayList<BlockadeInfo> blockadesArray) {
        int numberOfButtonsNeeded = blockadesArray.size();
        int numberOfButtonsCreated = 0;
        
        int probability = 35;
        
        ArrayList<Coordinates> roomsWithButtons = new ArrayList<>();
        while(numberOfButtonsNeeded > numberOfButtonsCreated) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if(blockadesArray.isEmpty()) {
                        return;
                    }
                    if(finishedMap[i][j] != null 
                            && !roomsWithButtons.contains(new Coordinates(i,j))) {
                        
                        if(100*Math.random() <= probability) {
                            int chosenBlockade = randomSeed.nextInt(blockadesArray.size());
                            BlockadeInfo bi = blockadesArray.get(chosenBlockade);
                            LevelButton button = new LevelButton(0, 0, 32, 32,
                                    bi.getRoom1(), bi.getRoom2(), bi.getDirection());
                            finishedMap[i][j].addGameObject(button);
                            finishedMap[i][j].placeButton(button);
                            numberOfButtonsCreated++;
                            roomsWithButtons.add(new Coordinates(i, j));
                            blockadesArray.remove(chosenBlockade);
                        }
                    }
                }
            }
        }
    }
    /**
     * Using the prob attribute, randomly marks rooms from the boolean 3d matrix
     * as already checked to avoid these random rooms to be used for the map.
     * Helps into the creation of 'corridor rooms' instead of clustering all
     * rooms together.
     * @param preMap  3D matrix of booleans representing the map.
     */
    private void generateSparsity(boolean[][][] preMap) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i != initialRow && j != initialColumn) {
                    if (randomSeed.nextInt(prob) == 0) { // Probability = 1/prob
                        preMap[i][j][4] = true;
                    }
                }
            }
        }
    }
    
    /**
     * Print the finished map's ('map' attribute) representation in console.
     */
    private void printMap(Room[][] map) {

        System.out.println("Printing Map");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (map[i][j] == null) {
                    System.out.print("·");
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Returns a string representing the map that's passed as a parameter.
     * 
     * @param map
     *              Matrix with all the rooms which repesentation will be 
     *          returned.
     * 
     * @return
     *              A string with the ASCII representation of the map.
     */
    public static String getMapRepresentation (Room [][] map) {
        
        StringBuilder representation = new StringBuilder();
        
        for (Room[] mapRow : map) {
            
            for (int j = 0; j < map[0].length; j++) {
                
                if (mapRow[j] == null) {
                    
                    representation.append(".");
                } else {
                    
                    representation.append(mapRow[j]);
                }
            }
            
            representation.append("\n");
        }
        
        return new String(representation);
    }
    
    
    /**
     * Returns a matrix of rooms equivalent to the one represented on the string.
     * 
     * @param representation 
     *              The string representation of the level, like a map of a city
     *          or anything similar.
     * 
     * @param tileset 
     *              Tileset that the rooms will have.
     * 
     * @return 
     *              A new matrix of rooms with the given disposition on success,
     *          or <i>null</i> on error.
     */
    public static Room [][] getRepresentedMap (String representation, String tileset) {
        
        String[] representationRow = representation.split("\n");
        Room [][] retVal = new Room [representationRow[0].length()]
                                    [representationRow.length];
        
        /* Gets every row and initializes each room depending on the stored
        character */
        for (int i = 0; i < representationRow.length; i++) {
            
            for (int j = 0; j < representationRow[i].length(); j++) {
                /* Depending on the value of the character, generates a room */
                switch (representationRow[i].charAt(j)) {
                    case 'U':
                        retVal[i][j] = new Room("U", "1000-1", tileset,
                                                true, false, false, false);
                        break;
                        
                    case 'D':
                        retVal[i][j] = new Room("D", "0100-1", tileset, 
                                                false, true, false, false);
                        break;
                        
                    case 'L':
                        retVal[i][j] = new Room("L", "0010-1", tileset, 
                                                false, false, true, false);
                        break;
                        
                    case 'R':
                        retVal[i][j] = new Room("R", "0001-1", tileset,
                                                false, false, false, true);
                        break;
                        
                    case '║':
                        retVal[i][j] = new Room("║", "1100-1", tileset,
                                                true, true, false, false);
                        break;
                        
                    case '╗':
                        retVal[i][j] = new Room("╗", "0110-1", tileset, 
                                                false, true, true, false);
                        break;
                        
                    case '╚':
                        retVal[i][j] = new Room("╚", "1001-1", tileset, 
                                                 true, false, false, true);
                        break;
                        
                    case '╦':
                        retVal[i][j] = new Room("╦", "0111-1", tileset,
                                                false, true, true, true);
                        break;
                        
                    case '╣':
                        retVal[i][j] = new Room("╣", "1110-1", tileset, 
                                                true, true, true, false);
                        break;
                        
                    case '╬':
                        retVal[i][j] = new Room("╬", "1111-1", tileset, 
                                                true, true, true, true);
                        break;
                        
                    case '═':
                        retVal[i][j] = new Room("═", "0011-1", tileset,
                                                false, false, true, true);
                        break;
                        
                    case '╝':
                        retVal[i][j] = new Room("╝", "1010-1", tileset,
                                                true, false, true, false);
                        break;
                        
                    case '╔':
                        retVal[i][j] = new Room("╔", "0101-1", tileset,
                                                false, true, false, true);
                        break;
                        
                    case '╩':
                        retVal[i][j] = new Room("╩", "1011-1", tileset,
                                                true, false, true, true);
                        break;
                        
                    case '╠':
                        retVal[i][j] = new Room("╠", "1101-1", tileset, 
                                                true, true, false, true);
                        break;
                        
                    case '.':
                        retVal[i][j] = null;
                        break;
                        
                    default:
                        System.out.println("Error recognizing character '"
                                + representationRow[i].charAt(j) +
                                "' at MapGenerator.getRepresentedMap().");
                }
            }
        }
        
        return retVal;
    }
    
    
}
